package Tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks=new ArrayList<>();
    LocalDateTime endTime=LocalDateTime.now();
    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
        for (SubTask st:subTasks){
            setDuration(st);
        }
        controlStatus();
    }

    public EpicTask(String name, String text, Long id){
        super(name, text, id, StatusOfTask.NEW);
    }

    public void addSubTask(SubTask subTask){
        subTask.setStatus(StatusOfTask.NEW);
        subTask.setEpicTaskId(this.getId());
        subTasks.add(subTask);
        setDuration(subTask);
        controlStatus();
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString(){
        String s = getId()+","+getType()+","+getName()+","+status+","+getText()+","+
                startTime+","+getEndTime()+","+duration;
        return s;
    }


    public void  controlStatus(){
        this.status = StatusOfTask.IN_PROGRESS;
        boolean tempProgress = false;
        int tempDone = 0;
        for(SubTask st: subTasks){
            if(st.getStatus().equals(StatusOfTask.IN_PROGRESS)) {
                tempProgress = true;
            }
            else if(st.getStatus().equals(StatusOfTask.DONE)){
                tempDone++;
            }
        }
        if(!tempProgress && tempDone == 0){
            this.status = StatusOfTask.NEW;
        }
        else if(tempDone == subTasks.size()){
            this.status = StatusOfTask.DONE;
        }
        setTimeStart();
    }

    public void setTimeStart(){
        if (subTasks.isEmpty()){
            return;
        }
        if(duration==null){
            duration=0L;
        }
        startTime=subTasks.get(0).getStartTime();
        for(SubTask st: subTasks){
            if (st.getStartTime().isBefore(startTime)){
                startTime=st.getStartTime();
            }
        }
    }

    public SubTask getSubTask(Long number){
        for(SubTask st: subTasks){
            if(Objects.equals(st.getId(), number))
                return st;
        }
        return null;
    }
    public void progressTask(Long number){
        Task task = this.getSubTask(number);
        if(task != null) {
            task.setStatus(StatusOfTask.IN_PROGRESS);
            controlStatus();
        } else {
            System.out.println("нет такого задания");
        }
        controlStatus();
    }

    public void doneTask(Long number){
        Task task = this.getSubTask(number);
        if(task != null) {
            task.setStatus(StatusOfTask.DONE);
            controlStatus();
        } else {
            System.out.println("нет такого задания");
        }
        controlStatus();
    }

    @Override
    public LocalDateTime getEndTime() {
        endTime=startTime.plusMinutes(duration);
        if (subTasks != null) {
            for (SubTask st : subTasks) {
                if (st.getEndTime().isAfter(endTime)) {
                    endTime = st.getEndTime();
                }
            }
        }
        return endTime;
    }

    public void setDuration(SubTask subTask){
        duration = duration+subTask.getDuration();
    }
}