package com.digdes.school;

import java.util.*;

public class WhereSection {
    private final String[] conditions;
    private final String[] sequence;

    public WhereSection(String str) {
        this.conditions = str.split("AND|OR|and|or");
        this.sequence = buildCheckSequence(str.toUpperCase());
    }

    public static String[] buildCheckSequence(String str) {
        List<String> sequenceList = new ArrayList<>();
        while (str.contains("AND") && str.contains("OR")) {
            if (str.indexOf("AND") < str.indexOf("OR")) {
                sequenceList.add("AND");
                str = str.substring(str.indexOf("AND") + 3);
            }
            if (str.indexOf("AND") > str.indexOf("OR")) {
                sequenceList.add("OR");
                str = str.substring(str.indexOf("OR") + 2);
            }
        }
        while (str.contains("AND")) {
            sequenceList.add("AND");
            str = str.substring(str.indexOf("AND") + 3);
        }
        while (str.contains("OR")) {
            sequenceList.add("OR");
            str = str.substring(str.indexOf("OR") + 2);
        }
        return sequenceList.toArray(sequenceList.toArray(new String[0]));
    }

    public boolean isEmpty() {
        return conditions[0].equals("");
    }

    public List<Map<String, Object>> searchRows(List<Map<String, Object>> table) throws Exception {
        if (isEmpty()) return table;
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> entry : table) {
            if (checkSequenceConditions(entry)) result.add(entry);
        }
        return result;
    }

    private boolean checkSequenceConditions(Map<String, Object> row1) throws Exception {
        if (sequence.length == 0) return new Pair(conditions[0]).checkRow(row1);
        for (int i = 0; i < sequence.length; i++) {
            boolean current = new Pair(conditions[i]).checkRow(row1);
            boolean next = new Pair(conditions[i + 1]).checkRow(row1);
            if (sequence[i].equalsIgnoreCase("OR")) if (current || next) return true;
            if (sequence[i].equalsIgnoreCase("AND")) if (current && next) return true;
        }
        return false;
    }
}