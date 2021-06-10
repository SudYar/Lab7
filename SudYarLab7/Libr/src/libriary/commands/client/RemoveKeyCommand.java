package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

public class RemoveKeyCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public RemoveKeyCommand(StudyGroupCollection studyGroupCollection) {
        super("remove", "id", "Удалить элемент с данным id из коллекции");
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
        try {
            int id = Integer.parseInt(argument);
            if (!studyGroupCollection.getCollection().containsKey(id))
                return "Такого Id нет в коллекции";
            studyGroupCollection.remove(id);
            return "Элемент удален из коллекции";
        } catch (NumberFormatException e) {
            return "Неверный тип аргумента. В аргументы подается id типа int";
        }
    }
}
