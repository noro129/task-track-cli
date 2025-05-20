package com.tracker_cli.executor;

import com.tracker_cli.action.ActionTypeEnum;
import com.tracker_cli.executor.utility.ActionExecutorMapper;

public class ActionExecutor {
    private final ActionTypeEnum action;

    public ActionExecutor(ActionTypeEnum action) {
        this.action= action;
    }


    public boolean executeAction(String target, String[] arguments){
        Executor executor = ActionExecutorMapper.getExecutor(action, target);
        if(executor == null) {
            System.err.println("ERROR: could not find the appropriate executor for action "+action.toString()+".");
            return false;
        }
        return executor.execute(arguments);
    }


}
