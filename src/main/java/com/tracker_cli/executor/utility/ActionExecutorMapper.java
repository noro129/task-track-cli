package com.tracker_cli.executor.utility;

import com.tracker_cli.action.ActionTypeEnum;
import com.tracker_cli.action.target.AddActionTarget;
import com.tracker_cli.action.target.CleanActionTarget;
import com.tracker_cli.action.target.ListActionTarget;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.addExecutor.AddExecutor;
import com.tracker_cli.executor.cleanExecutor.CleanExecutor;
import com.tracker_cli.executor.listExecutor.ListExecutor;

public interface ActionExecutorMapper {

    static Executor getExecutor(ActionTypeEnum action, String target) {
        return switch (action) {
            case ActionTypeEnum.ADD -> new AddExecutor(AddActionTarget.valueOf(target.toUpperCase()));
            case ActionTypeEnum.CLEAN -> new CleanExecutor(CleanActionTarget.valueOf(target.toUpperCase()));
            case ActionTypeEnum.LIST -> new ListExecutor(ListActionTarget.valueOf(target.toUpperCase()));
            default -> null;
        };
    }
}
