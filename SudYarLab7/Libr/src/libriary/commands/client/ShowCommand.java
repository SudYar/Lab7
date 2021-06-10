package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;

public class ShowCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public ShowCommand(StudyGroupCollection studyGroupCollection) {
        super("show", "Вывести все элементы коллекции");
        this.studyGroupCollection = studyGroupCollection;
    }

    public ShowCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public String execute(Pack pack) {
        return studyGroupCollection.toString();

    }
}
