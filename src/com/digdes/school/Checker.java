package com.digdes.school;

import java.util.Map;

public class Checker {
    private static final String[] equalOperators = new String[]{
            ">=",
            "<=",
            "!=",
            "=",
            "ilike",
            "like",
            ">",
            "<"
    };

    public static boolean checkCondition(Map<String, Object> row1, String condition) throws Exception {
        for (int i = 0; i < equalOperators.length; i++) {
            if (condition.contains(equalOperators[i])) {
                Pair pair = new Pair(condition, equalOperators[i]);
                String attribute = pair.getAttribute();
                String value = pair.getValue();
                try {
                    if (i == 0) return Checker.checkEqualOrGreat(row1.get(attribute), value);
                    if (i == 1) return Checker.checkEqualOrLess(row1.get(attribute), value);
                    if (i == 2) return Checker.checkNotEqual(row1.get(attribute), value);
                    if (i == 3) return Checker.checkEqual(row1.get(attribute), value);
                    if (i == 4) return Checker.checkIlike((String) row1.get(attribute), value);
                    if (i == 5) return Checker.checkLike((String) row1.get(attribute), value);
                    if (i == 6) return Checker.checkGreat(row1.get(attribute), value);
                    return Checker.checkLess(row1.get(attribute), value);
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid datatype in where section " + condition);
                }
            }
        }
        return false;
    }

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