package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

public class RemoveGreaterKeyCommand extends AbstractCommand {

    private StudyGroupCollection studyGroupCollection;

    public RemoveGreaterKeyCommand(StudyGroupCollection studyGroupCollection) {
        super("remove_greater_key", "id", "Удалить из коллекции все аргументы, ключ которых превышает заданный");
        this.studyGroupCollection = studyGroupCollection;
    }
    @Override
    public String isValidArgument(String argument) {
        if (argument == null) return "Нет аргументов, требуется id типа int > 0";
        if (StudyGroupParser.parseId(argument) == null) return "Неверный тип аргумента. В аргументы подается int > 0";
        else return "VALID";
    }

    @Override
    public String  execute(Pack pack) {
        String argument = pack.getArgument();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        try {
            Integer id = Integer.parseInt(argument);
            for (Integer i : studyGroupCollection.getCollection().keySet()) {
                if (id.compareTo(i) < 0) {
                    studyGroupCollection.remove(i);
                    return "Элемент с Id " + i + " удален из коллекции";
                }
            }
            return "Удаление завершено";
        } catch (NumberFormatException e) {
            return "Неверный тип аргумента. В аргументы подается id типа int";
        }
    }
}
