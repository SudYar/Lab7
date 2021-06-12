package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.DataBase;
import libriary.internet.Pack;

import java.sql.SQLException;

/**
 * Очищает коллекцию
 *
 *
 */

public class ClearCommand  extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;


    public ClearCommand(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("clear", "Очистить коллекцию");
        this.studyGroupCollection = studyGroupCollection;
        this.dataBase = dataBase;
    }

    @Override
    public String toString() {
        return this.getName()+" : " + this.getDescription();
    }

    /**
     *Выполнение команды
     * @return Статус заверщения команды
     */
    @Override
    public String execute(Pack pack) {
        int ownerId = pack.getUserConnection().getUser().getId();
        String answer = "";
        for (Integer i : studyGroupCollection.getCollection().keySet()) {
            if (studyGroupCollection.getById(i).getIdOwner().equals(ownerId) ) {
                try {
                    dataBase.deleteStudyGroup(i);
                    studyGroupCollection.remove(i);
                    answer += "Элемент с Id " + i + " удален из коллекции\n";
                } catch (SQLException throwables) {
                    answer += "Не получилось удалить элемент в Id " + i + " по причине: " + throwables.getMessage() + "\n";
                }
            }
        }
        return answer + "Все ваши группы удалены";
    }
}
