package libriary.commands;

import libriary.data.StudyGroup;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.utilities.StudyGroupParser;

public class CountByStudentsCountCommand extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public CountByStudentsCountCommand(StudyGroupCollection studyGroupCollection) {
        super("count_by_students_count", "studentsCount", "Вывести колличество элементов, значение studentsCount которых равно данному");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String isValidArgument(String argument) {
        if (argument == null) return "Нет аргументов, требуется id типа int";
        Integer studentsCount = StudyGroupParser.parseStudentsCount(argument);
        if (studentsCount == null) return "Неверный аргумент, пожалуйста, введите int > 0";

        return "VALID";
    }

    @Override
    public String execute(Pack pack) {
        String argument = pack.getArgument();
        try {
            if (argument == null) throw new IllegalArgumentException("Нет аргументов, требуется id типа int");
            Integer studentsCount = StudyGroupParser.parseStudentsCount(argument);
            if (studentsCount == null)
                throw new IllegalArgumentException("Неверный аргумент, пожалуйста, введите int > 0");
            else {
                int count = (int) studyGroupCollection.getCollection().values().stream().mapToInt(StudyGroup::getStudentsCount).filter(studentsCount::equals).count();
                return "Колличество элементов с таким же studentCount: " + count;
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            return illegalArgumentException.getMessage();
        }
    }
}
