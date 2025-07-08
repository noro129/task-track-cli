package com.tracker_cli.executor.cleanExecutor;

import com.tracker_cli.action.target.CleanActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;

public class CleanExecutor extends Executor {
    private final CleanActionTarget actionTarget;

    public CleanExecutor(CleanActionTarget actionTarget) {this.actionTarget = actionTarget;}

    @Override
    public boolean execute(String[] arguments) {
        System.out.println("INFO: cleaning "+actionTarget.toString()+" tasks.");
        boolean showFirst = false;
        boolean cleanSatisfiedTaskToTask = false;
        boolean cleanSatisfiedTaskToDate = false;

        for (String arg : arguments) {
            if(arg.equals("-t")) cleanSatisfiedTaskToTask = true;
            if(arg.equals("-d")) cleanSatisfiedTaskToDate = true;
            if(arg.equals("--safe-check")) showFirst = true;
        }

        if(actionTarget==CleanActionTarget.SATISFIED) {
            if(cleanSatisfiedTaskToTask && cleanSatisfiedTaskToDate) {
                System.err.println("ERROR: invalid arguments, no need to use both -t and -d at the same time");
            }
            if(!cleanSatisfiedTaskToTask || !cleanSatisfiedTaskToDate) {
                return DataOperator.cleanSatisfied(showFirst, true, true);
            }
            return DataOperator.cleanSatisfied(showFirst, cleanSatisfiedTaskToTask, cleanSatisfiedTaskToDate);
        } else {
            if (arguments.length == 1 && arguments[0].equals("--safe-check")) {
                showFirst=true;
                System.out.println("INFO: listing tasks selected to be deleted.");
            } else if (arguments.length!=0) {
                System.err.println("ERROR: invalid flag, valid flags are:" +
                        "\t --safe-check \tOptional, check first tasks that are going to be deleted.");
                return false;
            }

            return DataOperator.cleanTasks(showFirst, actionTarget);
        }
    }
}
