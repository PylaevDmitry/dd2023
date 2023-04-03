package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> table = new ArrayList<>();

    //Дефолтный конструктор
    public JavaSchoolStarter() {

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        String[] parseRequest = parseRequest(request);
        Operator operator;
        try {
            operator = Operator.valueOf(parseRequest[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Syntax error in expression " + parseRequest[0]);
        }
        Map<String, Object> row = createRow(parseRequest[1]);
        Condition condition = new Condition(parseRequest[2]);
        List<Map<String, Object>> result = new ArrayList<>();

        switch (operator) {
            case INSERT -> {
                table.add(row);
                result.add(row);
            }
            case UPDATE -> {
                result = condition.searchRows(table);
                for (Map<String, Object> tableItem : table) {
                    for (Map<String, Object> entry : result) {
                        if (tableItem.equals(entry)) tableItem.putAll(row);
                    }
                }
            }
            case SELECT -> result = condition.searchRows(table);
            case DELETE -> {
                if (condition.isEmpty()) {
                    result = table;
                    table.clear();
                }
                else {
                    result = condition.searchRows(table);
                    for (Map<String, Object> entry : result) {
                        table.removeIf(entry::equals);
                    }
                }
            }
        }
        return result;
    }

    //Разбор входной строки
    public static String[] parseRequest(String request) {
        String[] result = new String[3];
        result[0] = request.substring(0, 6);
        request = request.substring(6).trim();
        if (request.toUpperCase().startsWith("VALUES")) request = request.substring(6);
        String[] message = request.split("where|WHERE");
        result[1] = message[0];
        result[2] = (message.length > 1) ? message[1] : "";
        return result;
    }

    //Парсинг атрибутов на входе и их значений
    public static Map<String, Object> createRow(String stringRow) throws Exception {
        Map<String, Object> row = new HashMap<>();
        if (stringRow.equals("")) return row;
        String[] rowArr = stringRow.split(",");
        for (String strRow : rowArr) {
            Relation relation = new Relation(strRow,"=");
            if (relation.getValue().equals("null")) {
                row.put(relation.getAttribute(), null);
                return row;
            }
            row.put(relation.getAttribute(), relation.getValueObject());
        }
        return row;
    }
}
