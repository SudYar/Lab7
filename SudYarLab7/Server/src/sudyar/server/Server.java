package sudyar.server;


import libriary.commands.Commands;
import libriary.data.StudyGroupCollection;
import libriary.internet.Pack;
import libriary.internet.UserConnection;
import libriary.utilities.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.*;

/**
 * Класс Server по шаблону Singleton
 * за колличество подключений отвечает константа MAX_CONNECTION
 */

public class Server {
    private static volatile Server instance;

    StudyGroupCollection studyGroupCollection;
    static final Logger logger = Logger.getLogger(Server.class.getName());

    private boolean serverRun = true;
    private final HashMap<Integer, UserConnection> clientCollection = new HashMap<>();
    HashSet<Integer> freeId = new HashSet<>();
    private static boolean isHaveLog = false;


    private Server() {
        this.studyGroupCollection = StudyGroupCollection.getInstance();
    }

    public static Server getInstance() {
        Server localInstance = instance;
        if (localInstance == null) {
            synchronized (Server.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Server();
                }
            }
        }
        return localInstance;
    }

    public void setLog(String logPath){
        try {
            FileHandler handler = new FileHandler(logPath, true);
            logger.setUseParentHandlers(false);
            handler.setFormatter(new MyFormatter());
            logger.addHandler(handler);
            isHaveLog = true;
        }catch (Exception e){
            println("Работаем без логирования");
            isHaveLog = false;
        }
    }

    public void run(int port,Commands clientCommands, Commands serverCommands, int maxConnection) {
        for (int i = 1; i <= maxConnection + 1 ; i++){
            freeId.add(i);
        }
        getServerConsole(serverCommands).start();
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            printInf("Сервер запущен");
            Socket clientSocket;
            while (serverRun){
                try {
                    clientSocket = serverSocket.accept();

                    printInf("Подключился клиент");
                    Pack answer;
                    if (!freeId.isEmpty()){
                        answer = new Pack("Подключение удалось");
                        int idConnection = freeId.iterator().next();
                        UserConnection clientConnection = new UserConnection(clientSocket);
                        clientCollection.put(idConnection, clientConnection);
                        sendPack(answer, idConnection);
                        ClientRunner newClient = new ClientRunner(clientSocket, this, clientCommands, idConnection, clientConnection);
                        newClient.start();
                    }
                    else {
                        answer = new Pack("Сервер переполнен, подключайтесь позже");
                        printInf("Сервер переполнен, отключаем клиента");
                        int idConnection = freeId.iterator().next();
                        UserConnection clientConnection = new UserConnection(clientSocket);
                        clientCollection.put(idConnection, clientConnection);
                        sendPack(answer, idConnection);
                        clientSocket.close();
                    }

                }catch (Exception e){
                    printErr(e, "Возникла непредвиденная ошибка, сохраните log");
                }

            }

            serverCommands.getCommand("save");

        }catch (IOException e){
            printErr(e,"Ошибка включения сервера");
        }

    }

    public void removeClient(int id){
        freeId.add(id);
        clientCollection.remove(id);
    }

    public UserConnection getClientConnection(int id) {
        return clientCollection.get(id);
    }

    public void stopServer (){
        serverRun = false;
    }

    public boolean isServerRun (){return serverRun;};

    private BufferedReader scanner =new BufferedReader(new InputStreamReader(System.in));

    private String readLine(){
        String line;
        try {
            line = scanner.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка ввода, пожалуйста не вводите это снова");
            return null;
        }
        return line;
    }

    private Thread getServerConsole(Commands commands){
        return new Thread(() -> {


            println("Доступна консоль, можете ввести help, чтобы получить список доступных команд");

            while (true) {
                String line = readLine();
                if (line == null) {
                    printInf(commands.getCommand("save").execute(null));
                    printInf("Завершение работы сервера");
                    printInf(commands.getCommand("exit").execute(null));
                }
                else {
                    line = line.trim();
                    if (commands.getCommands().containsKey(line)){
                        if ("exit".equals(line)) {
                            printInf(commands.getCommand("save").execute(null));
                            printInf("Завершение работы сервера");
                            printInf(commands.getCommand("exit").execute(null));
                        }
                        else println(commands.getCommand(line).execute(null));
                    }else println("Такой команды нет, введите help");

                }
            }

        });

    }
    private void sendPack(Pack pack, int id) {
        Socket socket = clientCollection.get(id).getSocket();
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] buf = null;
            try {
                buf = Serializer.serialize(pack);

            } catch (IOException e) {
                System.out.println("Ошибка при упаковке пакета");
                try {
                    buf = Serializer.serialize(new Pack("Ошибка"));
                } catch (IOException ioException) {
                    printErr(ioException, "непредвиденная ошибка");
                }
            }
            outputStream.write(buf);
        } catch (IOException e) {
            printInf("Не получилось отправить данные клиенту, отключаем его");
            removeClient(id);
        }

    }

    public void printInf(String line){
        System.out.println(line + "\n");
        if (isHaveLog) logger.info(line);
    }
    public void println(String line){
        System.out.println(line);
    }
    public void print(String line){
        System.out.print(line);
    }

    public void printErr(Exception e, String line){
        System.out.println("ERROR: " + line + e.getMessage());
        if (isHaveLog) logger.log(Level.WARNING, line , e);
    }




    private static class MyFormatter extends Formatter {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd ' время:' hh:mm:ss a zzz");

        @Override
        public String format(LogRecord record){
            return formatForDateNow.format(dateNow) + "\n" + record.getLevel() + ": " + record.getMessage() + "\n\n";
        }
    }
}
