package sudyar.client;



import libriary.commands.Commands;
import libriary.utilities.GetCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Main {
    private static BufferedReader  scanner =new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) {



        Commands clientCommands = GetCommands.getClientCommands(null);
        String line;
        do {
            try (Socket socket = new Socket("localhost", 31174)) {

                new Client(socket).run(clientCommands);

            } catch (IOException ioe) {
                System.out.println("Сервер не работает");
                ;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Введите непустую строку, если хотите переподключиться, либо пустую, чтобы завершить программу");
            line = readLine();
        }while (!"".equals( line));
        System.out.println("Завершение программы");
    }

    public static String readLine(){
        String line;
        try {
            line = scanner.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка ввода, пожалуйста не вводите это снова");
            return "";
        }
        if (line == null) {
            System.out.println("ВВедено Ctrl + D\nЗавершаем работу");
            System.exit(1);
        }
        return line;
    }
}
