package com.example.assignment.model;

public class TaskModel {

   public String title;
   public String description;
   public int status;
   public String key;
   public String path;

   public TaskModel(){}

    public TaskModel(String title, String description, int status, String path) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
