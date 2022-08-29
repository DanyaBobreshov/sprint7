package Tasks;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Task {
    private String name;
    private String text;
    private Long id;
    //private Type type;
    protected Long duration;
    protected LocalDateTime startTime;
    public StatusOfTask status;

    public Task(){
        this.name = "noName";
        this.text = "noText";
        status = StatusOfTask.NEW;
        id = 0L;
    }

    public Task(String name, String text, Long id, StatusOfTask status){
        this.name = name;
        this.text = text;
        this.id = id;
        this.status = status;
        startTime=LocalDateTime.MIN;
        duration=0L;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }




    public void setName(String name){
        if (name != null)this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return id+","+getType()+","+name+","+status+","+text+","+
                startTime+","+getEndTime()+","+duration;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text != null)
            this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if(id > 0)
            this.id = id;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(StatusOfTask status) {
        if(status != null) {
            this.status = status;
        }
    }

    public Type getType() {
        return Type.TASK;
    }

    public int compareTo(Task o){
        return this.getStartTime().compareTo(o.getStartTime());
    }

}
