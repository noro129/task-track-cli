package com.tracker_cli.executor.removeExecutor;

import com.tracker_cli.action.target.RemoveActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;

public class RemoveExecutor extends Executor {
    private final RemoveActionTarget actionTarget;

    public RemoveExecutor(RemoveActionTarget actionTarget) { this.actionTarget = actionTarget; }

    @Override
    public boolean execute(String[] arguments) {
        if(arguments.length!=1) {
            System.err.println("ERROR: invalid command, valid command form is:" +
                    "\n\t remove <task or rule> <task-hash or rule-hash>");
            return false;
        }
        String hash = arguments[0].toUpperCase();
        return switch (actionTarget) {
            case RemoveActionTarget.RULE -> DataOperator.removeRule(hash);
            case RemoveActionTarget.TASK -> DataOperator.removeTask(hash);
        };
    }
}
