package com.tracker_cli.executor.updateExecutor;

import com.tracker_cli.action.target.UpdateActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.utility.DataOperator;
import com.tracker_cli.utility.UtilityClass;

import java.util.Arrays;

public class UpdateExecutor extends Executor {

    private final UpdateActionTarget actionTarget;

    public UpdateExecutor(UpdateActionTarget updateActionTarget) {this.actionTarget = updateActionTarget;}

    @Override
    public boolean execute(String[] arguments) {
        String taskHash = null;
        String taskMessage = null;
        String taskStatus = null;
        if(arguments.length!=3 && arguments.length!=5) {
            System.err.println("ERROR: invalid command arguments, to use the command follow one of the following forms:" +
                    "\n\t update task <Task Hash> --task-name <New Task Message>" +
                    "\n\t update task <Task Hash> --task-status <New Task Status>" +
                    "\n\t update task <Task Hash> --task-name <New Task Message> --task-status <New Task Status>");
            return false;
        }
        taskHash = arguments[0];
        for(int i=1; i<arguments.length-1; i+=2) {
            if(arguments[i].equals("--task-name")) taskMessage = arguments[i+1].strip();
            if(arguments[i].equals("--task-status")) taskStatus = arguments[i+1].strip();
        }

        if(taskMessage == null && taskStatus == null) {
            System.err.println("ERROR: invalid command arguments, to use the command follow one of the following forms:" +
                    "\n\t update task <Task Hash> --task-name <New Task Message>" +
                    "\n\t update task <Task Hash> --task-status <New Task Status>" +
                    "\n\t update task <Task Hash> --task-name <New Task Message> --task-status <New Task Status>");
            return false;
        }


        return DataOperator.updateTask(taskHash, taskMessage, UtilityClass.getTaskStatusEnumFromValue(taskStatus));
    }
}
