package libriary.commands.client;

import libriary.commands.AbstractCommand;
import libriary.data.StudyGroupCollection;
import libriary.exception.DBExceprion;
import libriary.internet.DataBase;
import libriary.internet.Pack;
import libriary.internet.User;

import java.sql.SQLException;

public class RegistrationCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;
    private DataBase dataBase;

    public RegistrationCommand(StudyGroupCollection studyGroupCollection, DataBase dataBase) {
        super("reg", "<login> <password>", "Регистрация пользователя с таким логином с возможностью указать пароль");
        this.studyGroupCollection = studyGroupCollection;
        this.dataBase = dataBase;
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
                try {
                    int id = dataBase.insert(pack.getUserConnection().getUser());
                    User user = pack.getUserConnection().getUser();
                    user.setId(id);
                    return studyGroupCollection.addUser(user);
                } catch (SQLException | DBExceprion throwables) {
                    throwables.printStackTrace();
                    return "Ошибка, не получилось добавить пользователя в базу данных";
                }
            }
        }
    }
}
