package com.tracker_cli.executor.cleanExecutor;

import com.tracker_cli.action.target.CleanActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;
import com.tracker_cli.model.TaskStatusEnum;

public class CleanExecutor extends Executor {
    private final CleanActionTarget actionTarget;

    public CleanExecutor(CleanActionTarget actionTarget) {this.actionTarget = actionTarget;}

    @Override
    public boolean execute(String[] arguments) {
        System.out.println("cleaning "+actionTarget.toString()+" tasks.");
        boolean showFirst = false;
        if (arguments.length == 1 && arguments[0].equals("--safe-check")) {
            showFirst=true;
        } else if (arguments.length!=0) {
            System.err.println("ERROR: invalid flag, valid flags are:" +
                    "\t --safe-check \tOptional, check first tasks that are going to be deleted.");
            return false;
        }

        DataOperator.cleanTasks(showFirst, actionTarget);
        return true;
    }
}
