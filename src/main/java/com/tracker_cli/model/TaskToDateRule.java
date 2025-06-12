package com.tracker_cli.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;

@JsonTypeName("taskToDate")
public class TaskToDateRule extends Rule{
    private LocalDate date;

    public TaskToDateRule() { }

    public TaskToDateRule(String id, TaskStatusEnum firstTaskStatus, String firstTaskHash, RuleEnum ruleRelation, LocalDate date) {
        super(id, firstTaskStatus, firstTaskHash, ruleRelation);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RuleId={"+getId()+"} \t"+getFirstTaskStatus()+" "+getFirstTaskHash()+" "+getRuleRelation()+" "+date;
    }
}
