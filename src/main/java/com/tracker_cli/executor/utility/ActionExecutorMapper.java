package com.tracker_cli.executor.utility;

import com.tracker_cli.action.ActionTypeEnum;
import com.tracker_cli.action.target.AddActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.addExecutor.AddExecutor;
import com.tracker_cli.executor.cleanExecutor.CleanExecutor;

public interface ActionExecutorMapper {

    static Executor getExecutor(ActionTypeEnum action, String target) {
        return switch (action) {
            case ActionTypeEnum.ADD -> new AddExecutor(AddActionTarget.valueOf(target.toUpperCase()));
            case ActionTypeEnum.CLEAN -> new CleanExecutor(AddActionTarget.valueOf(target.toUpperCase()));
            default -> null;
        };
    }
}
