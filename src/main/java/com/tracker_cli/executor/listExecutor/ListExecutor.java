package com.tracker_cli.executor.listExecutor;

import com.tracker_cli.action.target.ListActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;
import com.tracker_cli.executor.utility.Printer;
import com.tracker_cli.model.Rule;
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
        if(arguments.length != 0 && arguments.length !=2 && arguments.length!=4) {
            System.err.println("ERROR: invalid command arguments." +
                    "\n valid flags: " +
                    "\n\t--type          optional, accepts either taskToTask or TaskToDate values." +
                    "\n\t--task-hash     optional, list only rules applied on a specific task, by providing the task id.");
            return false;
        }
        String type = null;
        String taskHash = null;
        for(int i=0; i<arguments.length; i+=2) {
            if(arguments[i].equalsIgnoreCase("--task-hash")) taskHash = arguments[i+1];
            if(arguments[i].equalsIgnoreCase("--type")) type = arguments[i+1];
        }

        if(arguments.length == 4 && (type == null || taskHash == null)) {
            System.err.println("ERROR: invalid command arguments." +
                    "\n valid flags: " +
                    "\n\t--type          optional, accepts either taskToTask or TaskToDate values." +
                    "\n\t--task-hash     optional, list only rules applied on a specific task, by providing the task id.");
            return false;
        }

        if(arguments.length == 2 && type == null && taskHash == null) {
            System.err.println("ERROR: invalid command arguments." +
                    "\n valid flags: " +
                    "\n\t--type          optional, accepts either taskToTask or TaskToDate values." +
                    "\n\t--task-hash     optional, list only rules applied on a specific task, by providing the task id.");
            return false;
        }
        if (type!=null && (!type.equalsIgnoreCase("tasktotask") && !type.equalsIgnoreCase("tasktodate"))) {
            System.err.println("ERROR: invalid type option "+type+", the only valid options are: taskToTask, taskToDate");
            return false;
        }

        List<Rule> ruleList = DataOperator.listRules(type, taskHash);

        if(ruleList == null) return false;
        Printer.printRules(ruleList);
        return true;
    }
}
