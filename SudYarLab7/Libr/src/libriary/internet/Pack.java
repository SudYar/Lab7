package libriary.internet;

import libriary.commands.Command;
import libriary.data.StudyGroup;

import java.io.Serializable;
import java.util.List;

public class Pack implements Serializable {
    private static final long serialVersionUID = 100L;
    Command command;
    String argument;
    StudyGroup studyGroup;
    String answer;
    UserConnection user;
    List<String > script;

    public Pack(UserConnection user, Command command, List<String> script) {
        this.command = command;
        this.script = script;
        this.user = user;
    }
    public Pack(UserConnection user, Command command, String argument, StudyGroup studyGroup) {
        this.command = command;
        this.argument = argument;
        this.studyGroup = studyGroup;
        this.user = user;
    }

    public Pack(UserConnection user, Command command, String argument) {
        this.command = command;
        this.argument = argument;
        
    }

    public Pack(String answer) {
        this.answer = answer;
    }

    public Pack(Command command, StudyGroup studyGroup) {
        this.command = command;
        this.studyGroup = studyGroup;
    }

    public Command getCommand() {
        return command;
    }

    public String getArgument() {
        return argument;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public String getAnswer() {
        return answer;
    }

    public void setConnection (UserConnection user){this.user = user;}

    public UserConnection getUserConnection() {
        return user;
    }

    public List<String> getScript() {
        return script;
    }

    @Override
    public String toString() {
        return (user == null ? "" : " user: " + command.getName() + "\n") +
                (command == null ? "" : " command: " + command.getName() + "\n") +
                (argument == null ? "" : " argument: " + argument + '\n') +
                (studyGroup == null ? "" : " studyGroup: " + studyGroup + "\n")+
                (answer == null ? "" : " answer: " + answer + '\n') +
                (script == null ? "" : " script: " + script);
    }
}
