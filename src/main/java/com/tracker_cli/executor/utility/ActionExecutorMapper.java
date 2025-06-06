package com.tracker_cli.executor.utility;

import com.tracker_cli.action.ActionTypeEnum;
import com.tracker_cli.action.target.*;
import com.tracker_cli.executor.Executor;
import com.tracker_cli.executor.addExecutor.AddExecutor;
import com.tracker_cli.executor.cleanExecutor.CleanExecutor;
import com.tracker_cli.executor.listExecutor.ListExecutor;
import com.tracker_cli.executor.removeExecutor.RemoveExecutor;
import com.tracker_cli.executor.updateExecutor.UpdateExecutor;

public interface ActionExecutorMapper {

    static Executor getExecutor(ActionTypeEnum action, String target) {
        return switch (action) {
            case ActionTypeEnum.ADD -> new AddExecutor(AddActionTarget.valueOf(target.toUpperCase()));
            case ActionTypeEnum.CLEAN -> new CleanExecutor(CleanActionTarget.valueOf(target.toUpperCase()));
            case ActionTypeEnum.LIST -> new ListExecutor(ListActionTarget.valueOf(target.toUpperCase()));
            case ActionTypeEnum.UPDATE -> new UpdateExecutor(UpdateActionTarget.valueOf(target.toUpperCase()));
            case ActionTypeEnum.REMOVE -> new RemoveExecutor(RemoveActionTarget.valueOf(target.toUpperCase()));
        };
    }
}
