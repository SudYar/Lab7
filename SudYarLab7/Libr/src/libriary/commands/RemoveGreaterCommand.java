package libriary.commands;

import libriary.data.StudyGroup;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;

public class RemoveGreaterCommand extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public RemoveGreaterCommand(StudyGroupCollection studyGroupCollection) {
        super("remove_greater", "Удалить все элементы превышающие заданный");
        this.studyGroupCollection = studyGroupCollection;
    }


    @Override
    public String execute(Pack pack) {
        StudyGroup studyGroup = pack.getStudyGroup();
        if (studyGroup == null) return "ERROR: В пакете нет StudyGroup";

        for (int i : studyGroupCollection.getCollection().values().stream().filter(group -> studyGroup.compareTo(group) < 0).
                mapToInt(StudyGroup::getId).toArray()) {
           studyGroupCollection.remove(i);
        }


        return  "Все элементы коллекции, превышающие заданный, удалены";

    }
}
