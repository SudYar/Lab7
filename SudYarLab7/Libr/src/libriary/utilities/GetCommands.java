package libriary.utilities;

import libriary.commands.*;
import libriary.commands.client.*;
import libriary.commands.server.DisconnectCommand;
import libriary.commands.server.SaveCommand;
import libriary.commands.server.ShowConnectionCommand;
import libriary.data.StudyGroupCollection;

import java.util.HashSet;

public class GetCommands {


    public static Commands getClientCommands (StudyGroupCollection collection){
        HashSet<Command> set1 = new HashSet<>();
        HashSet<Command> set3 = new HashSet<>();
        Commands clientCommands = new Commands();
        set1.add(new HelpCommand(clientCommands));
        set1.add(new InfoCommand(collection));
        set1.add(new ShowCommand(collection));
        set1.add(new ShowOneCommand(collection));
        set1.add(new ExitCommand("Переподключение к серверу (возможность зайти под другим логином)"));
        set3.add(new InsertCommand(collection));
        set3.add(new UpdateCommand(collection));
        set1.add(new RemoveKeyCommand(collection));
        set1.add(new ClearCommand(collection));
        set3.add(new RemoveGreaterCommand(collection));
        set3.add(new ReplaceIfLowe(collection));
        set1.add(new RemoveGreaterKeyCommand(collection));
        set1.add(new RemoveAllByFormOfEducationCommand(collection));
        set1.add(new SumOfStudentsCountCommand(collection));
        set1.add(new CountByStudentsCountCommand(collection));
        set1.add(new ExecuteScriptCommand());


        clientCommands.setCommands(set1, set3);
        return clientCommands;
    }

    public static Commands getServerCommands(){
        HashSet<Command> set = new HashSet<>();
        Commands serverCommands = new Commands();

        set.add(new HelpCommand(serverCommands));
        set.add(new SaveCommand(null));
        set.add(new DisconnectCommand());
        set.add(new ShowConnectionCommand());
        set.add(new ExitCommand());

        serverCommands.setCommands(set);

        return serverCommands;
    }

    public static Commands getAuthCommands(StudyGroupCollection collection){
        HashSet<Command> set = new HashSet<>();
        Commands authCommands = new Commands();
        set.add(new HelpCommand(authCommands));
        set.add(new ExitCommand("Завершение программы"));
        set.add(new RegistrationCommand(collection));
        set.add(new LogInCommand(collection));

        authCommands.setCommands(set);

        return authCommands;
    }
}
