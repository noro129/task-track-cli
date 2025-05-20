package com.tracker_cli.executor.cleanExecutor;

import com.tracker_cli.action.target.AddActionTarget;
import com.tracker_cli.executor.Executor;

public class CleanExecutor extends Executor {
    private final AddActionTarget actionTarget;

    public CleanExecutor(AddActionTarget actionTarget) {this.actionTarget = actionTarget;}

    @Override
    public boolean execute(String[] arguments) {
        return true;
    }
}
