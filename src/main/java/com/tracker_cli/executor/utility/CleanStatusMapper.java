package com.tracker_cli.executor.utility;

import com.tracker_cli.action.target.CleanActionTarget;
import com.tracker_cli.model.TaskStatusEnum;

public interface CleanStatusMapper {

    static TaskStatusEnum getTaskStatusFromCleanTarget(CleanActionTarget actionTarget) {
        return switch (actionTarget) {
            case CleanActionTarget.ALL, CleanActionTarget.SATISFIED -> null;
            case CleanActionTarget.DONE -> TaskStatusEnum.Done;
            case CleanActionTarget.INPROGRESS -> TaskStatusEnum.InProgress;
            case CleanActionTarget.STARTED -> TaskStatusEnum.Started;
            case CleanActionTarget.WAITING -> TaskStatusEnum.Waiting;
        };
    }
}
