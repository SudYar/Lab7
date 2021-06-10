package sudyar.server;


import libriary.commands.Commands;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.internet.UserConnection;
import libriary.utilities.CommandsExecute;
import libriary.utilities.GetCommands;
import libriary.utilities.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientRunner extends Thread {
    private Socket socket;
    private Server server;
    private Commands commands;
    private int idConnection;
    private UserConnection userConnection;

    public ClientRunner(Socket socket, Server server, Commands commands, int idConnection, UserConnection userConnection) {
        this.socket = socket;
        this.server = server;
        this.commands = commands;
        this.idConnection = idConnection;
        this.userConnection = userConnection;
    }

    public void run(){

        Commands authCommands = GetCommands.getAuthCommands(StudyGroupCollection.getInstance());

        CommandsExecute commandsExecute = new CommandsExecute(commands);
        try {
            CommandsExecute authCommandsExecute = new CommandsExecute(authCommands);
            boolean logging = true;
            while (logging && userConnection.equals(server.getUserConnection(idConnection)) && server.isServerRun()) {
                Pack request = readPack();
                server.printInf("Полученный пакет от клиента " + idConnection + ":  \n" + request.toString());
                String authAnswer = authCommandsExecute.execute(request);
                Pack answerPack = new Pack(authAnswer);
                if (authAnswer.contains("успешно")) {
                    server.updateUserConnection(idConnection, StudyGroupCollection.getInstance().getUser(request.getUserConnection().getUser().getLogin()));
                    userConnection = server.getUserConnection(idConnection);
                    answerPack.setConnection(userConnection);
                    logging = false;
                }
                sendPack(answerPack);
            }
            while (userConnection.equals(server.getUserConnection(idConnection)) && server.isServerRun()) {
                try {
                    Pack request = readPack();
                    server.printInf("Полученный пакет от клиента " + idConnection + ":  \n" + request.toString());
                    String answer = commandsExecute.execute(request);
                    server.printInf("Результат команды: " + answer);
                    Pack newPack = new Pack(answer);
                    sendPack(newPack);
                }  catch (ClassNotFoundException e) {
                    sendPack(new Pack("Пакет сломался при передаче"));

                }
            }
        }catch (IOException e) {
            server.printInf("Отключился клиент с id: " + idConnection + "\nподключением: " + userConnection.toString());
            try {
                server.removeClient(idConnection);
                socket.close();
            } catch (IOException ioException) {
                server.printErr(ioException, "При попытке закрыть сокет: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            server.printErr(e, "Непредвиденная ошибка у клиента " + idConnection);
        }

    }

    private Pack readPack() throws IOException, ClassNotFoundException {
        InputStream inputStream = socket.getInputStream();
        return Serializer.deserialize(inputStream);
    }

    private void sendPack(Pack pack) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        byte[] buf = null;
        try {
            buf = Serializer.serialize(pack);

        } catch (IOException e) {
            e.printStackTrace();
            server.printErr(e,"Ошибка при упаковке пакета");
            try {
                buf = Serializer.serialize(new Pack("Ошибка"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        outputStream.write(buf);
    }
}
