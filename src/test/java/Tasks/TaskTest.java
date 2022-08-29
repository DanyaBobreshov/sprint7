package Tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private final TaskManager taskManager=Managers.getDefault();
    private Task task;

    @Test
    void addTask(){
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        taskManager.makeTask(task);
        final Long id=task.getId();
        final Task taskInManager = taskManager.getTask(id);
        assertNotNull(taskInManager, "Задача не сохранена");
        assertEquals(task,taskInManager, "Задачи не совпадают");
        final Map<Long, Task> allTasks=taskManager.getAllTasks();
        assertNotNull(allTasks,"список задач не сфоормирован");
        assertEquals(1, allTasks.size(), "количество задач не соответсвует");
        assertEquals(task, allTasks.get(task.getId()), "задачи не совпадают");
    }

    @Test
    void setName() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        task.setName("new name");
        assertEquals("new name", task.getName(), "имя не поменялось");
    }

    @Test
    void getName() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        String taskName=task.getName();
        assertEquals("TForAdd", taskName, "имя не совпадает");
    }

    @Test
    void testToString() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        String taskString=task.toString();
        assertEquals(task.getId()+","+task.getType()+","+task.getName()+","+task.getStatus()+","+task.getText()+",", taskString, "строка не совпадает");
    }

    @Test
    void getText() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        assertEquals("TForAddDescription", task.getText(), "текст не совпадает");
    }

    @Test
    void setText() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        task.setText("newTForAddDescription");
        assertEquals("newTForAddDescription", task.getText(), "текст не совпадает");
    }

    @Test
    void getId() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        assertEquals(1L, task.getId(), "ID не совпадает");
    }

    @Test
    void setId() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        task.setId(2L);
        assertEquals(2L, task.getId(), "ID не совпадает");
    }

    @Test
    void getStatus() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        assertEquals(StatusOfTask.NEW, task.getStatus(), "статус не совпадает");
    }

    @Test
    void setStatus() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        task.setStatus(StatusOfTask.DONE);
        assertEquals(StatusOfTask.DONE, task.getStatus(), "статус не совпадает");
    }

    @Test
    void getType() {
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        assertEquals(Type.TASK, task.getType(), "тип не совпадает");
    }

    @Test
    void setTime(){
        task=new Task("TForAdd", "TForAddDescription", 1L, StatusOfTask.NEW);
        task.setStartTime(LocalDateTime.parse("2022-08-29T14:55:20"));
        task.setDuration(2400L);
        assertEquals(LocalDateTime.parse("2022-08-29T14:55:20"), task.getStartTime(), "время начала не совпадает");
        assertEquals(2400L, task.getDuration(), "время окончания не совпадает");
        assertEquals(LocalDateTime.parse("2022-08-29T14:55:20").plusMinutes(2400), task.getEndTime(), "время окончания не совпадает");
    }
}