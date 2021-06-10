package libriary.utilities;

import libriary.data.*;
import libriary.exception.DuplicateException;

import java.util.Date;
import java.util.Locale;

public class StudyGroupParser {

//    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
//    private String name; //Поле не может быть null, Строка не может быть пустой
//    private Coordinates coordinates; //Поле не может быть null
//    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
//    private Integer studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
//    private FormOfEducation formOfEducation; //Поле может быть null
//    private Semester semesterEnum; //Поле не может быть null
//    private Person groupAdmin; //Поле может быть null

    public StudyGroupParser() {
    }

    public static Integer parseId (String value){
        try {
            int result = Integer.parseInt(value);
            if (result <=0) return null;
            return result;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String parseName (String value){
        if ("".equals(value)) return null;
        else return value;
    }

    public static Double parseX (String value) {
        try {
            double result = Double.parseDouble(value);
            if ((result < Coordinates.xMaxValue) && (result > Coordinates.xMinValue)) return result;
            else return null;
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public static Float parseY (String value) {
        try {
            float result = Float.parseFloat(value);
            if ((result < Coordinates.yMaxValue) && (result > Coordinates.yMinValue)) return result;
            else return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Coordinates parseCoordinates (Double x, Float y){

        if ((x != null) && ( y != null)) return new Coordinates(x,y);
        else return null;
    }

    public static Date parseDate (String value){
        Date date = new Date();
        Date date21 = new Date();
        date21.setTime(Long.parseLong("1609448400000"));
        try {
            date.setTime(Long.parseLong(value));
            if (date.before(date21)) return null;
            else return date;
        } catch (NumberFormatException e ) {
            return null;
        }

    }

    public static Integer parseStudentsCount (String value){
        try {
            int result = Integer.parseInt(value);
            if (result <= 0 ) return null;
            return result;
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public static FormOfEducation parseFormOfEducation (String value){
        String caps = value.toUpperCase(Locale.ROOT);
        try{
            return FormOfEducation.valueOf(caps);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Semester parseSemesterEnum (String value) {
        String caps = value.toUpperCase(Locale.ROOT);
        try{
            return Semester.valueOf(caps);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String parseNameAdmin (String value) {
        if ("".equals(value)) return null;
        else return value;
    }

    public static Double parseWeigh (String value) {
        try {
            double result = Double.parseDouble(value);
            if (result > 0) return result;
            else return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String parsePassportId (String value) {
        if ("".equals(value)) return null;
        else return value;
    }

    public static String parsePassportId (String value, StudyGroupCollection studyGroupCollection) throws DuplicateException {
        if ("".equals(value)) return null;
        if (studyGroupCollection.containsPassportId(value)) throw new DuplicateException("ERROR: Повторение passportId админа");
        else return value;
    }



    public static Person parsePerson (String name, Double weigh, String passportId) {
        if (name!= null && weigh != null && passportId != null) return new Person(name, weigh, passportId);
        else return null;
    }
}
