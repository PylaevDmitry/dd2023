package com.digdes.school;

import java.util.Arrays;

public class Relation {
    private static final String[] attributes = new String[]{
            "id",
            "lastname",
            "age",
            "cost",
            "active"
    };

    private String attribute;
    private String value;

    public Relation(String str, String sign) throws Exception {
        String[] pair = str.split(sign);
        attribute = pair[0].trim().replace("'", "").toLowerCase();
        if (Arrays.stream(attributes).noneMatch(attribute.toLowerCase()::contains))
            throw new Exception("Invalid attribute in where section: " + attribute);
        value = pair[1].trim();
        if (attribute.equals(attributes[1])) {
            attribute = "lastName";
            value = value.replace("'", "");
        }
    }

    public String getAttribute() {
        return attribute;
    }
    public String getValue() {
        return value;
    }

    public Object getValueObject() throws Exception {
        Object valueAsObject;
        try {
            if (attribute.toLowerCase().equals(attributes[0]) || attribute.toLowerCase().equals(attributes[2]))
                valueAsObject = Integer.valueOf(value);
            else if (attribute.toLowerCase().equals(attributes[3])) valueAsObject = Double.valueOf(value);
            else if (attribute.toLowerCase().equals(attributes[4])) valueAsObject = Boolean.valueOf(value);
            else if (attribute.toLowerCase().equals(attributes[1])) valueAsObject = value;
            else throw new Exception("Invalid attribute insert");
        } catch (NumberFormatException e) {
            throw new Exception("Invalid datatype insert");
        }
        return valueAsObject;
    }
}
