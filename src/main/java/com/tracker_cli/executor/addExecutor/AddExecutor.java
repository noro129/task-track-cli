package com.tracker_cli.executor.addExecutor;

import com.tracker_cli.action.target.AddActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;
import com.tracker_cli.model.Task;
import com.tracker_cli.model.TaskDetail;

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
        if (arguments.length == 0) {
            System.err.println("ERROR: invalid command, missing task name");
            return null;
        }

    }
}
