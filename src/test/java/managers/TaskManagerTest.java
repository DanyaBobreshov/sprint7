package managers;

import Tasks.EpicTask;
import Tasks.StatusOfTask;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected EpicTask epicTask;
    protected SubTask subTask;

  //  @BeforeEach
    protected void setUpInAbstractClass() {
        task=new Task("TName", "TDescription", 1L, StatusOfTask.NEW);
        epicTask=new EpicTask("EName", "EDescription", 2L);
        subTask=new SubTask("SName", "SDescription", 3L, StatusOfTask.NEW);
        subTask.setEpicTaskId(epicTask.getId());
        taskManager.makeTask(task);
        taskManager.makeTask(epicTask);
        taskManager.makeTask(subTask);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void printTasks() {
        System.out.println("print task:_");
        taskManager.printTasks();
    }

    @Test
    void deleteTasks() {
        taskManager.deleteTasks();
        Map<Long,Task> tasks=taskManager.getTasksINmanager();
        assertEquals(0, tasks.size(), "список не пуст");
    }

    @Test
    void getTask() {
       Task taskInManager= taskManager.getTask(task.getId());
        assertEquals(task, taskInManager, "задачи не совпадают");
    }

    @Test
    void makeTask() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        taskManager.makeTask(task);
        Long id=task.getId();
        final Task taskInManager = taskManager.getTask(id);
        assertNotNull(taskInManager, "Задача не сохранена");
        assertEquals(task,taskInManager, "Задачи не совпадают");
        final Map<Long, Task> allTasks=taskManager.getAllTasks();
        assertNotNull(allTasks,"список задач не сфоормирован");
        assertEquals(2, allTasks.size(), "количество задач не соответсвует");
        assertEquals(task, allTasks.get(task.getId()), "задачи не совпадают");
    }

    @Test
    void updateTask() {
       SubTask subTask1=new SubTask("SNameNew", "SDescriptionNew", subTask.getId(), StatusOfTask.NEW);
       taskManager.updateTask(subTask1);
       SubTask subTaskFromManager= (SubTask) taskManager.getTask(subTask1.getId());
        assertEquals(subTask1, subTaskFromManager, "задачи не совпадают");
    }

    @Test
    void deleteTask() {
        taskManager.deleteTask(task.getId());
        int size=taskManager.getTasksINmanager().size();
        assertEquals(2, size, "количество не совпадает");
    }

    @Test
    void getSubtaskByEpicId() {
        ArrayList<SubTask> subTaskFromManager=taskManager.getSubtaskByEpicId(epicTask.getId());
        ArrayList<SubTask> subTasks=new ArrayList<>();
        subTasks.add(subTask);
        assertEquals(subTaskFromManager, subTasks, "списки не совпадают");

    }

    @Test
    void getHistory() {
        System.out.println("history: "+taskManager.getHistory());
    }

    @Test
    void getAllTasks() {
        final Map<Long, Task> allTasks=taskManager.getAllTasks();
        assertNotNull(allTasks,"список задач не сфоормирован");
        assertEquals(1, allTasks.size(), "количество задач не соответсвует");
        assertEquals(task, allTasks.get(task.getId()), "задачи не совпадают");
    }

    @Test
    void getAllEpicTasks() {
        final Map<Long, EpicTask> allTasks=taskManager.getAllEpicTasks();
        assertNotNull(allTasks,"список задач не сфоормирован");
        assertEquals(1, allTasks.size(), "количество задач не соответсвует");
        assertEquals(epicTask, allTasks.get(epicTask.getId()), "задачи не совпадают");
    }

    @Test
    void getAllSubTasks() {
        final Map<Long, SubTask> allTasks=taskManager.getAllSubTasks();
        assertNotNull(allTasks,"список задач не сфоормирован");
        assertEquals(1, allTasks.size(), "количество задач не соответсвует");
        assertEquals(subTask, allTasks.get(subTask.getId()), "задачи не совпадают");
    }

    @Test
    void deleteAllTasks() {
        taskManager.deleteAllTasks();
        assertEquals(2, taskManager.getTasksINmanager().size(), "количество задач не соответсвует");
    }

    @Test
    void deleteAllSubTasks() {
        taskManager.deleteAllSubTasks();
        assertEquals(2, taskManager.getTasksINmanager().size(), "количество задач не соответсвует");
    }

    @Test
    void deleteAllEpicTasks() {
        taskManager.deleteAllEpicTasks();
        assertEquals(2, taskManager.getTasksINmanager().size(), "количество задач не соответсвует");
    }
}