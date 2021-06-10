package libriary.commands;

import libriary.internet.Pack;

public class ExitCommand  extends AbstractCommand{

    public ExitCommand() {
        super("exit", "Завершение программы (с сохраненением в файл)");
    }

    public ExitCommand(String description) {
        super("exit", description);
    }

    @Override
    public String execute(Pack pack) {
        System.exit(0);
        return "Завершение программы";
    }

}
