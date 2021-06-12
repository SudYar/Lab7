package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroup;
import libriary.data.StudyGroupCollection;
import libriary.exception.DuplicateException;
import libriary.internet.DataBase;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;


public class ReplaceIfLowe extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;

    public ReplaceIfLowe(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("replace_if_lowe", "id", "заменить значение по id, если новое значение меньше старого");
        this.studyGroupCollection = studyGroupCollection;
        this.dataBase = dataBase;
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
        StudyGroup s = pack.getStudyGroup();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        if (s == null) return "В пакете нет StudyGroup";
        Integer id = StudyGroupParser.parseId(argument);
        if (id == null) return "Аргумент не является int > 0";
        StudyGroup s2 = studyGroupCollection.getById(id);
        if ( s2 == null) return "Нет элемента с таким id";
        else if (s2.getIdOwner().equals(s.getIdOwner()))
            if (s2.compareTo(s)< 0) {
                try {
                    studyGroupCollection.update(id, s);
                    return "Элемент заменен";
                } catch (DuplicateException e) {
                    return e.getMessage();
                }
            } else return "Элемент не меньше старого";
        else return "Вы не владелец этой группы";
    }
}
