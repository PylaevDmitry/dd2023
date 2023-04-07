package com.digdes.school;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "UnusedAssignment"})
public class Main {
    public static void main(String... args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            //Вставка строки в коллекцию
            List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true");
            //Изменение значения которое выше записывали
            List<Map<String,Object>> result2 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
            //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
            List<Map<String,Object>> result3 = starter.execute("SELECT");

            List<Map<String, Object>> result;

            //INSERT
            result = starter.execute("iNSERT VALUES 'lastName' = 'Петров', 'id'=1, 'age'=30, 'cost'=5.4, 'active'=true");
            result = starter.execute("INSERT VALUES 'lastName'= 'Иванов' , 'id'=2, 'aGe'=25, 'cost'=4.3, 'active'=false");
            result = starter.execute("INSERT VALUES 'LastName' ='Федоров' , 'id' = 3, 'age'=40, 'Active'=true where 'id' = 3");

            //INSERT EXCEPTIONS
//            result = starter.execute("INSERT VALUES 'lastN'='Федоров'");
//            result = starter.execute("INЕERT VALUES 'lastName'='Федоров'");
//            result = starter.execute("INSERT VALUES 'lastName'='Федоров', 'age'='11'");
//            result = starter.execute("INSERT VALUES 'lastName'='Петров', 'age'=один");
//            result = starter.execute("INSERT VALUES 'LastName' > 'Иванов'");

            //SELECT
            result = starter.execute("SELECT WHERE 'active'=true");
            result = starter.execute("SELECT WHERE 'age'>30 OR 'active'=false");
            result = starter.execute("SELECT WHERE 'id'<=30 and 'lastName' ilike '%п%'");
            result = starter.execute("SELECT");
            result = starter.execute("SELECT WHERE 'id'>=30");

            //SELECT EXCEPTIONS
//            result = starter.execute("SELECT WHERE 'lastname'>10");
//            result = starter.execute("SELECT WHERE 'id' like 10");
//            result = starter.execute("SELECT WHERE 'lastN'='Федоров'");
//            result = starter.execute("SELECT WHERE 'cost'<='10.1'");
//            result = starter.execute("SELECT WHERE 'cost'-10.1");

            //UPDATE
            result = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.3 where 'id'=3");
            result = starter.execute("UPDATE VALUES 'active'=true where 'Active'=false");
            result = starter.execute("UPDATE VALUES 'lastName' = 'Попов' , 'id'=5, 'age'=null, 'active'=false WHERE 'id'=1");

            result = starter.execute("SELECT WHERE 'active'=false");
            result = starter.execute("SELECT WHERE 'age'=null");

            result = starter.execute("UPDATE VALUES 'age'=21 WHERE 'id'=5");

            //DELETE
            result = starter.execute("DELETE WHERE 'id'=3");
            result = starter.execute("DELETE");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
