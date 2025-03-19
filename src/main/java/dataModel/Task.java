package dataModel;

import java.io.Serializable;

sealed public abstract class Task implements Serializable permits ComplexTask, SimpleTask {
    int idTask;
    String statusTask;

    public Task(int idTask, String statusTask) {
        this.idTask = idTask;
        this.statusTask = statusTask;
    }

    public int getIdTask() {
        return idTask;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

    public abstract int estimateDuration();

    @Override
    public String toString() {
        return "ID: " + idTask;
    }

}