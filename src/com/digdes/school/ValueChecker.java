package com.digdes.school;

public class ValueChecker {
    public static boolean checkEqual(Object o1, Object o2) {
        if (o1==null || o2==null) return false;
        return String.valueOf(o1).equals(String.valueOf(o2));
    }

    public static boolean checkNotEqual(Object o1, Object o2) {
        if (o1==null || o2==null) return true;
        return !String.valueOf(o1).equals(String.valueOf(o2));
    }

    public static boolean checkEqualOrGreat(Object o1, Object o2) throws NumberFormatException {
        if (o1==null || o2==null) return false;
        return Double.parseDouble(String.valueOf(o1)) >= Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkEqualOrLess(Object o1, Object o2) throws NumberFormatException {
        if (o1==null || o2==null) return false;
        return Double.parseDouble(String.valueOf(o1)) <= Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkGreat(Object o1, Object o2) throws NumberFormatException {
        if (o1==null || o2==null) return false;
        return Double.parseDouble(String.valueOf(o1)) > Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkLess(Object o1, Object o2) throws NumberFormatException {
        if (o1==null || o2==null) return false;
        return Double.parseDouble(String.valueOf(o1)) < Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkLike(String o1, String o2) {
        if (o2.startsWith("%") && o2.endsWith("%")) return o1.contains(o2.substring(1, o2.length()-1));
        if (o2.startsWith("%") && !o2.endsWith("%")) return o1.endsWith(o2.substring(1));
        if (!o2.startsWith("%") && o2.endsWith("%")) return o1.startsWith(o2.substring(0, o2.length() - 1));
        return o1.equals(o2);
    }

    public static boolean checkIlike(String o1, String o2) {
        return checkLike(o1.toLowerCase(), o2.toLowerCase());
    }
}
