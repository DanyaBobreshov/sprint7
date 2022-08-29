package managers.History;

import Tasks.EpicTask;
import Tasks.StatusOfTask;
import Tasks.SubTask;
import Tasks.Task;
import managers.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;
    private Task task;
    private EpicTask epicTask;
    private SubTask subTask;

    @BeforeEach
    void setUp() {
        historyManager=new InMemoryHistoryManager();
        task=new Task("TName", "TDescription", 1L, StatusOfTask.NEW);
        epicTask=new EpicTask("EName", "EDescription", 2L);
        subTask=new SubTask("SName", "SDescription", 3L, StatusOfTask.NEW);
        subTask.setEpicTaskId(epicTask.getId());
    }


    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(1, history.size(), "элемент не один");
    }

    @Test
    void addDouble(){
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(1, history.size(), "элемент не один");
    }

    @Test
    void removeFirst(){
        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(3, history.size(), "элементов не три");

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(2, history.size(), "элементов не два");
        assertEquals(epicTask, history.get(0),"не соблюден порядок");
        assertEquals(subTask, history.get(1),"не соблюден порядок");

    }

    @Test
    void removeMiddle(){
        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(3, history.size(), "элементов не три");

        historyManager.remove(epicTask.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(2, history.size(), "элементов не два");
        assertEquals(task, history.get(0),"не соблюден порядок");
        assertEquals(subTask, history.get(1),"не соблюден порядок");
    }

    @Test
    void removeEnd(){
        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(3, history.size(), "элементов не три");

        historyManager.remove(subTask.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(2, history.size(), "элементов не два");
        assertEquals(task, history.get(0),"не соблюден порядок");
        assertEquals(epicTask, history.get(1),"не соблюден порядок");
    }

    @Test
    void removeOne(){
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertEquals(1, history.size(), "элементов не три");

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertTrue(history.isEmpty(),"история не пустая");
    }

    @Test
    void remove() {
    }

    @Test
    void getHistory() {
        List<Task>history = historyManager.getHistory();
        assertNotNull(history, "не создан список");
        assertTrue(history.isEmpty(),"история не пустая");
    }


}