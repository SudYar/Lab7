package sudyar.client;

import libriary.commands.Commands;
import libriary.internet.Pack;
import libriary.internet.User;
import libriary.internet.UserConnection;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registration {



    public static UserConnection logIn(Client client, Commands commands) throws IOException, ClassNotFoundException {
        UserConnection user = null;
        client.printLn("Пожалуйста зарегестрируйтесь (reg <login> <password>) или войдите (log <login> <password>), используя ваш логин и пароль \n(если нет пароля - будет установлен пустой пароль)\nМожете ввести help");
        while (user == null) {
            client.print("\u001B[32m$\u001B[0m");
            String line = client.readLine();
            String[] splitLine = line.trim().split(" ");
            if (splitLine.length < 1) client.printLn("Ошибка, поступила пустая строка");
            else {

                String commandName = splitLine[0];
                if (commands.getCommand(commandName) == null)
                    client.printLn("Не найдена команда:" + commandName + " Введите help, чтобы узнать возможные команды");
                else {
                    switch (commandName) {
                        case ("exit"):
                            client.printLn("Завершение работы");
                            System.exit(0);
                        case ("help"):
                            client.printLn(commands.getCommand("help").execute(null));
                            break;
                        default:
                            if (!(splitLine.length < 2) && !"".equals(splitLine[1].trim())) {
                                String login = splitLine[1];
                                String password = (splitLine.length > 2 ? splitLine[2] : "");
                                user = new UserConnection(new User(login, hashPassword(login, password)), null);
                                Pack authPack = new Pack(user, commands.getCommand(commandName), "");
                                client.sendPack(authPack);
                                authPack = client.readPack();
                                user = authPack.getUserConnection();
                                client.printLn(authPack.getAnswer());

                            } else client.printLn("Либо не введён логин, либо перед ним много пробелов");
                            break;
                    }
                }
            }
        }
        return user;

    }

    private static String hashPassword(String login, String password){
        MessageDigest md;
        byte[] hash = null;
        String newPassword = "";
        try {
            md = MessageDigest.getInstance("SHA-224");
            byte[] data = (password + login).getBytes(StandardCharsets.UTF_8);
            hash = md.digest(data);
            BigInteger no = new BigInteger(1, hash);
            newPassword = no.toString(16);

            while (newPassword.length() < 32){
                newPassword = "0" + newPassword;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return newPassword;
    }

}
