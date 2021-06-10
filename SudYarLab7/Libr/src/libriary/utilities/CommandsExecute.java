package libriary.utilities;

import libriary.commands.Command;
import libriary.commands.Commands;
import libriary.data.Coordinates;
import libriary.data.Semester;
import libriary.internet.Pack;

import java.util.List;

public class CommandsExecute {
    Commands commands;

    public CommandsExecute(Commands commands) {
        this.commands = commands;
    }

    public String execute(Pack pack){
        Command command = commands.getCommand( pack.getCommand().getName());
        if (command == null) return "Ошибка, в пакете нет команды";
        else if ("execute_script".equals(command.getName())) return executeScript(pack);
        else return command.execute(pack);
    }

    private String executeScript(Pack pack) {
        StringBuilder answer = new StringBuilder();
        List<String> list = pack.getScript();
        int i = 0;
        while (i < list.size()){
            String l = list.get(i);
            String[] line = l.trim().split(" ");
            Command command = commands.getCommand(line[0]);
            String argument = null;
            if (line.length > 1) argument = line[1];
            if (command != null) {
                if ("execute_script".equals(command.getName())) answer.append("Выполнение скрипта в скрипте запрещено\n");
                else if (commands.isNeedStudyGroup(command.getName())) {
                    StudyGroupBuilder builder = new StudyGroupBuilder();
                    try {

                        i++;
                        if (list.size() < i + 5 + 1) {
                            answer.append("Недостаточно полей в скрипте, чтобы заполнить StudyGroup\n");
                            continue; /* Если в скрипте не хватает строк, чтобы обработать минимальный набор группы, то сразу перестаем набирать */
                        }
                        if (!builder.addName(list.get(i++))) {
                            answer.append("Имя группы не может быть пустым\n");
                            continue;
                        }
                        if (!builder.addX(list.get(i++))) {
                            answer.append("X должен быть типа double, а не \"" + list.get(i - 1) + "\"\n");
                            continue;
                        }
                        if (!builder.addY(list.get(i++))) {
                            answer.append("Y должен быть типа float > " + Coordinates.yMinValue + " а не \"" + list.get(i - 1) + "\"\n");
                            continue;
                        }
                        if (!builder.addStudentCount(list.get(i++))) {
                            answer.append("StudentCount должен быть натуральным числом, а не \"" + list.get(i - 1) + "\"\n");
                            continue;
                        }
                        builder.addFormOfEducation(list.get(i++));
                        if (!builder.addSemester(list.get(i++))) {
                            answer.append("Semester должен быть " + Semester.nameList() + ", а не \"" + list.get(i - 1) + "\"\n");
                            continue;
                        }
                        if (builder.addNameAdmin(list.get(i++)) && (list.size() > (i + 1))) /*+1 чтобы прочитать ещё Passport */ {
                            if (builder.addWeight(list.get(i++))) {
                                if (!builder.addPassportId(list.get(i++)))
                                    answer.append("PassportId должна быть непустая строка\n");
                            } else answer.append("Weight должен быть типа double > 0\n" );
                        }
                        Pack newPack = new Pack(pack.getUserConnection(), command, argument, builder.toStudyGroup());
                        answer.append(command.execute(newPack)).append('\n');
                        continue;

                    } catch (IndexOutOfBoundsException e) {
                        answer.append("Недостаточно полей для создания новой StudyGroup\n");
                    }

                } else {
                    Pack newPack;
                    newPack = new Pack(pack.getUserConnection(), command, argument);
                    answer.append(command.execute(newPack)).append('\n');
                }
            } else answer.append("нет команды \"").append(line[0]).append("\"\n");
            i++;
        }
        return answer.append("Выполнение скрипта закончено").toString();
    }
}
