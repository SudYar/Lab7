package libriary.commands.server;

import libriary.commands.AbstractCommand;
import libriary.utilities.StudyGroupParser;

public class DisconnectCommand extends AbstractCommand {

    public DisconnectCommand() {
        super("disconnect", "id_connection", "Отключить определенное подключение");
    }

    @Override
    public String isValidArgument(String argument) {
        if (StudyGroupParser.parseId(argument) == null) return "Неверный тип id. Должно быть int > 0";
        else return "VALID";
    }
}
