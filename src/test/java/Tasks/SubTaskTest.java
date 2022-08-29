package Tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    SubTask subTask1;
    EpicTask epicTask;
    TaskManager taskManager;

    @BeforeEach
    void setUp() {
        subTask1=new SubTask("SNameNew1", "SDescriptionNew1", 1L, StatusOfTask.NEW);
        epicTask=new EpicTask("EName", "EDescription", 2L);
        assertEquals(Type.SUBTASK, subTask1.getType(), "тип не совпадает");
    }

    @Test
    void setAnfGetEpic(){
        taskManager= Managers.getDefault();
        taskManager.makeTask(epicTask);
        subTask1.setEpicTaskId(epicTask.getId());
        taskManager.makeTask(subTask1);
        assertEquals(epicTask.getId(), subTask1.getEpicTaskId(), "epic не совпадает");
    }
}