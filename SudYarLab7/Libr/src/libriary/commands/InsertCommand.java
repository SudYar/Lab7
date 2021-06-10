package libriary.commands;

import libriary.data.StudyGroupCollection;
import libriary.exception.DuplicateException;
import libriary.internet.Pack;

public class InsertCommand extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public InsertCommand(StudyGroupCollection studyGroupCollection) {
        super("insert", "Добавить новую учебную группу в коллекицю");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String  execute(Pack pack) {
        try {
            if (pack.getStudyGroup() != null){
                studyGroupCollection.insert(pack.getStudyGroup());
                return "Группа добавлена";
            }
            return "ERROR: в пакете нет StudyGroup";

        } catch (DuplicateException e) {
            return e.getMessage();
        }
    }
    
}
