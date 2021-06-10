package libriary.commands;


public class ExecuteScriptCommand  extends AbstractCommand{
    public ExecuteScriptCommand() {
        super("execute_script","file_name", "Считать и исполнить скрипт с данного файла. В скрипте команды должны выглядеть так же как в интерактивном режиме");
    }

}
