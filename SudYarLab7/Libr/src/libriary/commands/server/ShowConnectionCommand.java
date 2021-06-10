package libriary.commands.server;

import libriary.commands.AbstractCommand;

public class ShowConnectionCommand extends AbstractCommand {
    public ShowConnectionCommand() {
        super("show_con", "Вывести все подключения");
    }
}
