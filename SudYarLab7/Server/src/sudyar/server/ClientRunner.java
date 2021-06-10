package sudyar.server;


import libriary.commands.Commands;
import libriary.internet.Pack;
import libriary.internet.UserConnection;
import libriary.utilities.CommandsExecute;
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
        CommandsExecute commandsExecute = new CommandsExecute(commands);
        try {
            while (userConnection.equals(server.getClientConnection(idConnection)) && server.isServerRun()) {
                try {
                    Pack request = readPack();
                    server.printInf("Полученный пакет: \n" + request.toString());
                    String answer = commandsExecute.execute(request);
                    server.printInf("Результат команды: " + answer);
                    Pack newPack = new Pack(answer);
                    sendPack(newPack);
                }  catch (ClassNotFoundException e) {
                    sendPack(new Pack("Пакет сломался при передаче"));

                }
            }
        }catch (IOException e) {
            if ("UserConnection reset".equals(e.getMessage())) server.printInf("Отключился клиент с id: " + idConnection + "\nподключением: " + userConnection.toString());
            else server.printErr(e, "");
            try {
                server.removeClient(idConnection);
                socket.close();
            } catch (IOException ioException) {
                server.printErr(ioException, "При попытке закрыть сокет: ");
            }
        } catch (Exception e) {
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
