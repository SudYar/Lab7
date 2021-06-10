package libriary.commands;

import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;

/**
 * Очищает коллекцию
 *
 *
 */

public class ClearCommand  extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public ClearCommand(StudyGroupCollection studyGroupCollection) {
        super("clear", "Очистить коллекцию");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String toString() {
        return this.getName()+" : " + this.getDescription();
    }

    /**
     *Выполнение команды
     * @return Статус заверщения команды
     */
    @Override
    public String execute(Pack pack) {
        studyGroupCollection.clear();
        return "Коллекция очищена";
    }
}
