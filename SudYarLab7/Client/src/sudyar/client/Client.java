package sudyar.client;



import libriary.commands.*;
import libriary.data.StudyGroup;
import libriary.internet.Pack;
import libriary.internet.UserConnection;
import libriary.utilities.GetCommands;
import libriary.utilities.Serializer;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Client {

    Socket clientSocket;

    Commands commands;
    boolean isConnected = false;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String  readLine (){
        return Main.readLine();
    }

    public void printLn(String line){
        System.out.println(line);
    }

    public void print(String line) {
        System.out.print(line);
    }

    public void run (Commands comm) throws IOException, ClassNotFoundException {
        this.commands = comm;
        String answer = readPack().getAnswer();
        if (!"Подключение удалось".equals(answer)) System.out.println(answer);
        else {
            isConnected = true;

            UserConnection user = Registration.logIn(this, GetCommands.getAuthCommands(null, null));

            String line;
            while (isConnected) {
                print("\u001B[32m$\u001B[0m");
                line = readLine();
                String[] command = line.trim().split(" ", 3);
                String argument;
                if (command.length > 1) argument = command[1];
                else argument = null;
                for (int i = 0; i < command.length; i++) command[i] = command[i].trim();

                if (commands.getCommands().containsKey(command[0])) {
                    if ("help".equals(command[0])) {
                        System.out.println(commands);
                    } else if ("exit".equals(command[0])) {
                        printLn("Переподключение к серверу");
                        isConnected = false;
                    } else if ("execute_script".equals(command[0])) {
                        Pack pack = readScript(user, argument);
                        if (pack != null) {
                            sendPack(pack);
                            if (!isConnected) continue;
                            Pack answerPack = readPack();
                            if (answerPack != null && !"".equals(answerPack.getAnswer()))
                                System.out.println(answerPack.getAnswer());
                            else System.out.println("Ошибка при получении пакета");

                        }
                    } else {
                        Pack request;
                        Command thisCommand = commands.getCommand(command[0]);
                        if (!"VALID".equals(thisCommand.isValidArgument(argument))) {
                            System.out.println(thisCommand.isValidArgument(argument));
                            continue;
                        }
                        if (commands.isNeedStudyGroup(command[0])) {
                            if ("update".equals(command[0]) || "replace_if_lowe".equals(command[0])) {
                                Pack testRequest = new Pack(user, commands.getCommand("show_one"), argument);
                                sendPack(testRequest);
                                if (!isConnected) continue;
                                Pack testAnswer = readPack();
                                if (testAnswer != null && !"".equals(testAnswer.getAnswer())) {
                                    if (!testAnswer.getAnswer().contains("\n")) {
                                        System.out.println(testAnswer.getAnswer());
                                        continue;
                                    } else System.out.println("Найден такой Id у группы\n" + testAnswer.getAnswer());
                                } else System.out.println("Ошибка при получении пакета");
                            }
                            StudyGroup group = new StudyGroupAsk().getStudyGroup(this);
                            group.setLoginOwner(user.getUser().getLogin());
                            group.setIdOwner(user.getUser().getId());
                            request = new Pack(user, commands.getCommand(command[0]), argument, group);
                        } else request = new Pack(user, commands.getCommand(command[0]), argument);

                        sendPack(request);
                        if (!isConnected) continue;
                        Pack answerPack = readPack();
                        line = "line";
                        if (answerPack != null && !"".equals(answerPack.getAnswer())) {
                            while (answerPack != null && !"".equals(answerPack.getAnswer()) &&
                                    "ERROR: Повторение passportId админа".equals(answerPack.getAnswer())
                                    && !"".equals(line.trim()) && isConnected ) {
                                print(answerPack.getAnswer()+ "\n Введите другой passportId админа или пустую строку, если вам надоело\n>");
                                line = readLine();
                                if ("".equals(line.trim())) continue;
                                else request.getStudyGroup().getGroupAdmin().setPassportID(line);
                                sendPack(request);
                                if (!isConnected) continue;
                                answerPack = readPack();
                            }
                            printLn(answerPack.getAnswer());
                        }
                        else System.out.println("Ошибка при получении пакета");

                    }
                } else System.out.println("Error: Нет такой команды");

            }
        }

    }

    public Pack readPack() throws IOException, ClassNotFoundException {

        InputStream inputStream = clientSocket.getInputStream();
        return Serializer.deserialize(inputStream);
    }

    private Pack readScript(UserConnection user,String path){
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException | NullPointerException e) {
            System.out.println("Проблемы с файлом скрипта");
        }
        if (lines != null) {
            return new Pack(user, commands.getCommand("execute_script"), lines);
        } else return null;
    }
    public void sendPack(Pack pack) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        byte[] buf = null;
        try {
            buf = Serializer.serialize(pack);

        } catch (IOException e) {
            System.out.println("Ошибка при упаковке пакета");
            try {
                buf = Serializer.serialize(new Pack("Ошибка"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        outputStream.write(buf);
    }

}
