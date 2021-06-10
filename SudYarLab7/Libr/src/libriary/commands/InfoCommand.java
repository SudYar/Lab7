package libriary.commands;

import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;

public class InfoCommand  extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public InfoCommand(StudyGroupCollection studyGroupCollection) {
        super("info", "Выводит тип и количество элементов коллеции");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String execute(Pack pack) {
        return (studyGroupCollection.getInfo());
    }

    public InfoCommand(String name, String description) {
        super(name, description);
    }
}
