package libriary.commands.server;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;

public class SaveCommand extends AbstractCommand {
    private final StudyGroupCollection studyGroupCollection;
    public SaveCommand(StudyGroupCollection studyGroupCollection) {
        super("save", "Сохранение коллекции в файл");
        this.studyGroupCollection = studyGroupCollection;

    }

    @Override
    public String execute(Pack pack) {
//        if (fileParser.canWrite) return fileParser.unParse(studyGroupCollection);
//        else
        return "Нет возможности сохранить в файл";
    }
}
