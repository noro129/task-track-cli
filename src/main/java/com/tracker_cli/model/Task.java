package com.tracker_cli.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
    private String id;
    private String taskName;
    private TaskStatusEnum taskStatus;
    private LocalDate taskDate;
    private LocalTime taskTime;

    public Task(String id, String taskName, TaskStatusEnum taskStatus, LocalDate taskDate, LocalTime taskTime) {
        this.id = id;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
    }

    public String getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public LocalTime getTaskTime() {
        return taskTime;
    }
}
