package com.tracker_cli.executor.removeExecutor;

import com.tracker_cli.action.target.RemoveActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;

public class RemoveExecutor extends Executor {
    private final RemoveActionTarget actionTarget;

    public RemoveExecutor(RemoveActionTarget actionTarget) { this.actionTarget = actionTarget; }

    @Override
    public boolean execute(String[] arguments) {
        if(arguments.length!=1 && arguments.length!=2) {
            System.err.println("ERROR: invalid command, valid command forms are:" +
                    "\n\t remove <task or rule> <task-hash or rule-hash>" +
                    "\n\t remove <task or rule> <task-hash or rule-hash> --safe-check");
            return false;
        }
        String hash = arguments[0].toUpperCase();
        boolean safeCheck = false;
        if(arguments.length==2) {
            if(arguments[1].equals("--safe-check")) {
                safeCheck = true;
            } else {
                System.err.println("ERROR: invalid command, valid command forms are:" +
                        "\n\t remove <task or rule> <task-hash or rule-hash>" +
                        "\n\t remove <task or rule> <task-hash or rule-hash> --safe-check");
                return false;
            }
        }
        return switch (actionTarget) {
            case RemoveActionTarget.RULE -> DataOperator.removeRule(hash, safeCheck);
            case RemoveActionTarget.TASK -> DataOperator.removeTask(hash, safeCheck);
        };
    }
}
