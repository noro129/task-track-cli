package com.tracker_cli.executor.cleanExecutor;

import com.tracker_cli.action.target.AddActionTarget;
import com.tracker_cli.action.target.CleanActionTarget;
import com.tracker_cli.executor.Executor;

import java.util.Arrays;

public class CleanExecutor extends Executor {
    private final CleanActionTarget actionTarget;

    public CleanExecutor(CleanActionTarget actionTarget) {this.actionTarget = actionTarget;}

    @Override
    public boolean execute(String[] arguments) {
        return true;
    }
}
