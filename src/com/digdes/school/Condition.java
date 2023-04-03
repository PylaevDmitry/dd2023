package com.digdes.school;

import java.util.*;

public class Condition {
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

    private final String[] conditions;
    private final String[] sequence;

    public Condition(String strWhere) {
        this.conditions = strWhere.split("AND|OR|and|or");
        
        strWhere = strWhere.toUpperCase();
        List<String> sequenceList = new ArrayList<>();
        while (strWhere.contains("AND") && strWhere.contains("OR")) {
            if (strWhere.indexOf("AND") < strWhere.indexOf("OR")) {
                sequenceList.add("AND");
                strWhere = strWhere.substring(strWhere.indexOf("AND") + 3);
            }
            if (strWhere.indexOf("AND") > strWhere.indexOf("OR")) {
                sequenceList.add("OR");
                strWhere = strWhere.substring(strWhere.indexOf("OR") + 2);
            }
        }
        while (strWhere.contains("AND")) {
            sequenceList.add("AND");
            strWhere = strWhere.substring(strWhere.indexOf("AND") + 3);
        }
        while (strWhere.contains("OR")) {
            sequenceList.add("OR");
            strWhere = strWhere.substring(strWhere.indexOf("OR") + 2);
        }
        this.sequence = sequenceList.toArray(sequenceList.toArray(new String[0]));
    }

    public boolean isEmpty() {
        return conditions[0].equals("");
    }

    public List<Map<String, Object>> searchRows(List<Map<String, Object>> table) throws Exception {
        if (Objects.equals(conditions[0], "")) return table;
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> entry : table) {
            if (checkSequenceConditions(entry)) result.add(entry);
        }
        return result;
    }

    private boolean checkSequenceConditions(Map<String, Object> row1) throws Exception {
        if (sequence.length == 0) return checkSingleCondition(row1, conditions[0]);
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i].equalsIgnoreCase("OR")) {
                if (checkSingleCondition(row1, conditions[i]) || checkSingleCondition(row1, conditions[i + 1])) return true;
            }
            if (sequence[i].equalsIgnoreCase("AND")) {
                if (checkSingleCondition(row1, conditions[i]) && checkSingleCondition(row1, conditions[i + 1])) return true;
            }
        }
        return false;
    }

    private boolean checkSingleCondition(Map<String, Object> row1, String condition) throws Exception {
        for (int i = 0; i < equalOperators.length; i++) {
            String equalOperator = equalOperators[i];
            if (condition.contains(equalOperator)) {
                Relation relation = new Relation(condition, equalOperator);
                String attribute = relation.getAttribute();
                String value = relation.getValue();
                try {
                    if (i == 0) return ValueChecker.checkEqualOrGreat(row1.get(attribute), value);
                    if (i == 1) return ValueChecker.checkEqualOrLess(row1.get(attribute), value);
                    if (i == 2) return ValueChecker.checkNotEqual(row1.get(attribute), value);
                    if (i == 3) return ValueChecker.checkEqual(row1.get(attribute), value);
                    if (i == 4) return ValueChecker.checkIlike((String) row1.get(attribute), value);
                    if (i == 5) return ValueChecker.checkLike((String) row1.get(attribute), value);
                    if (i == 6) return ValueChecker.checkGreat(row1.get(attribute), value);
                    return ValueChecker.checkLess(row1.get(attribute), value);
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid operator in where expression: " + condition);
                }
            }
        }
        return false;
    }
}
