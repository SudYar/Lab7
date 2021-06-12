package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.exception.DBExceprion;
import libriary.exception.DuplicateException;
import libriary.internet.DataBase;
import libriary.internet.Pack;

import java.sql.SQLException;

public class InsertCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;

    public InsertCommand(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("insert", "Добавить новую учебную группу в коллекицю");
        this.studyGroupCollection = studyGroupCollection;
        this.dataBase = dataBase;
    }

    @Override
    public String  execute(Pack pack) {
        try {
            if (pack.getStudyGroup() != null){
                int id = dataBase.insert(pack.getStudyGroup(), pack.getUserConnection().getUser().getId(), pack.getUserConnection().getUser().getLogin());
                studyGroupCollection.insert(id, pack.getStudyGroup());
                return "Группа добавлена";
            }
            return "ERROR: в пакете нет StudyGroup";

        } catch (DuplicateException e) {
            return e.getMessage();
        } catch (DBExceprion dbExceprion) {
            return dbExceprion.getMessage();
        } catch (SQLException throwables) {
            return "ERROR: Повторение passportId админа" ;
        }
    }
    
}
