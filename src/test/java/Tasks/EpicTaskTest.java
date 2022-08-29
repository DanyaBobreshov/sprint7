package Tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {

    private EpicTask epicTask;
    private SubTask subTask1;
    private SubTask subTask2;
    TaskManager taskManager= Managers.getDefault();

    @BeforeEach
    void addTask(){
        epicTask=new EpicTask("EName", "EDescription", 1L);
        subTask1=new SubTask("SName1", "SDescription", 2L, StatusOfTask.NEW);
        subTask2=new SubTask("SName2", "SDescription", 3L, StatusOfTask.NEW);
        taskManager.makeTask(epicTask);
        taskManager.makeTask(subTask1);
        taskManager.makeTask(subTask2);
        final Long id=epicTask.getId();
        final Task taskInManager = taskManager.getTask(id);
        assertNotNull(taskInManager, "Задача не сохранена");
        assertEquals(epicTask,taskInManager, "Задачи не совпадают");
        final Map<Long, EpicTask> allTasks=taskManager.getAllEpicTasks();
        assertNotNull(allTasks,"список задач не сфоормирован");
        assertEquals(1, allTasks.size(), "количество задач не соответсвует");
        assertEquals(epicTask, allTasks.get(epicTask.getId()), "задачи не совпадают");
    }

    @Test
    void getSubTasks() {
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        ArrayList<SubTask>subTasks=new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        assertEquals(subTasks, epicTask.getSubTasks(), "задачи не совпадают");
    }

    @Test
    void setSubTasks() {
        ArrayList<SubTask>subTasks=new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        epicTask.setSubTasks(subTasks);
        assertEquals(subTasks, epicTask.getSubTasks(), "задачи не совпадают");
    }

    @Test
    void addSubTask() {
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        ArrayList<SubTask>subTasks=new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        assertEquals(subTasks, epicTask.getSubTasks(), "задачи не совпадают");
    }

    @Test
    void controlStatus() {
        assertEquals(epicTask.getStatus(), StatusOfTask.NEW, "статус не совпадает");
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        epicTask.controlStatus();
        assertEquals(epicTask.getStatus(), StatusOfTask.NEW, "статус не совпадает");
        subTask1.setStatus(StatusOfTask.DONE);
        subTask2.setStatus(StatusOfTask.DONE);
        epicTask.controlStatus();
        assertEquals(epicTask.getStatus(), StatusOfTask.DONE, "статус не совпадает");
        subTask1.setStatus(StatusOfTask.NEW);
        epicTask.controlStatus();
        assertEquals(epicTask.getStatus(), StatusOfTask.IN_PROGRESS, "статус не совпадает");
        subTask1.setStatus(StatusOfTask.IN_PROGRESS);
        subTask2.setStatus(StatusOfTask.IN_PROGRESS);
        epicTask.controlStatus();
        assertEquals(epicTask.getStatus(), StatusOfTask.IN_PROGRESS, "статус не совпадает");
    }

    @Test
    void getSubTask() {
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        assertEquals(epicTask.getSubTask(subTask1.getId()), subTask1, "подзадачи не совпадают");
    }

    @Test
    void progressTask() {
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        epicTask.progressTask(subTask1.getId());
        epicTask.controlStatus();
        assertEquals(epicTask.getStatus(), StatusOfTask.IN_PROGRESS, "статус не совпадает");
    }

    @Test
    void doneTask() {
        epicTask.addSubTask(subTask1);
        subTask1.setStatus(StatusOfTask.DONE);
        epicTask.addSubTask(subTask2);
        epicTask.doneTask(subTask2.getId());
        assertEquals(epicTask.getStatus(), StatusOfTask.DONE, "статус не совпадает");
    }

    @Test
    void controlDelete(){
        taskManager.deleteAllEpicTasks();
        final Map<Long, EpicTask> allTasks=taskManager.getAllEpicTasks();
        assertNotNull(allTasks,"список задач не сфоормирован");
        assertEquals(0, allTasks.size(), "количество задач не соответсвует");
    }

    @Test
    void controlTime(){
        subTask1.setStartTime(LocalDateTime.parse("2022-08-29T15:00:00"));
        subTask1.setDuration(300L);
        subTask2.setStartTime(LocalDateTime.parse("2022-08-30T15:00:00"));
        subTask2.setDuration(60L);
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        assertEquals(LocalDateTime.parse("2022-08-29T15:00:00"), epicTask.getStartTime(), "время начала не совпадает");
        assertEquals(LocalDateTime.parse("2022-08-30T15:00:00").plusMinutes(60), epicTask.getEndTime(), "время окончания не совпадает");
        assertEquals(360L, epicTask.getDuration(), "время выполнения не совпадает");


    }

}