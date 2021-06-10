package libriary.commands;

import java.util.HashMap;
import java.util.HashSet;

public class Commands {
    HashMap<String, Command> commands = new HashMap<>();
    HashSet<String> commandsNeedStudyGroup = new HashSet<>();

    public Commands(ClearCommand clearCommand,
                    CountByStudentsCountCommand countByStudentsCountCommand,
                    ExecuteScriptCommand executeScriptCommand,
                    ExitCommand exitCommand,
                    HelpCommand helpCommand,
                    InfoCommand infoCommand,
                    RemoveAllByFormOfEducationCommand removeAllByFormOfEducationCommand,
                    RemoveGreaterCommand removeGreaterCommand,
                    RemoveKeyCommand removeKeyCommand,
                    ReplaceIfLowe replaceIfLowe,
                    ShowCommand showCommand,
                    SumOfStudentsCountCommand sumOfStudentsCountCommand,
                    UpdateCommand updateCommand) {
        commands.put(clearCommand.getName(), clearCommand);
        commands.put(countByStudentsCountCommand.getName(), countByStudentsCountCommand);
        commands.put(executeScriptCommand.getName(), executeScriptCommand);
        commands.put(exitCommand.getName(), exitCommand);
        commands.put(helpCommand.getName(), helpCommand);
        commands.put(infoCommand.getName(), infoCommand);
        commands.put(removeAllByFormOfEducationCommand.getName(), removeAllByFormOfEducationCommand);
        commands.put(removeGreaterCommand.getName(), removeGreaterCommand);
        commands.put(removeKeyCommand.getName(), removeKeyCommand);
        commands.put(replaceIfLowe.getName(), replaceIfLowe);
        commands.put(showCommand.getName(), showCommand);
        commands.put(sumOfStudentsCountCommand.getName(), sumOfStudentsCountCommand);
        commands.put(updateCommand.getName(), updateCommand);
    }

    public Commands() {
    }

    public Commands(HashSet<Command> set){
        for (Command command: set) {
            commands.put(command.getName(), command);
        }
    }
    public Commands(HashSet<Command> set1, HashSet<Command> set2){
        for (Command command: set1) {
            commands.put(command.getName(), command);
        }
        for (Command command: set2) {
            commands.put(command.getName(), command);
            commandsNeedStudyGroup.add(command.getName());
        }
    }


    public void setCommands(HashSet<Command> set){
        for (Command command: set) {
            commands.put(command.getName(), command);
        }
    }

    public void setCommands(HashSet<Command> set1, HashSet<Command> set2){
        for (Command command: set1) {
            commands.put(command.getName(), command);
        }
        for (Command command: set2) {
            commands.put(command.getName(), command);
            commandsNeedStudyGroup.add(command.getName());
        }
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public Command getCommand(String name){
        return commands.get(name);
    }

    public boolean isNeedStudyGroup(String nameCommand){
        return commandsNeedStudyGroup.contains(nameCommand);
    }

    @Override
    public String toString() {
        String result = "";
        for (String i: commands.keySet()) {
            result+= i + (commands.get(i).getDescriptionArgument() == null ? "" : " "+ commands.get(i).getDescriptionArgument() ) + "\t\t" + commands.get(i).getDescription() + "\n";
        }

        return result.trim();
    }
}
