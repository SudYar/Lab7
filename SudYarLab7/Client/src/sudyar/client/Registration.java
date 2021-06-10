package sudyar.client;

import libriary.commands.Commands;
import libriary.internet.Pack;
import libriary.internet.User;
import libriary.internet.UserConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registration {



    public static UserConnection logIn(Client client, Commands commands) throws IOException, ClassNotFoundException {
        UserConnection user = null;
        client.print("Пожалуйста зарегестрируйтесь или войдите, используя ваш логин и пароль (если нет пароля - будет установлен пустой пароль\nМожете ввести help\n$");
        while (user == null) {
            String line = client.readLine();
            String[] splitLine = line.trim().split(" ");
            if (splitLine.length < 1) client.print("Ошибка, поступила пустая строка\n$");
            else {

                String commandName = splitLine[0];
                if (commands.getCommand(commandName) == null)
                    client.print("Не найдена команда:" + commandName + " Введите help, чтобы узнать возможные команды\n$");
                else {
                    switch (commandName) {
                        case ("exit"):
                            client.print("Завершение работы");
                            System.exit(0);
                        case ("help"):
                            client.print(commands.getCommand("help").execute(null) + "\n$");
                            break;
                        default:
                            if (!(splitLine.length < 2) && !"".equals(splitLine[1].trim())) {
                                String login = splitLine[1];
                                String password = (splitLine.length > 2 ? splitLine[2] : "");
                                user = new UserConnection(new User(login, hashPassword(login, password)), null);
                                Pack authPack = new Pack(user, commands.getCommand(commandName), "");
                                client.printLn(authPack.toString());
                                client.sendPack(authPack);
                                authPack = client.readPack();
                                user = authPack.getUserConnection();
                                client.printLn(authPack.getAnswer());

                            } else client.print("Либо не введён логин, либо перед ним много пробелов\n$");
                            break;
                    }
                }
            }
        }
        return user;

    }

    private static byte[] hashPassword(String login, String password){
        MessageDigest md;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-224");
            byte[] data = (password + login).getBytes(StandardCharsets.UTF_8);
            hash = md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

}
