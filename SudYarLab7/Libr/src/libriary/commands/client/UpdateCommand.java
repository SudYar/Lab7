package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.*;
import libriary.exception.DBExceprion;
import libriary.exception.DuplicateException;
import libriary.internet.DataBase;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

import java.sql.SQLException;


public class UpdateCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;

    public UpdateCommand(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("update","id", "Обновить значение элемента коллекции, id которого равен введенному");
        this.studyGroupCollection = studyGroupCollection;
        this.dataBase = dataBase;
    }

    public String isValidArgument(String argument) {
        if (argument == null) return "Нет аргументов, требуется id типа int > 0";
        if (StudyGroupParser.parseId(argument) == null) return "Неверный тип аргумента. В аргументы подается int > 0";
        else return "VALID";
    }

    @Override
    public String execute(Pack pack) {
        String argument = pack.getArgument();
        StudyGroup s = pack.getStudyGroup();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        if (s == null) return "В пакете нет StudyGroup";
        Integer id = StudyGroupParser.parseId(argument);
        if (id == null) return "Аргумент не является int > 0";

        if ( !studyGroupCollection.getCollection().containsKey(id)) return "Нет элемента с таким id";
        else if (!studyGroupCollection.getById(id).getIdOwner().equals(s.getIdOwner())) return "У вас нет прав изменять чужую группу";
        else try {
            dataBase.update(id, s);
            studyGroupCollection.update(id, s);
            return "Элемент заменен";
        } catch (DuplicateException | DBExceprion e) {
            return e.getMessage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "Непредвиденная SQL ошибка: " + throwables.getMessage();
        }
    }
}
