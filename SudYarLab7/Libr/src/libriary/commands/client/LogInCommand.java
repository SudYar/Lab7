package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.internet.User;

import java.util.Arrays;

public class LogInCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public LogInCommand(StudyGroupCollection studyGroupCollection) {
        super("log", "login password", "Вход под данными логином и паролем через пробел (если не было пароля, не вводите его)");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String isValidArgument(String argument) {
        if ("".equals(argument)) return "Логин должен быть непустой строкой";
        else return "VALID";
    }

    @Override
    public String execute(Pack pack) {
        if (pack.getUserConnection() == null) return "Ошибка, нет пользователя в файле";
        else {
            User user = studyGroupCollection.getUser(pack.getUserConnection().getUser().getLogin());
            if (user != null)
                if (user.getPassword().equals(pack.getUserConnection().getUser().getPassword()))
                    return "Подключение успешно";
                else return "Пароль не совпадает " ;
            else {
                return "Такой логин не найден";
            }
        }
    }
}
