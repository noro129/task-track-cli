package com.tracker_cli.executor.addExecutor;

import com.tracker_cli.action.target.AddActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;
import com.tracker_cli.model.RuleDetail;
import com.tracker_cli.model.RuleEnum;
import com.tracker_cli.model.TaskDetail;
import com.tracker_cli.model.TaskStatusEnum;
import com.tracker_cli.utility.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.tracker_cli.utility.UtilityClass.getTaskStatusEnumFromValue;

public class AddExecutor extends Executor {
    private final AddActionTarget actionTarget;

    public AddExecutor(AddActionTarget actionTarget) {this.actionTarget = actionTarget;}


    @Override
    public boolean execute(String[] arguments) {
        return switch (actionTarget) {
            case AddActionTarget.TASK -> addTask(arguments);
            case AddActionTarget.RULE -> addRule(arguments);
        };
    }

    private boolean addTask(String[] arguments) {
        TaskDetail taskDetail = extractTaskDetailArgs(arguments);
        if(taskDetail==null) return false;
        return DataOperator.addTask(taskDetail);
    }

    private boolean addRule(String[] arguments) {
        RuleDetail ruleDetail = extractRuleDetailArgs(arguments);
        if(ruleDetail==null) return false;
        return DataOperator.addRule(ruleDetail);
    }

    private TaskDetail extractTaskDetailArgs(String[] arguments) {
        String taskName = null;
        TaskStatusEnum taskStatus = TaskStatusEnum.Waiting;
        for(int i=0; i<arguments.length-1; i+=2) {
            if(arguments[i].equals("--task-name")) taskName = arguments[i+1];
            if(arguments[i].equals("--task-status")) taskStatus = getTaskStatusEnumFromValue(arguments[i+1]);
        }

        if (taskStatus==null) {
            System.err.println("ERROR: invalid task status, please choose one of the following "+ UtilityClass.enumValuesToString(TaskStatusEnum.class));
            return null;
        }

        if(taskName==null) {
            System.err.println("ERROR: missing task name field --task-name");
            return null;
        }
        return new TaskDetail(taskName, taskStatus);
    }

    private RuleDetail extractRuleDetailArgs(String[] arguments) {
        if (arguments.length != 4 && arguments.length!=5) {
            System.err.println("ERROR: invalid command, to add a rule use one of the following forms:" +
                    "\n--- add rule <task 1 status> <task 1 id> <rule relation> <task 2 status> <task 2 id>" +
                    "\n\tEXAMPLE: add rule start 2ER56 before done 33YUI0" +
                    "\n--- add rule <task 1 status> <task 1 id> <rule relation> <date:dd-MM-yyyy>" +
                    "\n\tEXAMPLE: add rule done 33YUI0 before 23-05-2025");
            return null;
        }
        TaskStatusEnum firstTaskStatus = getTaskStatusEnumFromValue(arguments[0]);
        if(firstTaskStatus==null || firstTaskStatus == TaskStatusEnum.Waiting) {
            if(firstTaskStatus != null)
                System.err.print("ERROR: 'waiting' task status cannot be included in rule creation, valid rule task status are: ");
            else
                System.err.print("ERROR: invalid rule task status '"+arguments[0]+"', valid rule task status are: ");
            for (TaskStatusEnum relation : TaskStatusEnum.values()) {
                if(relation != TaskStatusEnum.Waiting) System.err.print(relation+", ");
            }
            System.err.println();
            return null;
        }
        String firstTaskId = arguments[1].toUpperCase();
        RuleEnum ruleRelation = getRuleEnumFromValue(arguments[2]);
        if(ruleRelation==null) {
            System.err.println("ERROR: invalid rule relation '"+arguments[2]+"', valid rule relations are: "+UtilityClass.enumValuesToString(RuleEnum.class));
            return null;
        }
        LocalDate date= null;
        TaskStatusEnum secondTaskStatus=null;
        String secondTaskId=null;

        if (arguments.length==4) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try{
                date = LocalDate.parse(arguments[3], formatter);
            } catch (DateTimeParseException e) {
                System.err.println("ERROR: parsing date "+arguments[3]+" failed, expected format : dd-MM-yyyy (ex: 23-05-2025)");
                return null;
            }
        } else {
            secondTaskStatus = getTaskStatusEnumFromValue(arguments[3]);
            if(secondTaskStatus==null) {
                System.err.print("ERROR: invalid task status '"+arguments[3]+"', valid task status are: ");
                for (TaskStatusEnum relation : TaskStatusEnum.values()) {
                    if(relation != TaskStatusEnum.Waiting) System.err.print(relation+", ");
                }
                System.err.println();
                return null;
            }
            secondTaskId = arguments[4].toUpperCase();
        }

        return new RuleDetail(firstTaskStatus, firstTaskId, ruleRelation, secondTaskStatus, secondTaskId, date);
    }

    private RuleEnum getRuleEnumFromValue(String value) {
        return switch (value.toLowerCase()) {
            case "before" -> RuleEnum.BEFORE;
            case "after" -> RuleEnum.AFTER;
            default -> null;
        };
    }
}
