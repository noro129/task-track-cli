package com.tracker_cli.model;

import java.time.LocalDate;

public class RuleDetail {
    private TaskStatusEnum firstTaskStatus;
    private String firstTaskHash;
    private RuleEnum ruleRelation;
    private TaskStatusEnum secondTaskStatus;
    private String secondTaskHash;
    private LocalDate date;

    public RuleDetail() {
    }

    public RuleDetail(TaskStatusEnum firstTaskStatus, String firstTaskHash, RuleEnum ruleRelation, TaskStatusEnum secondTaskStatus, String secondTaskHash, LocalDate date) {
        this.firstTaskStatus = firstTaskStatus;
        this.firstTaskHash = firstTaskHash;
        this.ruleRelation = ruleRelation;
        this.secondTaskStatus = secondTaskStatus;
        this.secondTaskHash = secondTaskHash;
        this.date = date;
    }

    public TaskStatusEnum getFirstTaskStatus() {
        return firstTaskStatus;
    }

    public void setFirstTaskStatus(TaskStatusEnum firstTaskStatus) {
        this.firstTaskStatus = firstTaskStatus;
    }

    public String getFirstTaskHash() {
        return firstTaskHash;
    }

    public void setFirstTaskHash(String firstTaskHash) {
        this.firstTaskHash = firstTaskHash;
    }

    public RuleEnum getRuleRelation() {
        return ruleRelation;
    }

    public void setRuleRelation(RuleEnum ruleRelation) {
        this.ruleRelation = ruleRelation;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        if(getDate()==null) return "RuleDetail{" +
                "  firstTaskStatus=" + firstTaskStatus +
                ", firstTaskHash='" + firstTaskHash + '\'' +
                ", ruleRelation=" + ruleRelation +
                ", secondTaskStatus=" + secondTaskStatus +
                ", secondTaskHash='" + secondTaskHash + '\'' +
                '}';
        return "RuleDetail{" +
                "  firstTaskStatus=" + firstTaskStatus +
                ", firstTaskHash='" + firstTaskHash + '\'' +
                ", ruleRelation=" + ruleRelation +
                ", date=" + date +
                '}';
    }
}
