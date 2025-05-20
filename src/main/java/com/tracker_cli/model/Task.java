package com.tracker_cli.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
    private String id;
    private String taskName;
    private TaskStatusEnum taskStatus;
    private LocalDate taskDate;
    private LocalTime taskTime;

    public Task() {
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskStatus(TaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public void setTaskTime(LocalTime taskTime) {
        this.taskTime = taskTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskDate=" + taskDate +
                ", taskTime=" + taskTime +
                '}';
    }
}
