package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.DataBase;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

import java.sql.SQLException;

public class RemoveKeyCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;

    public RemoveKeyCommand(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("remove", "id", "Удалить элемент с данным id из коллекции");
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
    public String execute(Pack pack) {
        String argument = pack.getArgument();
        int idOwner = pack.getUserConnection().getUser().getId();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        try {
            int id = Integer.parseInt(argument);
            if (!studyGroupCollection.getCollection().containsKey(id))
                return "Такого Id нет в коллекции";
            if (studyGroupCollection.getById(id).getIdOwner().equals(idOwner)) {
                dataBase.deleteStudyGroup(id);
                studyGroupCollection.remove(id);
                return "Элемент удален из коллекции";
            }else return "Вы не владелец этой группы";
        } catch (NumberFormatException e) {
            return "Неверный тип аргумента. В аргументы подается id типа int";
        } catch (SQLException throwables) {
            return "Локальная коллекция не соответсвует базе данных";
        }
    }
}
