package libriary.commands;

import libriary.data.StudyGroupCollection;
import libriary.utilities.FileParser;
import libriary.internet.Pack;

public class SaveCommand extends AbstractCommand {
    private final StudyGroupCollection studyGroupCollection;
    private final FileParser fileParser;
    public SaveCommand(StudyGroupCollection studyGroupCollection, FileParser fileParser) {
        super("save", "Сохранение коллекции в файл");
        this.studyGroupCollection = studyGroupCollection;
        this.fileParser = fileParser;

    }

    @Override
    public String execute(Pack pack) {
        if (fileParser.canWrite) return fileParser.unParse(studyGroupCollection);
        else return "Нет возможности сохранить в файл";
    }
}
