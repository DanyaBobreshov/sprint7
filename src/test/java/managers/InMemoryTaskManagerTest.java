package managers;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void setUp() {
        taskManager=new InMemoryTaskManager();
        setUpInAbstractClass();
    }

}