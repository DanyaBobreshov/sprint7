package managers;

import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    void printTasks();
    void deleteTasks();
    Task getTask(Long id);
    void makeTask(Task task);
    void updateTask(Task task);

    void deleteTask (Long id);
    ArrayList<SubTask> getSubtaskByEpicId(Long id);
    List<Task> getHistory();

    HashMap<Long, Task> getAllTasks();
    HashMap<Long, EpicTask> getAllEpicTasks();
    HashMap<Long, SubTask> getAllSubTasks();
    void deleteAllTasks();
    void deleteAllSubTasks();
    void deleteAllEpicTasks();
    Map<Long, Task> getTasksINmanager();
    List<Task> getPrioritized();
}
