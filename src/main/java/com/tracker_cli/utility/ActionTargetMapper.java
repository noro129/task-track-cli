package com.tracker_cli.utility;

import com.tracker_cli.action.target.*;

public interface ActionTargetMapper {

    static Class<? extends Enum<?>> getTargetEnum(String action) {
        return switch (action.toLowerCase()) {
            case "add" -> AddActionTarget.class;
            case "list" -> ListActionTarget.class;
            case "remove" -> RemoveActionTarget.class;
            case "clean" -> CleanActionTarget.class;
            case "update" -> UpdateActionTarget.class;
            default -> AddActionTarget.class;
        };
    }
}
