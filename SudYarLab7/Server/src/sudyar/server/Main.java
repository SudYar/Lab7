package sudyar.server;

import libriary.data.StudyGroupCollection;
import libriary.utilities.GetCommands;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        String log = "SudYar7log.txt";
        File logFile = new File(log);
        if (!logFile.isFile()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Проблемы с лог файлом, дайте доступный SudYar6log.txt");
            }
        }

        Server server = Server.getInstance();
        server.setLog(log);


        StudyGroupCollection collection = StudyGroupCollection.getInstance();
        server.run(31174, GetCommands.getServerCommands(), 30);

    }
}
