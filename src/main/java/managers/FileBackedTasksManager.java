package managers;

import Tasks.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTasksManager extends InMemoryTaskManager {


    private final File fileForSave;
    private static final String FILE_HEADER="id,type,name,status,description,epic";

    public FileBackedTasksManager(File fileForSave) {
        this.fileForSave = fileForSave;
    }

    public static FileBackedTasksManager loadFromFile(File file){
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    fileBackedTasksManager.historyFromString(reader.readLine());
                    break;
                }
                fileBackedTasksManager.processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    private void historyFromString(String readLine) {
        if (readLine==null||readLine.isEmpty()){
            return;
        }
        String[] taskStringIds=readLine.split(",");
        for (String taskStringId : taskStringIds) {
            long taskId = Long.parseLong(taskStringId);
            if (taskId < tasksINmanager.size()) {
                historyManager.add(tasksINmanager.get(taskId));
            }
        }

    }

    private void processLine(String taskString) {
        String taskStringWithoutSpace=taskString.replaceAll("\\s+","");
        String[] taskStringParts = taskStringWithoutSpace.split(",");
        if(taskStringParts.length<7){
            System.out.println("don't format arrays length");
            return;
        }
        long id= Long.parseLong(taskStringParts[0]);
        if (id>=idGenerator){
            idGenerator=id+1;
        }
        if (Type.valueOf(taskStringParts[1]).equals(Type.SUBTASK)) {
            SubTask task = new SubTask(taskStringParts[2], taskStringParts[4], id,
                    Tasks.StatusOfTask.valueOf(taskStringParts[3]));
            task.setStartTime(LocalDateTime.parse(taskStringParts[5]));
            task.setDuration(Long.parseLong(taskStringParts[7]));
            if (!validationDate(task)){
                System.out.println("Приведет к пересечению задач");
                return;
            }
            tasksINmanager.put(task.getId(),task);
            prioritizedTasks.add(task);
            if(taskStringParts[8]==null){
                System.out.println("epic for subtask with id"+task.getId()+"dont correct");
                return;
            }
            EpicTask epicTask= (EpicTask) tasksINmanager.get(Long.valueOf(taskStringParts[8]));
            epicTask.getSubTasks().add(task);
            task.setEpicTaskId(epicTask.getId());
            epicTask.controlStatus();
            return;
        }
        if (Type.valueOf(taskStringParts[1]).equals(Type.EPIC)) {
            Task task = new EpicTask(taskStringParts[2], taskStringParts[4], id);
            task.setStartTime(LocalDateTime.parse(taskStringParts[5]));
            task.setDuration(Long.parseLong(taskStringParts[7]));
            tasksINmanager.put(task.getId(), task);
            prioritizedTasks.add(task);
            return;
        }
        if(Type.valueOf(taskStringParts[1]).equals(Type.TASK)){
            Task task = new Task(taskStringParts[2], taskStringParts[4], id,
                    Tasks.StatusOfTask.valueOf(taskStringParts[3]));
            task.setStartTime(LocalDateTime.parse(taskStringParts[5]));
            task.setDuration(Long.parseLong(taskStringParts[7]));
            if (!validationDate(task)){
                System.out.println("Приведет к пересечению задач");
                return;
            }
            tasksINmanager.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }
    private void save() {
        try(FileWriter writer= new FileWriter(fileForSave)) {
            writer.write(FILE_HEADER+"\n");
            for (Task task : getAllTasks().values()) {
                writer.write(task.toString() + "\n");
            }

            for (EpicTask task : getAllEpicTasks().values()) {
                task.controlStatus();
                writer.write(task + "\n");
            }

            for (Task task : getAllSubTasks().values()) {
                writer.write(task + "\n");
            }

            writer.write("\n");
            int size = historyManager.getHistory().size();
            if (size > 0) {
                for (int i = 0; i < size - 1; i++) {
                    writer.write(historyManager.getHistory().get(i).getId() + ",");
                }
                writer.write(historyManager.getHistory().get(size - 1).getId() + "");
            }

        } catch (IOException e) {
           throw  new ManagerSaveException("При чтении файла "+fileForSave.getName()+" произошла ошибка");
        }


    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void makeTask(Task task) {
        super.makeTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(Long id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public Task getTask(Long id) {
        Task task= super.getTask(id);
        save();
        return task;
    }

    public static void main(String[] args) throws IOException {
        File file=new File("tasks");
        FileBackedTasksManager fileManager4=Managers.getFileManager(file);
        EpicTask epicTask= new EpicTask("epicTask", "epicText", 1L);
        fileManager4.makeTask(epicTask);
        fileManager4=FileBackedTasksManager.loadFromFile(file);
        System.out.println("control sorted");
        SubTask subTask4 = new SubTask("name4", "text4", 1L, StatusOfTask.NEW);
        SubTask subTask5 = new SubTask("name5", "text5", 1L, StatusOfTask.NEW);
        subTask4.setStartTime(LocalDateTime.now().plusDays(1));
        subTask5.setStartTime(LocalDateTime.now().plusHours(5));
        subTask4.setDuration(400L);
        subTask5.setDuration(400L);
        subTask4.setEpicTaskId(epicTask.getId());
        subTask5.setEpicTaskId(epicTask.getId());
        fileManager4.makeTask(subTask4);
        fileManager4.makeTask(subTask5);
        System.out.println(fileManager4.getPrioritized());
    }

}
