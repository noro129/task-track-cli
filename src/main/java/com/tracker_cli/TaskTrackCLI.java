package com.tracker_cli;


import com.tracker_cli.action.ActionTypeEnum;
import com.tracker_cli.executor.ActionExecutor;
import com.tracker_cli.utility.ActionTargetMapper;
import com.tracker_cli.utility.UtilityClass;

import java.util.Arrays;


public class TaskTrackCLI {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.err.println("ERROR: invalid command, missing action, use one of the following command actions: "+ UtilityClass.enumValuesToString(ActionTypeEnum.class));
            return;
        }

        String action = args[0];

        if(!UtilityClass.isValidEnumValue(ActionTypeEnum.class, action)) {
            System.err.println("ERROR: invalid command, invalid action "+action+", use one of the following command actions: "+ UtilityClass.enumValuesToString(ActionTypeEnum.class));
            return;
        }

        Class targetType = ActionTargetMapper.getTargetEnum(action);

        if(args.length == 1) {
            System.err.println("ERROR: invalid command, missing target, use one of the following command targets: "+ UtilityClass.enumValuesToString(targetType));
            return;
        }

        String targetValue = args[1];

        if(!UtilityClass.isValidEnumValue(targetType, targetValue)) {
            System.err.println("ERROR: invalid command, invalid target "+targetValue+", use one of the following command actions: "+ UtilityClass.enumValuesToString(targetType));
            return;
        }

        System.out.println("INFO: executing command: "+action+" "+targetValue);



        boolean result = (new ActionExecutor(ActionTypeEnum.valueOf(action.toUpperCase())))
                .executeAction(targetValue, Arrays.copyOfRange(args, 2, args.length));

        if(result) System.out.println("INFO: command executed successfully");
        else System.err.println("ERROR: could not execute the command.");
    }


}