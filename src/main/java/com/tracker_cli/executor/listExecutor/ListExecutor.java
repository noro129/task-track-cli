package com.tracker_cli.executor.listExecutor;

import com.tracker_cli.action.target.ListActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;
import com.tracker_cli.executor.utility.Printer;
import com.tracker_cli.model.Task;
import com.tracker_cli.model.TaskStatusEnum;
import com.tracker_cli.utility.UtilityClass;

import static com.tracker_cli.utility.UtilityClass.getTaskStatusEnumFromValue;

import java.util.*;


public class ListExecutor extends Executor {
    private final ListActionTarget actionTarget;

    public ListExecutor(ListActionTarget actionTarget) { this.actionTarget = actionTarget; }

    @Override
    public boolean execute(String[] arguments) {
        return switch (actionTarget) {
            case ListActionTarget.TASK -> listTasks(arguments);
            case ListActionTarget.RULE -> listRules(arguments);
        };
    }

    private boolean listTasks(String[] arguments) {
        Set<TaskStatusEnum> include = new HashSet<>();
        if(arguments.length == 0 ) include.addAll(Arrays.asList(TaskStatusEnum.values()));
        else if (arguments[0].equalsIgnoreCase("-f")) {
            for(int i=1; i< arguments.length; i++) {
                TaskStatusEnum taskStatus = getTaskStatusEnumFromValue(arguments[i]);
                if( taskStatus == null ) {
                    System.err.println("ERROR: invalid task status, valid options are: "+ UtilityClass.enumValuesToString(TaskStatusEnum.class));
                    System.err.println("ERROR: to use the list task");
                    System.err.println("ERROR: -f optional flag, used for filtering tasks by status, accepts white space separated list of task status");
                    return false;
                }
                include.add(taskStatus);
            }
        } else if (arguments[0].startsWith("-")) {
            System.err.println("ERROR: invalid flag option: "+arguments[0]+" only valid flags are: -f");
            System.err.println("\n\tEXAMPLE: list task -f waiting started");
            return false;
        }
        List<Task> taskList = DataOperator.listTasks(include);
        if(taskList == null) return false;

        Printer.printTasks(taskList);
        return true;
    }

    private boolean listRules(String[] arguments) {
        return true;
    }
}
