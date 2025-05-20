package com.tracker_cli.model;

public class TaskDetail {
    private String taskName;
    private TaskStatusEnum taskStatus;

    public TaskDetail(String taskName, TaskStatusEnum taskStatus) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskStatusEnum getTaskStatus() {
        return taskStatus;
    }
}
