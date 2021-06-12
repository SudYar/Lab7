package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroup;
import libriary.data.StudyGroupCollection;
import libriary.internet.DataBase;
import libriary.internet.Pack;

import java.sql.SQLException;

public class RemoveGreaterCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;

    public RemoveGreaterCommand(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("remove_greater", "Удалить все элементы превышающие заданный");
        this.studyGroupCollection = studyGroupCollection;
        this.dataBase = dataBase;
    }


    @Override
    public String execute(Pack pack) {
        StudyGroup studyGroup = pack.getStudyGroup();
        int idOwner = studyGroup.getIdOwner();
        if (studyGroup == null) return "ERROR: В пакете нет StudyGroup";

        String answer = "";
        for (int i : studyGroupCollection.getCollection().values().stream().filter(group -> group.getIdOwner().equals(idOwner)).
                filter(group -> studyGroup.compareTo(group) < 0).mapToInt(StudyGroup::getId).toArray()) {
            try {
                dataBase.deleteStudyGroup(i);
                studyGroupCollection.remove(i);
            } catch (SQLException throwables) {
                answer += "Не получилось удалить элемент с Id = " + i + " Ошибка: " + throwables.getMessage() +"\n";
            }
        }

        answer +="Все элементы коллекции, превышающие заданный, удалены";
        return  answer;

    }
}
