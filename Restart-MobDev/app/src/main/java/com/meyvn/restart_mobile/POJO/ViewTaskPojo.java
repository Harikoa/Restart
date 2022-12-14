package com.meyvn.restart_mobile.POJO;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ViewTaskPojo {
    private String title;
    private String taskDate;
    private String taskDeadline;
    private String taskDescription;
    private String dateAccomplished;
    private boolean isComplete;
    private String taskReflection;
    private String ID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getTaskReflection() {
        return taskReflection;
    }

    public void setTaskReflection(String taskReflection) {
        this.taskReflection = taskReflection;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDateAccomplished() {
        return dateAccomplished;
    }

    public void setDateAccomplished(String dateAccomplished) {
        this.dateAccomplished = dateAccomplished;
    }
}
