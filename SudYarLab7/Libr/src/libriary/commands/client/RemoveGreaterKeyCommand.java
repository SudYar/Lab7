package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.DataBase;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

import java.sql.SQLException;

public class RemoveGreaterKeyCommand extends AbstractCommand {

    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;

    public RemoveGreaterKeyCommand(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("remove_greater_key", "id", "Удалить из коллекции все аргументы, ключ которых превышает заданный");
        this.studyGroupCollection = studyGroupCollection;
        this.dataBase = dataBase;
    }
    @Override
    public String isValidArgument(String argument) {
        if (argument == null) return "Нет аргументов, требуется id типа int > 0";
        if (StudyGroupParser.parseId(argument) == null) return "Неверный тип аргумента. В аргументы подается int > 0";
        else return "VALID";
    }

    @Override
    public String  execute(Pack pack) {
        String argument = pack.getArgument();
        Integer idOwner = pack.getUserConnection().getUser().getId();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        String answer = "";
        try {
            Integer id = Integer.parseInt(argument);
            for (Integer i : studyGroupCollection.getCollection().keySet()) {
                if (id.compareTo(i) < 0 && idOwner.equals(studyGroupCollection.getById(id).getIdOwner())) {
                    try {
                        dataBase.deleteStudyGroup(i);
                        studyGroupCollection.remove(i);
                        answer += "Элемент с Id " + i + " удален из коллекции\n";
                    } catch (SQLException throwables) {
                        answer+= "Не получилось удалить элемент с Id = " + i + " Ошибка: " + throwables.getMessage() +"\n";
                    }
                }
            }
            answer += "Удаление завершено";
            return answer;
        } catch (NumberFormatException e) {
            return "Неверный тип аргумента. В аргументы подается id типа int";
        }
    }
}
