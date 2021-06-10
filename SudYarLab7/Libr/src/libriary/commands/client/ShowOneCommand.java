package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

public class ShowOneCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public ShowOneCommand(StudyGroupCollection studyGroupCollection) {
        super("show_one","id", "Вывести элемент с данным id");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String isValidArgument(String argument) {
        if (argument == null) return "Нет аргументов, требуется id типа int > 0";
        if (StudyGroupParser.parseId(argument) == null) return "Неверный тип аргумента. В аргументы подается int > 0";
        else return "VALID";
    }

    @Override
    public String execute(Pack pack) {
        String argument = pack.getArgument();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        Integer id = StudyGroupParser.parseId(argument);
        if (id == null) return "Аргумент не является int > 0";

        if ( !studyGroupCollection.getCollection().containsKey(id)) return "Нет элемента с таким id";
        else return studyGroupCollection.getById(id).toString();

    }
}
