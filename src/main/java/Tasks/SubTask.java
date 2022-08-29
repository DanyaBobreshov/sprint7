package Tasks;

public class SubTask extends Task {
   private Long epicTaskId;

    public SubTask(String name, String text, Long id, Tasks.StatusOfTask status){
        super(name, text, id, status);
    }

    public Long getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(Long epicTaskId) {
        this.epicTaskId = epicTaskId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }


    @Override
    public String toString() {
        return getId()+","+getType()+","+getName()+","+status+","+getText()+","+
                startTime+","+getEndTime()+","+duration+","+getEpicTaskId();
    }
}
