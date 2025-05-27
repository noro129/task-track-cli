package com.tracker_cli.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type" )
@JsonSubTypes({
        @JsonSubTypes.Type(value = TaskToTaskRule.class, name = "taskToTask"),
        @JsonSubTypes.Type(value = TaskToDateRule.class, name = "taskToDate")
})
public abstract class Rule {
    private String id;
    private TaskStatusEnum firstTaskStatus;
    private String firstTaskHash;
    private RuleEnum ruleRelation;

    public Rule() { }

    public Rule(String id, TaskStatusEnum firstTaskStatus, String firstTaskHash, RuleEnum ruleRelation) {
        this.id = id;
        this.firstTaskStatus = firstTaskStatus;
        this.firstTaskHash = firstTaskHash;
        this.ruleRelation = ruleRelation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Rule{" +
                "id='" + id + '\'' +
                ", firstTaskStatus=" + firstTaskStatus +
                ", firstTaskHash='" + firstTaskHash + '\'' +
                ", ruleRelation=" + ruleRelation +
                '}';
    }
}
