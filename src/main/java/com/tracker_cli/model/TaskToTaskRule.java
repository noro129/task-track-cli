package com.tracker_cli.model;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("taskToTask")
public class TaskToTaskRule extends Rule{
    private TaskStatusEnum secondTaskStatus;
    private String secondTaskHash;

    public TaskToTaskRule() { }

    public TaskToTaskRule(String id, TaskStatusEnum firstTaskStatus, String firstTaskHash, RuleEnum ruleRelation, TaskStatusEnum secondTaskStatus, String secondTaskHash) {
        super(id, firstTaskStatus, firstTaskHash, ruleRelation);
        this.secondTaskStatus = secondTaskStatus;
        this.secondTaskHash = secondTaskHash;
    }

    public TaskStatusEnum getSecondTaskStatus() {
        return secondTaskStatus;
    }

    public void setSecondTaskStatus(TaskStatusEnum secondTaskStatus) {
        this.secondTaskStatus = secondTaskStatus;
    }

    public String getSecondTaskHash() {
        return secondTaskHash;
    }

    public void setSecondTaskHash(String secondTaskHash) {
        this.secondTaskHash = secondTaskHash;
    }

    @Override
    public String toString() {
        return "RuleId={"+getId()+"} \t"+getFirstTaskStatus()+" "+getFirstTaskHash()+" "+getRuleRelation()+" "+secondTaskStatus+" "+secondTaskHash;
    }
}
