package dataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task implements Serializable {
    private static final long serialVersionUID = 2314448046272300455L;
    private List<Task> tasks;

    public ComplexTask(int idTask, String statusTask) {
        super(idTask, statusTask);
        tasks = new ArrayList<Task>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }


    @Override
    public int estimateDuration(){
        int duration = 0;
        for(Task task : tasks){
            duration += task.estimateDuration();
        }
        return duration;
    }

    @Override
    public String toString() {
        return "ComplexTask - ID: " + idTask + ", status: " + statusTask;
    }


}