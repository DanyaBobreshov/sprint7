package managers;

import managers.History.HistoryManager;
import managers.History.InMemoryHistoryManager;

import java.io.File;
import java.io.IOException;

public class Managers {

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static FileBackedTasksManager getFileManager(File file) {
        return new FileBackedTasksManager(file);
    }
    public static HistoryManager getHistoryManager(){
        return new InMemoryHistoryManager();
    }


    public static void loadFromFile(File file) throws IOException {
        FileBackedTasksManager.loadFromFile(file);
    }
}
