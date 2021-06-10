package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.FormOfEducation;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

public class RemoveAllByFormOfEducationCommand  extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public RemoveAllByFormOfEducationCommand(StudyGroupCollection studyGroupCollection) {
        super("remove_all_by_form_of_education", "formOfEducation", "удалить все элементы с такой формой обучения");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String isValidArgument(String argument) {
        if(argument == null) return "Нет аргументов, возможные варианты: " + FormOfEducation.nameList();
        FormOfEducation formOfEducation = StudyGroupParser.parseFormOfEducation(argument);
        if (formOfEducation == null) return ("Не найдена такая форма образования. Возможные варианты: " + FormOfEducation.nameList());
        return "VALID";
    }

    @Override
    public String execute(Pack pack) {
        String argument = pack.getArgument();
        String answer = "";
        try {
            if (argument == null) throw new IllegalArgumentException("Нет аргументов, возможные варианты: " + FormOfEducation.nameList());
            FormOfEducation formOfEducation = StudyGroupParser.parseFormOfEducation(argument);
            if (formOfEducation == null)throw new IllegalArgumentException("Не найдена такая форма образования. Возможные варианты: " + FormOfEducation.nameList());
            else {
                for (Integer i : studyGroupCollection.getCollection().keySet()) {
                    if (formOfEducation.equals(studyGroupCollection.getById(i).getFormOfEducation())) {
                        studyGroupCollection.remove(i);
                        answer += "Элемент с Id " + i + " удален из коллекции\n";
                    }
                }
            }
            return answer + "Удаление завершено";
        } catch (IllegalArgumentException illegalArgumentException) {
            return illegalArgumentException.getMessage();
        }
    }
}
