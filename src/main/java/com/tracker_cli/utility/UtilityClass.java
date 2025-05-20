package com.tracker_cli.utility;

public class UtilityClass {

    public static <E extends Enum<E>> String enumValuesToString(Class<E> enumType) {
        StringBuilder resultBuilder = new StringBuilder();
        E[] enumValues = enumType.getEnumConstants();

        resultBuilder.append(enumValues[0]);

        for(int valueIndex = 1; valueIndex<enumValues.length; valueIndex++) resultBuilder.append(", ").append(enumValues[valueIndex]);

        return resultBuilder.toString();
    }

    public static boolean isValidEnumValue(Class<? extends Enum<?>> enumType, String enumValue) {
        for (Enum<?> validValue : enumType.getEnumConstants()) {
            if(validValue.toString().equalsIgnoreCase(enumValue)) return true;
        }
        return false;
    }


}
