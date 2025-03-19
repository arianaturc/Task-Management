package dataModel;

import java.io.Serializable;

public final class SimpleTask extends Task implements Serializable {
    private static final long serialVersionUID = 2314448046272300455L;
    private int startHour;
    private int endHour;

    public SimpleTask(int idTask, String statusTask, int startHour, int endHour) {
        super(idTask, statusTask);
        if(startHour < 1 || endHour < 1 || startHour > 24 || endHour > 24)
            throw new IllegalArgumentException("The hour must be between 1 and 24");
        if(startHour > endHour)
            throw new IllegalArgumentException("The start hour must be less than the end hour");
        this.startHour = startHour;
        this.endHour = endHour;
    }

    @Override
    public int estimateDuration() {
        return endHour - startHour;
    }

    @Override
    public String toString() {
        return "SimpleTask - ID: " + idTask + ", status: " + statusTask + ", start hour: " + startHour + ", end hour: " + endHour;
    }

}