package libriary.commands;

import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;

public class RegistrationCommand extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public RegistrationCommand(StudyGroupCollection studyGroupCollection) {
        super("reg", "login password", "Регистрация пользователя с таким логином с возможностью указать пароль");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String isValidArgument(String argument) {
        if ("".equals(argument)) return "Логин должен быть уникальной непустой строкой";
        else return "VALID";
    }

    @Override
    public String execute(Pack pack) {
        if (pack.getUserConnection() == null) return "Ошибка, нет пользователя в файле";
        else {
            if (studyGroupCollection.getUser(
                    pack.getUserConnection().getUser().getLogin()) != null) return "Ошибка, такой логин уже занят";
            else {
                return studyGroupCollection.addUser(pack.getUserConnection().getUser());
            }
        }
    }
}
