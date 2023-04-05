package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> table = new ArrayList<>();

    //Дефолтный конструктор
    public JavaSchoolStarter() {

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        Request requestObj = Request.parseRequest(request);
        return requestObj.apply(table);
    }
}
