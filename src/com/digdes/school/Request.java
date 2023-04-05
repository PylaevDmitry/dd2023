package com.digdes.school;

import java.util.*;

@SuppressWarnings({"unused"})
public enum Request {
    INSERT {
        @Override
        public List<Map<String, Object>> apply(List<Map<String, Object>> table) {
            List<Map<String, Object>> result = new ArrayList<>();
            table.add(row);
            result.add(row);
            return result;
        }
    },
    UPDATE {
        @Override
        public List<Map<String, Object>> apply(List<Map<String, Object>> table) throws Exception {
            List<Map<String, Object>> result = whereSection.searchRows(table);
            for (Map<String, Object> tableItem : table) {
                for (Map<String, Object> entry : result) if (tableItem.equals(entry)) tableItem.putAll(row);
            }
            return result;
        }
    },
    DELETE {
        @Override
        public List<Map<String, Object>> apply(List<Map<String, Object>> table) throws Exception {
            List<Map<String, Object>> result;
            if (whereSection.isEmpty()) {
                result = table;
                table.clear();
            }
            else {
                result = whereSection.searchRows(table);
                for (Map<String, Object> entry : result) table.removeIf(entry::equals);
            }
            return result;
        }
    },
    SELECT {
        @Override
        public List<Map<String, Object>> apply(List<Map<String, Object>> table) throws Exception {
            return whereSection.searchRows(table);
        }
    };

    private static Map<String, Object> row;
    private static WhereSection whereSection;

    public static Request parseRequest(String request) throws Exception {
        Request requestType;
        try {
            requestType = Request.valueOf(request.substring(0, 6).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Syntax error in expression " + request.substring(0, 6));
        }
        request = request.substring(6).trim();
        if (request.toUpperCase().startsWith("VALUES")) request = request.substring(6).trim();
        String[] arr = request.split("where|WHERE");
        row = parseRow(arr[0]);
        whereSection = new WhereSection((arr.length > 1) ? arr[1] : "");
        return requestType;
    }

    public static Map<String, Object> parseRow(String rowsStr) throws Exception {
        Map<String, Object> row = new HashMap<>();
        if (rowsStr.equals("")) return row;
        String[] rowsArr = rowsStr.split(",");
        for (String strRow : rowsArr) {
            Pair pair = new Pair(strRow,"=");
            if (pair.getValue().equals("null")) {
                row.put(pair.getAttribute(), null);
                return row;
            }
            row.put(pair.getAttribute(), pair.getValueAsObject());
        }
        return row;
    }

    public List<Map<String, Object>> apply(List<Map<String, Object>> table) throws Exception {
        return table;
    }
}