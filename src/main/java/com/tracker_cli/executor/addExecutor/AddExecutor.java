package com.tracker_cli.executor.addExecutor;

import com.tracker_cli.action.target.AddActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;
import com.tracker_cli.model.TaskDetail;
import com.tracker_cli.model.TaskStatusEnum;

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
        TaskDetail task = extractTaskDetailArgs(arguments);
        if(task==null) return false;
        DataOperator.addTask(task);
        return true;
    }

    private boolean addRule(String[] arguments) {
        DataOperator.addRule();
        return true;
    }

    private TaskDetail extractTaskDetailArgs(String[] arguments) {
        String taskName = null;
        TaskStatusEnum taskStatus = TaskStatusEnum.Waiting;
        for(int i=0; i<arguments.length-1; i+=2) {
            if(arguments[i].equals("--task-name")) taskName = arguments[i+1];
            if(arguments[i].equals("--task-status")) taskStatus = getTaskStatusEnumFromValue(arguments[i+1]);
        }

        if(taskName==null) {
            return null;
        }
        return new TaskDetail(taskName, taskStatus);
    }

    private TaskStatusEnum getTaskStatusEnumFromValue(String value){
        return switch (value.toLowerCase()) {
            case "inprogress" -> TaskStatusEnum.InProgress;
            case "done" -> TaskStatusEnum.Done;
            case "started" -> TaskStatusEnum.Started;
            default -> TaskStatusEnum.Waiting;
        };
    }
}
