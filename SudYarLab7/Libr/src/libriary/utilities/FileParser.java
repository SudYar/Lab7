package libriary.utilities;

import com.sun.istack.internal.NotNull;
import libriary.data.*;
import libriary.exception.DuplicateException;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
    public final String path;
    public final boolean canRead;
    public final boolean canWrite;
    public final boolean isFile;
    private File file;


    public FileParser(@NotNull String path) throws FileNotFoundException {
        this.path = path;
        file = new File(path);

        isFile = file.isFile();
        if (!isFile) {
            canWrite = false;
            canRead = false;
        } else{
            canWrite = file.canWrite();
            canRead = file.canRead();
        }


        if (!file.exists()) {
            throw new FileNotFoundException();
        }

    }

    public FileParser() {
        canWrite = false;
        canRead = false;
        isFile =  false;
        path = null;
    }


    private StudyGroup parseGroup (BufferedReader br, int groupNumber) throws NullPointerException, DuplicateException {
        StudyGroup studyGroup = new StudyGroup();
        String line;
        String regex = "<([^>]+)>([^<]*)</([^>]+)>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        HashMap<String, String> teg = new HashMap<>();
        teg.put("id", "");
        teg.put("name", "");
        teg.put("x", "");
        teg.put("y", "");
        teg.put("creationDate", "");
        teg.put("studentsCount", "");
        teg.put("semester", "");
        teg.put("formOfEducation", "");
        teg.put("nameAdmin", "");
        teg.put("weight", "");
        teg.put("passportID", "");
        try {
            line = br.readLine();
            while ((line != null) && (!("</StudyGroup>").equals(line.trim()))) {
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String strTeg = matcher.group(1); //line.substring(matcher.start(1) , matcher.end(1));
                    if (teg.containsKey(strTeg))
                        if ("".equals(teg.get(strTeg))){
                            String strValue = matcher.group(2);  //line.substring(matcher.start(2), matcher.end(2));
                            teg.put(strTeg, strValue);
                        } else throw  new NullPointerException("Error: повторяются теги у " + groupNumber + " группы. Группа пропущена");
                }
                line = br.readLine();
            }
            if (line == null) throw new NullPointerException("Error: найдена пустая строка у " + groupNumber + ". Группа пропущена");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
        final String name; //Поле не может быть null, Строка не может быть пустой
        final Coordinates coordinates; //Поле не может быть null
        final java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
        final Integer studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
        final FormOfEducation formOfEducation; //Поле может быть null
        final Semester semesterEnum; //Поле не может быть null
        final Person groupAdmin;

        id = StudyGroupParser.parseId(teg.get("id"));
        if (id == null) throw new NullPointerException("Error: В файле ошибка у " + groupNumber + " группы, id не типа int или < 0. Группа пропущена");
        else studyGroup.setId(id);

        name = StudyGroupParser.parseName(teg.get("name"));
        if (name == null) throw new NullPointerException("Error: В файле ошибка у " + groupNumber + " группы, пустое название группы. Группа пропущена");
        else studyGroup.setName(name);

        coordinates = StudyGroupParser.parseCoordinates(StudyGroupParser.parseX(teg.get("x")),
                                                        StudyGroupParser.parseY(teg.get("y")));
        if (coordinates == null) throw  new NullPointerException("Error: В файле ошибка у " + groupNumber + " группы, неверный формат координаты (Double x, float y > " + Coordinates.yMinValue + "). Группа пропущена");
        else studyGroup.setCoordinates(coordinates);

        creationDate = StudyGroupParser.parseDate(teg.get("creationDate"));
        if (creationDate == null) throw  new NullPointerException("Error: В файле ошибка у " + groupNumber + " группы, неверный формат даты (Long - колличество миллисекунд с 01/01/1900, не раньше 01/01/2021). Группа пропущена");
        else studyGroup.setCreationDate(creationDate);

        studentsCount = StudyGroupParser.parseStudentsCount(teg.get("studentsCount"));
        if (studentsCount == null) throw new NullPointerException("Error: В файле ошибка у " + groupNumber + " группы, неверный формат studentCount (Integer studentCount > 0). Группа пропущена");
        else studyGroup.setStudentsCount(studentsCount);

        semesterEnum = StudyGroupParser.parseSemesterEnum(teg.get("semester"));
        if (semesterEnum == null) throw new NullPointerException("Error: В файле ошибка у " + groupNumber + " группы, семестр не удовлетворяет SemesterEnum. Группа пропущена");
        else studyGroup.setSemesterEnum(semesterEnum);

        formOfEducation = StudyGroupParser.parseFormOfEducation(teg.get("formOfEducation"));
        if (formOfEducation != null) studyGroup.setFormOfEducation(formOfEducation);

        groupAdmin = StudyGroupParser.parsePerson(StudyGroupParser.parseNameAdmin(teg.get("nameAdmin")),
                                                  StudyGroupParser.parseWeigh(teg.get("weight")),
                                                  StudyGroupParser.parsePassportId(teg.get("passportID")));
        if (groupAdmin != null) studyGroup.setGroupAdmin(groupAdmin);

        return studyGroup;
    }

    public StudyGroupCollection parse(){
        StudyGroupCollection sgc = new StudyGroupCollection();
        String line;
        int groupNumber = 0;
        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(
                                new FileInputStream(file)));){


            line = br.readLine();
            String END = "</StudyGroupCollection>";
            while ((line != null) && (!END.equals(line))){
                 if ("<StudyGroup>".equals(line.trim())) {
                     groupNumber++;
                     try{
                         StudyGroup studyGroup = parseGroup(br, groupNumber);
                         sgc.add(studyGroup);
                     } catch (NullPointerException e) {
                         System.out.println(e.getMessage());
                     } catch (DuplicateException e) {
                         System.out.println(e.getMessage() + " у " + groupNumber + " группы");
                     }
                 }
                 line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Непредвиденная ошибка в прочтении коллекции из файла");;
        }
        return sgc;
    }

    /**
     * Сохраняет коллекцию в файл
     * @param studyGroupCollection коллекция которую мы подаем для сохранения в файл
     */
    public String unParse(StudyGroupCollection studyGroupCollection) {
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("<?xml version=\"1.0\"?>");
            if (!studyGroupCollection.isEmpty()) {
                pw.println("<StudyGroupCollection>");
                for (StudyGroup studyGroup : studyGroupCollection.getCollection().values()) {
                    pw.println("\t<StudyGroup>");
                    pw.println("\t\t<id>" + studyGroup.getId() + "</id>");
                    pw.println("\t\t<name>" + studyGroup.getName() + "</name>");
                    pw.println("\t\t<coordinates>");
                    pw.println("\t\t\t<x>" + studyGroup.getCoordinates().getX() + "</x>");
                    pw.println("\t\t\t<y>" + studyGroup.getCoordinates().getY() + "</y>");
                    pw.println("\t\t</coordinates>");
                    pw.println("\t\t<creationDate>" + studyGroup.getCreationDate().getTime() + "</creationDate>");
                    pw.println("\t\t<studentsCount>" + studyGroup.getStudentsCount() + "</studentsCount>");
                    pw.println("\t\t<semester>" + studyGroup.getSemesterEnum() + "</semester>");
                    if (studyGroup.getFormOfEducation() != null)
                        pw.println("\t\t<formOfEducation>" + studyGroup.getFormOfEducation() + "</formOfEducation>");
                    if (studyGroup.getGroupAdmin() != null) {
                        pw.println("\t\t<groupAdmin>");
                        pw.println("\t\t\t<nameAdmin>" + studyGroup.getGroupAdmin().getName() + "</nameAdmin>");
                        pw.println("\t\t\t<weight>" + studyGroup.getGroupAdmin().getWeight() + "</weight>");
                        pw.println("\t\t\t<passportID>" + studyGroup.getGroupAdmin().getPassportID() + "</passportID>");
                        pw.println("\t\t</groupAdmin>");
                    }
                    pw.println("\t</StudyGroup>");
                }
                pw.println("</StudyGroupCollection>");
                return ("Коллекция успешно сохранена!");
            }
            else return ("Пустая коллекция \"сохранена\"");
        } catch (FileNotFoundException e) {
            return ("Файл не найден");
        }

    }
}