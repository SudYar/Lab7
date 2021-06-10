package libriary.commands;

import libriary.internet.Pack;

public class HelpCommand extends AbstractCommand{
    private Commands thisCommands;


    public HelpCommand(String name, String description) {
        super(name, description);
    }

    public HelpCommand(Commands commands){
        super("help", "Вывод описания команд");
        thisCommands = commands;
    }

    @Override
    public String execute(Pack pack) {
        return (thisCommands == null ? "Ошибка, не переданы команды в help, обратитесь к разработчику" : thisCommands.toString());
    }
}
