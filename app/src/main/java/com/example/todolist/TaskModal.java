package com.example.todolist;

public class TaskModal {
    private String taskName;
    private String taskContent;
    private String taskDeadline;
    private boolean isCompleted;
    private String taskCreationDate;
    private String taskPriority;
    private int id;

    // creating getter and setter methods
    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTaskContent()
    {
        return taskContent;
    }

    public void setTaskContent(String taskContent)
    {
        this.taskContent = taskContent;
    }

    public String getTaskDeadline()
    {
        return taskDeadline;
    }

    public void setTaskDeadline(String taskDeadline)
    {
        this.taskDeadline = taskDeadline;
    }

    public boolean getIsCompleted()
    {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted)
    {
        this.isCompleted = isCompleted;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTaskCreationDate(){
        return taskCreationDate;
    }

    public void setTaskCreationDate(String taskCreationDate){
        this.taskCreationDate = taskCreationDate;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority){
        this.taskPriority = taskPriority;
    }

    // constructor
    public TaskModal(String taskName, String taskContent, String taskDeadline, String creationDate, boolean isCompleted, String taskPriority)
    {
        this.taskName = taskName;
        this.taskContent = taskContent;
        this.taskDeadline = taskDeadline;
        this.isCompleted = isCompleted;
        this.taskCreationDate = creationDate;
        this.taskPriority = taskPriority;
    }
}
