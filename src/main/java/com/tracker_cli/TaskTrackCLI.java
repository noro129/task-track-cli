package com.tracker_cli;


import com.tracker_cli.action.ActionTypeEnum;
import com.tracker_cli.utility.ActionTargetMapper;
import com.tracker_cli.utility.UtilityClass;

import java.util.logging.Logger;

public class TaskTrackCLI {
    private final static Logger logger = Logger.getLogger(TaskTrackCLI.class.getName());

    public static void main(String[] args) {
        if(args.length == 0) {
            logger.severe("invalid command, missing action, use one of the following command actions: "+ UtilityClass.enumValuesToString(ActionTypeEnum.class));
            return;
        }

        String action = args[0];

        if(!UtilityClass.isValidEnumValue(ActionTypeEnum.class, action)) {
            logger.severe("invalid command, invalid action "+action+", use one of the following command actions: "+ UtilityClass.enumValuesToString(ActionTypeEnum.class));
            return;
        }

        Class targetType = ActionTargetMapper.getTargetEnum(action);

        if(args.length == 1) {
            logger.severe("invalid command, missing target, use one of the following command targets: "+ UtilityClass.enumValuesToString(targetType));
            return;
        }

        String targetValue = args[1];



    }


}