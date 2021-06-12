package sudyar.server;


import libriary.commands.Commands;
import libriary.data.StudyGroupCollection;
import libriary.exception.DBExceprion;
import libriary.internet.DataBase;
import libriary.internet.Pack;
import libriary.internet.User;
import libriary.internet.UserConnection;
import libriary.utilities.GetCommands;
import libriary.utilities.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

/**
 * Класс Server по шаблону Singleton
 * за колличество подключений отвечает константа MAX_CONNECTION
 */

public class Server {
    private static volatile Server instance;
    private ExecutorService executorService = Executors.newCachedThreadPool();

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

    public void run(int port, Commands serverCommands, int maxConnection) {
        for (int i = 1; i <= maxConnection + 1 ; i++){
            freeId.add(i);
        }
        DataBase dataBase = null;
        println("Введите пароль от пользователя s311742, чтобы войти в базу данных");
        while (dataBase == null) {
            try {
                print(">");
                dataBase = new DataBase("s311742", readLine());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (SQLException throwables) {
                printErr(throwables, "Ошибка, введён неверный пароль для пользователя s311742, либо база данных не доступна");
            }
        }
        try {
            printInf(dataBase.selectAll(studyGroupCollection));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (DBExceprion dbExceprion) {
            printErr(dbExceprion, "Непредвиденная ошибка при получении данных с базы данных: ");
        }
        getServerConsole(serverCommands).start();
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            Commands clientCommands = GetCommands.getClientCommands(studyGroupCollection, dataBase) ;
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
                        UserConnection clientConnection = new UserConnection(clientSocket, idConnection);
                        freeId.remove(idConnection);
                        clientCollection.put(idConnection, clientConnection);
                        sendPack(answer, idConnection);
                        printInf("Ему выдан свободный id: " + idConnection);
                        executorService.submit( new ClientRunner(clientSocket, this, clientCommands, idConnection, clientConnection, dataBase));
                    }
                    else {
                        answer = new Pack("Сервер переполнен, подключайтесь позже");
                        printInf("Сервер переполнен, отключаем клиента");
                        int idConnection = freeId.iterator().next();
                        UserConnection clientConnection = new UserConnection(clientSocket, idConnection);
                        clientCollection.put(idConnection, clientConnection);
                        sendPack(answer, idConnection);
                        clientSocket.close();
                        removeClient(idConnection);
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

    /**
     * Добавление в подключение залогиневшегося юзера
     * @param id - id-шник подключения
     * @param user
     */
    public void updateUserConnection (int id, User user){
        clientCollection.get(id).setUser(user);
    }

    public UserConnection getUserConnection(int id) {
        return clientCollection.get(id);
    }

    public void stopServer (){
        serverRun = false;
    }

    public boolean isServerRun (){return serverRun;}

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
                    printInf("Завершение работы сервера");
                    printInf(commands.getCommand("exit").execute(null));
                }
                else {
                    String[] splitLine = line.trim().split(" ");
                    if (splitLine.length > 0 && commands.getCommands().containsKey(splitLine[0])){
                         String argument = (splitLine.length > 1 ? splitLine[1]:"");
                            if ("VALID".equals(commands.getCommand(splitLine[0]).isValidArgument( argument)))
                                switch (splitLine[0]){
                                    case ("exit"):
                                        printInf("Завершение работы сервера");
                                        printInf(commands.getCommand("exit").execute(null));
                                        break;
                                    case ("disconnect"):
                                        int id = Integer.parseInt(argument);
                                        if (clientCollection.containsKey(id)) {
                                            try {
                                                clientCollection.get(id).getSocket().close();
                                            } catch (IOException e) {
                                                printErr(e,"При попытке закрыть сокет (вероятно уже закрыт)");
                                            }
                                            removeClient(id);
                                            println("Клиент с этим id отключен");
                                        } else System.out.println("Такого id нет, введите show_con, чтобы узнать, кто подключен");
                                        break;
                                    case ("show_con"):
                                        String answer = "";
                                        for (int i : clientCollection.keySet()){
                                            answer+= "Id: " + i + clientCollection.get(i).toString() + "\n";
                                        }
                                        System.out.println("".equals(answer) ? "Никто не подключился" : answer.trim() );
                                        break;
                                    default:
                                        println(commands.getCommand(splitLine[0]).execute(null));
                                        break;
                                }
                            else println(commands.getCommand(splitLine[0]).isValidArgument( argument));

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
        System.out.println("ERROR: " + line + (e.getMessage() == null ? "": ": " + e.getMessage()));
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
