package managers;

import Tasks.Type;
import managers.History.HistoryManager;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    protected final Map<Long, Task> tasksINmanager = new HashMap<>();
    protected Long idGenerator = 0L;
    protected HistoryManager  historyManager = Managers.getHistoryManager();
    protected TreeSet<Task> prioritizedTasks=new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public void printTasks()  {
        System.out.println(tasksINmanager.values());
    }

    @Override
    public Map<Long, Task> getTasksINmanager() {
        return tasksINmanager;
    }

    public List<Task> getReservedPrioritized() {
        List<Task>tasks= new ArrayList<>();
        Iterator<Map.Entry<Long, Task>> iterator = tasksINmanager.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Task> entry = iterator.next();
            tasks.add(entry.getValue());
        }
        //tasks.sort(Task::compareTo);

        ////////////////////////////////////////////////

        return tasks;
    }
    @Override
    public List<Task> getPrioritized() {
        return new ArrayList<>(prioritizedTasks);
    }


    @Override
    public void deleteTasks(){
        tasksINmanager.clear();
        idGenerator =0L;
        //мы же тут полностью очищаем массив задач
    }

    @Override
    public Task getTask(Long id){
        historyManager.add(tasksINmanager.get(id));
        return tasksINmanager.get(id);
    }

    @Override
    public void makeTask(Task task){
        if (!validationDate(task)){
            System.out.println("Приведет к пересечению задач");
            return;
        }
        idGenerator++;
        task.setId(idGenerator);
        tasksINmanager.put(idGenerator,task);
        prioritizedTasks.add(task);
        if(task.getType().equals(Type.EPIC)){
            EpicTask epicTask= (EpicTask) task;
            for (SubTask subTask:epicTask.getSubTasks()){
                subTask.setEpicTaskId(epicTask.getId());
                epicTask.controlStatus();
            }
        }
        if(task.getType().equals(Type.SUBTASK)){
            SubTask subTask= (SubTask)task;
            EpicTask epicTask= (EpicTask) tasksINmanager.get(subTask.getEpicTaskId());
            if(epicTask!=null){
                epicTask.getSubTasks().add(subTask);
                epicTask.setDuration(subTask);
                epicTask.controlStatus();
            }
        }

    }

    @Override
    public void updateTask(Task task){
        //historyManager.add(task);
        if (!validationDate(task)){
            System.out.println("Приведет к пересечению задач");
            return;
        }
        if(task.getType().equals(Type.SUBTASK)){
           SubTask subTask= (SubTask)task;
           EpicTask epicTask= (EpicTask) tasksINmanager.get(subTask.getEpicTaskId());
           if(epicTask!=null){
               epicTask.getSubTasks().remove(tasksINmanager.get(subTask.getId()));
               epicTask.getSubTasks().add(subTask);
               epicTask.setDuration(subTask);
               epicTask.controlStatus();
           }
        }
        tasksINmanager.put(task.getId(),task);
        prioritizedTasks.remove(tasksINmanager.get(task.getId()));
        prioritizedTasks.add(task);
    }

    public boolean validationDate(Task task){
        for (Task taskInManager:prioritizedTasks){
            if (taskInManager.getStartTime().isBefore(task.getStartTime())&&
                    taskInManager.getEndTime().isAfter(task.getStartTime())){
                return false;
            }
            if (taskInManager.getStartTime().isBefore(task.getEndTime())&&
                    taskInManager.getEndTime().isAfter(task.getEndTime())){
                return false;
            }
            if (task.getStartTime().isBefore(taskInManager.getStartTime())&&
                    task.getEndTime().isAfter(taskInManager.getEndTime())){
                return false;
            }

        }
        return true;

    }

    @Override
    public void deleteTask (Long id){
        if (tasksINmanager.get(id).getType().equals(Type.EPIC)){
            EpicTask epicTask= (EpicTask) tasksINmanager.get(id);
            for(SubTask subTask:epicTask.getSubTasks()){
                deleteTask(subTask.getId());
            }

        }
        else if(tasksINmanager.get(id).getType().equals(Type.SUBTASK)){
            SubTask subTask=(SubTask) tasksINmanager.get(id);
            EpicTask epicTask=(EpicTask) tasksINmanager.get(subTask.getEpicTaskId());
            if(epicTask!=null){
                epicTask.getSubTasks().remove(subTask);
                epicTask.controlStatus();
            }
        }
        prioritizedTasks.remove(tasksINmanager.get(id));
        historyManager.remove(id);
        tasksINmanager.remove(id);

    }

    @Override
    public ArrayList<SubTask> getSubtaskByEpicId(Long id){
        Task ts=tasksINmanager.get(id);
        if(ts.getType().equals(Type.EPIC)){
            return ((EpicTask) ts).getSubTasks();
        }
        return null;
    }

    @Override
    public HashMap<Long, Task> getAllTasks(){
        HashMap<Long, Task> allTasks=new HashMap<>();
        for(Task task:tasksINmanager.values()){
            if (task.getType().equals(Type.TASK)){
                allTasks.put(task.getId(),task);
            }
        }
        return allTasks;
    }

    @Override
    public HashMap<Long, EpicTask> getAllEpicTasks(){
        HashMap<Long, EpicTask> allEpicTasks=new HashMap<>();
        for(Task task:tasksINmanager.values()){
            if (task.getType().equals(Type.EPIC)){
                allEpicTasks.put(task.getId(), (EpicTask) task);
            }
        }
        return allEpicTasks;
    }

    @Override
    public HashMap<Long, SubTask> getAllSubTasks(){
        HashMap<Long, SubTask> allSubTasks=new HashMap<>();
        for(Task task:tasksINmanager.values()){
            if (task.getType().equals(Type.SUBTASK)){
                allSubTasks.put(task.getId(), (SubTask) task);
            }
        }
        return allSubTasks;
    }
    @Override
    public void deleteAllTasks(){
        List<Task>tasks=new ArrayList<>();
        for(Task task:tasksINmanager.values()){
            if (task.getType().equals(Type.TASK)){
               historyManager.remove(task.getId());
               prioritizedTasks.remove(task);
              //  tasksINmanager.remove(task.getId());
                tasks.add(task);
            }
        }
        tasksINmanager.values().removeAll(tasks);
    }
    @Override
    public void deleteAllSubTasks(){
        List<Task>tasks=new ArrayList<>();
        for(Task task:tasksINmanager.values()){
            if (task.getType().equals(Type.SUBTASK)){
                SubTask subTask= (SubTask) task;
                EpicTask epicTask= (EpicTask) tasksINmanager.get(subTask.getEpicTaskId());
                epicTask.getSubTasks().clear();
                epicTask.controlStatus();
                //tasksINmanager.remove(task.getId());
                tasks.add(task);
                historyManager.remove(task.getId());
                prioritizedTasks.remove(task);
            }
        }
        tasksINmanager.values().removeAll(tasks);
    }
    @Override
    public void deleteAllEpicTasks(){
        List<Task>tasks=new ArrayList<>();
        for(Task task:tasksINmanager.values()){
            if (task.getType().equals(Type.EPIC)){
                //tasksINmanager.remove(task.getId());
                historyManager.remove(task.getId());
                prioritizedTasks.remove(task);
                tasks.add(task);
            }
        }
        tasksINmanager.values().removeAll(tasks);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}