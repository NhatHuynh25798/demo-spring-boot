package com.demo.utils;

public class SystemUtils {

    private static SystemUtils systemUtils;

    public static SystemUtils getInstance() {
        if (systemUtils == null) {
            systemUtils = new SystemUtils();
        }
        return systemUtils;
    }

    public String buildFieldGetter(String fieldName) {
        char[] fieldNameChars = fieldName.toCharArray();
        fieldNameChars[0] = Character.toUpperCase(fieldNameChars[0]);

        return "get" + new String(fieldNameChars);
    }

    public String buildFieldSetter(String fieldName) {
        char[] fieldNameChars = fieldName.toCharArray();
        fieldNameChars[0] = Character.toUpperCase(fieldNameChars[0]);

        return "set" + new String(fieldNameChars);
    }
}
