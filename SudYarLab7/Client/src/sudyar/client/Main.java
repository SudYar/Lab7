package sudyar.client;



import libriary.commands.Commands;
import libriary.utilities.GetCommands;

import java.io.IOException;
import java.net.Socket;


public class Main {
    public static void main(String[] args) {



        Commands clientCommands = GetCommands.getClientCommands(null);
        try (Socket socket = new Socket("localhost", 31174)) {

            new Client(socket).run(clientCommands);

        } catch (IOException  ioe) {
            System.out.println("Сервер не работает");;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
