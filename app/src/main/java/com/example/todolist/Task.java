package com.example.todolist;

import java.util.ArrayList;

public class Task {
    private String taskTitle;
    private String taskContents;

    public Task(String title, String content){
        taskTitle = title;
        taskContents = content;
    }

    public String getTaskTitle(){
        return taskTitle;
    }

    public String getTaskContents(){
        return taskContents;
    }

    private static int lastTaskId = 0;

    public static ArrayList<Task> createTasksList(int numTasks) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        for(int i = 1; i <= numTasks; i++){
            tasks.add(new Task("Task" + ++lastTaskId, "stuff" + lastTaskId));
        }
        return tasks;
    }
}
