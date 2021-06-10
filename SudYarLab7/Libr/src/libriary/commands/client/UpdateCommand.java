package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.*;
import libriary.exception.DuplicateException;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;


public class UpdateCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public UpdateCommand(StudyGroupCollection studyGroupCollection) {
        super("update","id", "Обновить значение элемента коллекции, id которого равен введенному");
        this.studyGroupCollection = studyGroupCollection;
    }

    public String isValidArgument(String argument) {
        if (argument == null) return "Нет аргументов, требуется id типа int > 0";
        if (StudyGroupParser.parseId(argument) == null) return "Неверный тип аргумента. В аргументы подается int > 0";
        else return "VALID";
    }

    @Override
    public String execute(Pack pack) {
        String argument = pack.getArgument();
        StudyGroup s = pack.getStudyGroup();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        if (s == null) return "В пакете нет StudyGroup";
        Integer id = StudyGroupParser.parseId(argument);
        if (id == null) return "Аргумент не является int > 0";

        if ( !studyGroupCollection.getCollection().containsKey(id)) return "Нет элемента с таким id";
        else try {
            studyGroupCollection.update(id, s);
            return "Элемент заменен";
        } catch (DuplicateException e) {
            return e.getMessage();
        }
    }
}
