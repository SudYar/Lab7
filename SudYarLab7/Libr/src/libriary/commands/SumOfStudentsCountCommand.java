package libriary.commands;

import libriary.data.StudyGroup;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;

public class SumOfStudentsCountCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public SumOfStudentsCountCommand (StudyGroupCollection studyGroupCollection){
        super("sum_of_student_count" , "Выводит сумму studentsCount из всех элементов коллекции");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String execute(Pack pack) {
        int sumOfStudentsCount = studyGroupCollection.getCollection().values().stream().mapToInt(StudyGroup::getStudentsCount).sum();
        if (sumOfStudentsCount == 0) return ("Коллекция пуста, тут нет групп");
        else return ("Колличество студентов: " + sumOfStudentsCount);
    }
}
