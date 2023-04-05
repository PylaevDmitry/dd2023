package com.digdes.school;

import java.util.Arrays;
import java.util.Map;

public class Pair {
    private static final String[] attributes = new String[]{"id", "lastname", "age", "cost", "active"};
    private static final String[] operators = new String[]{">=", "<=", "!=", "=", "ilike", "like", ">", "<"};

    private String attribute;
    private final Object value;
    private final String sign;

    public Pair(String str) throws Exception {
        for (String operator : operators)
            if (str.contains(operator)) {
                sign = operator;
                String[] pair = str.split(sign);
                attribute = pair[0].trim().replace("'", "").toLowerCase();
                if (Arrays.stream(attributes).noneMatch(attribute.toLowerCase()::contains))
                    throw new Exception("Invalid attribute in expression: " + str);
                String valueStr = pair[1].trim();
                if (attribute.equals(attributes[1])) {
                    attribute = "lastName";
                    valueStr = valueStr.replace("'", "");
                }
                if (valueStr.equals("null")) value = null;
                else try {
                    if (attribute.toLowerCase().equals(attributes[0]) || attribute.toLowerCase().equals(attributes[2]))
                        value = Integer.valueOf(valueStr);
                    else if (attribute.toLowerCase().equals(attributes[3])) value = Double.valueOf(valueStr);
                    else if (attribute.toLowerCase().equals(attributes[4])) value = Boolean.valueOf(valueStr);
                    else value = valueStr;
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid datatype in expression: " + str);
                }
                return;
            }
        throw new Exception("Invalid operator in expression: " + str);
    }

    public String getAttribute() {
        return attribute;
    }

    public Object getValue() {
        return value;
    }

    public boolean checkRow(Map<String, Object> row) throws Exception {
        try {
            if (sign.equals(operators[0])) return checkEqualOrGreat(row.get(attribute), value);
            if (sign.equals(operators[1])) return checkEqualOrLess(row.get(attribute), value);
            if (sign.equals(operators[2])) return checkNotEqual(row.get(attribute), value);
            if (sign.equals(operators[3])) return checkEqual(row.get(attribute), value);
            if (sign.equals(operators[4])) return checkIlike(row.get(attribute), value);
            if (sign.equals(operators[5])) return checkLike(row.get(attribute), value);
            if (sign.equals(operators[6])) return checkGreat(row.get(attribute), value);
            return checkLess(row.get(attribute), value);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid datatype in expression: " + this);
        }
    }

    public static boolean checkEqual(Object o1, Object o2) {
        if (o1 == null || o2 == null) return false;
        return String.valueOf(o1).equals(String.valueOf(o2));
    }

    public static boolean checkNotEqual(Object o1, Object o2) {
        if (o1 == null || o2 == null) return true;
        return !String.valueOf(o1).equals(String.valueOf(o2));
    }

    public static boolean checkEqualOrGreat(Object o1, Object o2) throws NumberFormatException {
        if (o1 == null || o2 == null) return false;
        return Double.parseDouble(String.valueOf(o1)) >= Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkEqualOrLess(Object o1, Object o2) throws NumberFormatException {
        if (o1 == null || o2 == null) return false;
        return Double.parseDouble(String.valueOf(o1)) <= Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkGreat(Object o1, Object o2) throws NumberFormatException {
        if (o1 == null || o2 == null) return false;
        return Double.parseDouble(String.valueOf(o1)) > Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkLess(Object o1, Object o2) throws NumberFormatException {
        if (o1 == null || o2 == null) return false;
        return Double.parseDouble(String.valueOf(o1)) < Double.parseDouble(String.valueOf(o2));
    }

    public static boolean checkLike(Object o1, Object o2) {
        if (o1 == null || o2 == null) return false;
        String o1Str = String.valueOf(o1);
        String o2Str = String.valueOf(o2);
        if (o2Str.startsWith("%") && o2Str.endsWith("%")) return o1Str.contains(o2Str.substring(1, o2Str.length() - 1));
        if (o2Str.startsWith("%") && !o2Str.endsWith("%")) return o1Str.endsWith(o2Str.substring(1));
        if (!o2Str.startsWith("%") && o2Str.endsWith("%"))
            return o1Str.startsWith(o2Str.substring(0, o2Str.length() - 1));
        return o1Str.equals(o2Str);
    }

    public static boolean checkIlike(Object o1, Object o2) {
        if (o1 == null || o2 == null) return false;
        String o1Str = String.valueOf(o1);
        String o2Str = String.valueOf(o2);
        return checkLike(o1Str.toLowerCase(), o2Str.toLowerCase());
    }

    @Override
    public String toString() {
        return attribute + sign + value;
    }
}