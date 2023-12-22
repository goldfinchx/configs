package ru.artorium.configs.utils;

import java.util.Collection;
import java.util.Map;

public class ClassUtils {

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isShort(String string) {
        try {
            Short.parseShort(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isByte(String string) {
        try {
            Byte.parseByte(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumber(String string) {
        return isDouble(string) || isInteger(string) || isLong(string) || isShort(string) || isFloat(string) || isByte(string);
    }

    public static boolean isArray(Object object) {
        return object instanceof Object[];
    }

    public static boolean isCollection(Object object) {
        return object instanceof Collection<?>;
    }

    public static boolean isMap(Object object) {
        return object instanceof Map<?,?>;
    }

    public static Class getCollectionType(Object object) {
        if (isMap(object)) {
            return Map.class;
        } else if (isArray(object)) {
            return Object[].class;
        } else if (isCollection(object)) {
            return Collection.class;
        } else {
            return null;
        }
    }

}
