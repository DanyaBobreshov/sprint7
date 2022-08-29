package managers;

import Tasks.EpicTask;
import Tasks.StatusOfTask;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    private File file;

    @BeforeEach
    void setUp() {
    file=new File("tasks.csv");
    taskManager=new FileBackedTasksManager(file);
    setUpInAbstractClass();
    }
    @Disabled
    @Test
    void setClear(){
        taskManager=new FileBackedTasksManager(file);
        assertEquals(0, taskManager.getAllTasks().size(), "список пуст");
        assertEquals(0, taskManager.getHistory().size(), "список пуст");
    }

    void setEpic(){
        taskManager=new FileBackedTasksManager(file);

    }

    @AfterEach
    void tearDown(){
        assertTrue(file.delete());
    }

    @Test
    void loadFromFile() {
    }

    @Disabled
    @Test
    void save(){

    }

    @Test
    void controlTimeAndValidation()  {
        taskManager=Managers.getFileManager(file);
        epicTask=new EpicTask("EName", "EDescription", 2L);
        SubTask subTask1 = new SubTask("name1", "text1", 1L, StatusOfTask.NEW);
        SubTask subTask2 = new SubTask("name2", "text2", 1L, StatusOfTask.NEW);
        subTask1.setStartTime(LocalDateTime.parse("2022-08-29T15:00:00"));
        subTask1.setDuration(300L);
        subTask2.setStartTime(LocalDateTime.parse("2022-08-30T00:00:00"));
        subTask2.setDuration(300L);
        SubTask subTask3 = new SubTask("name3", "text3", 1L, StatusOfTask.NEW);
        subTask3.setStartTime(LocalDateTime.parse("2022-08-29T17:00:00"));
        subTask3.setDuration(10L);
        task=new Task("TName", "TDescription", 1L, StatusOfTask.NEW);
        task.setStartTime(LocalDateTime.parse("2022-08-29T23:00:00"));
        task.setDuration(600L);
        Task task2=new Task("TName2", "TDescription2", 1L, StatusOfTask.NEW);
        task2.setStartTime(LocalDateTime.parse("2022-08-28T23:00:00"));
        task2.setDuration(6000L);
        taskManager.makeTask(epicTask);
        taskManager.makeTask(subTask1);
        taskManager.makeTask(subTask2);
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        assertEquals(LocalDateTime.parse("2022-08-29T15:00:00"), epicTask.getStartTime(), "время начала не совпадает");
        assertEquals(LocalDateTime.parse("2022-08-30T05:00:00"), epicTask.getEndTime(), "время окончания не совпадает");
        assertEquals(600L, epicTask.getDuration(), "время выполнения не совпадает");
        if (taskManager.validationDate(subTask3)) {
            epicTask.addSubTask(subTask3);
        }
        assertEquals(2, epicTask.getSubTasks().size(), "количество подзадач не совпадает");
        taskManager.makeTask(task);
        taskManager.makeTask(task2);
        assertEquals(0, taskManager.getAllTasks().size(),"Добавлены лишние задачи");

    }
}