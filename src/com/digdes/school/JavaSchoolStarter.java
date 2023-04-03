package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private static final String[] attributes = new String[] {
            "id",
            "lastname",
            "age",
            "cost",
            "active"
    };

    private static final String[] equalOperators = new String[] {
            ">=",
            "<=",
            "!=",
            "=",
            "ilike",
            "like",
            ">",
            "<"
    };

    private final List<Map<String,Object>> table = new ArrayList<>();

    //Дефолтный конструктор
    public JavaSchoolStarter(){

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String,Object>> execute(String request) throws Exception {
        String[] strArr = parseInput(request);
        Operator operator;
        try {
            operator = Operator.valueOf(strArr[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Syntax error in expression " + strArr[0]);
        }
        Map<String,Object> row = createRow(strArr[1]);
        String[] conditions = buildCondition(strArr[2]);
        String[] checkSeq = buildCheckSeq(strArr[2]);
        List<Map<String,Object>> result = new ArrayList<>();
        switch (operator) {
            case INSERT -> {
                table.add(row);
                result.add(row);
            }
            case UPDATE -> {
                result = search(conditions, checkSeq);
                for (Map<String,Object> item:table) {
                    for (Map<String,Object> entry:result) {
                        if (item.equals(entry)) item.putAll(row);
                    }
                }
            }
            case SELECT -> result = search(conditions, checkSeq);
            case DELETE -> {
                result = search(conditions, checkSeq);
                if (result.equals(table)) table.clear();
                for (Map<String,Object> item:result) {
                    table.removeIf(item::equals);
                }
            }
        }
        return result;
    }

    //Разбор входной строки
    private String[] parseInput(String request) {
        String[] result = new String[3];
        result[0] = request.substring(0, 6);
        request = request.substring(6).trim();
        if (request.toUpperCase().startsWith("VALUES")) request = request.substring(6);
        String[] message = request.split("where|WHERE");
        result[1] = message[0];
        result[2] = (message.length>1)?message[1]:"";
        return result;
    }

    //Парсинг атрибутов в запросе и их значений
    private Map<String,Object> createRow (String stringRow) throws Exception {
        Map<String,Object> row = new HashMap<>();
        if (stringRow.equals("")) return row;
        String[] dataArr = stringRow.split(",");
        for (String item : dataArr) {
            String attribute = item.substring(0, item.indexOf("=")).trim().replace("'", "").toLowerCase();
            String str;
            Object value;
            String trim = item.substring(item.indexOf("=") + 1).trim();
            if (attribute.equals(attributes[1])) {
                attribute = "lastName";
                str = trim.replace("'", "");
            }
            else str = trim;
            if (str.equals("null")) {
                row.put(attribute, null);
                return row;
            }
            try {
                if (attribute.toLowerCase().equals(attributes[0]) || attribute.toLowerCase().equals(attributes[2])) value = Integer.valueOf(str);
                else if (attribute.toLowerCase().equals(attributes[3])) value = Double.valueOf(str);
                else if (attribute.toLowerCase().equals(attributes[4])) value = Boolean.valueOf(str);
                else if (attribute.toLowerCase().equals(attributes[1])) value = str;
                else throw new Exception("Invalid attribute insert");
            } catch (NumberFormatException e) {
                throw new Exception("Invalid datatype insert");
            }
            row.put(attribute, value);
        }
        return row;
    }

    //Массив из условий, которым должены соответствовать записи в таблице
    private String[] buildCondition(String strWhere) {
        return strWhere.split("AND|OR|and|or");
    }

    //Массив логических операторов в условии выборки
    private String[] buildCheckSeq(String strWhere) {
        strWhere = strWhere.toUpperCase();
        List<String> res = new ArrayList<>();
        while (strWhere.contains("AND") && strWhere.contains("OR")) {
            if (strWhere.indexOf("AND") < strWhere.indexOf("OR")) {
                res.add("AND");
                strWhere = strWhere.substring(strWhere.indexOf("AND") + 3);
            }
            if (strWhere.indexOf("AND") > strWhere.indexOf("OR")) {
                res.add("OR");
                strWhere = strWhere.substring(strWhere.indexOf("OR") + 2);
            }
        }
        while (strWhere.contains("AND")) {
            res.add("AND");
            strWhere = strWhere.substring(strWhere.indexOf("AND") + 3);
        }
        while (strWhere.contains("OR")) {
            res.add("OR");
            strWhere = strWhere.substring(strWhere.indexOf("OR") + 2);
        }
        return res.toArray(res.toArray(new String[0]));
    }

    private boolean checkCondition(Map<String,Object> row1, String condition) throws Exception {
        for (int i = 0; i < equalOperators.length; i++) {
            String equalOperator = equalOperators[i];
            if (condition.contains(equalOperator)) {
                String[] arr = condition.split(equalOperator);
                String attribute = arr[0].trim().replace("'", "").toLowerCase();
                String value;
                if (attribute.equals(attributes[1])) {
                    attribute = "lastName";
                    value = arr[1].trim().replace("'", "");
                }
                else value = arr[1].trim();
                if (Arrays.stream(attributes).noneMatch(attribute.toLowerCase()::contains)) throw new Exception("Invalid attribute in where section: " + attribute);
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
                    throw new Exception("Error in expression" + condition);
                }
            }
        }
        return false;
    }

    private boolean checkSeq (Map<String,Object> row1, String[] conditions, String[] checkSeq) throws Exception {
        if (checkSeq.length == 0) return checkCondition(row1, conditions[0]);
        for (int i = 0; i < checkSeq.length; i++) {
            if (checkSeq[i].equalsIgnoreCase("OR")) {
                if (checkCondition(row1, conditions[i]) || checkCondition(row1, conditions[i+1])) return true;
            }
            if (checkSeq[i].equalsIgnoreCase("AND")) {
                if (checkCondition(row1, conditions[i]) && checkCondition(row1, conditions[i+1])) return true;
            }
        }
        return false;
    }

    private List<Map<String,Object>> search (String[] conditions, String[] checkSeq) throws Exception {
        if (Objects.equals(conditions[0], "")) return table;
        List<Map<String,Object>> result = new ArrayList<>();
        for (Map<String,Object> entry:table) {
            if (checkSeq(entry, conditions, checkSeq)) result.add(entry);
        }
        return result;
    }
}
