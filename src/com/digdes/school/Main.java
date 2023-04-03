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
            String i1 = "iNSERT VALUES 'lastName' = 'Петров', 'id'=1, 'age'=30, 'cost'=5.4, 'active'=true";
            String i2 = "INSERT VALUES 'lastName'= 'Иванов' , 'id'=2, 'aGe'=25, 'cost'=4.3, 'active'=false";
            String i3 = "INSERT VALUES 'LastName' ='Федоров' , 'id' = 3, 'age'=40, 'Active'=true";

            result = starter.execute(i1);
            result = starter.execute(i2);
            result = starter.execute(i3);

            //INSERT EXCEPTIONS
            String ie1 = "INSERT VALUES 'lastN'='Федоров'";
            String ie2 = "INЕERT VALUES 'lastName'='Федоров'";
            String ie3 = "INSERT VALUES 'lastName'='Федоров', 'age'='11'";

//            result = starter.execute(ie1);
//            result = starter.execute(ie2);
//            result = starter.execute(ie3);

            //SELECT
            String s1 = "SELECT WHERE 'active'=true";
            String s2 = "SELECT WHERE 'age'>30 OR 'active'=false";
            String s3 = "SELECT WHERE 'id'<=30 and 'lastName' ilike '%п%'";
            String s4 = "SELECT";
            String s5 = "SELECT WHERE 'id'>=30";

            result = starter.execute(s1);
            result = starter.execute(s2);
            result = starter.execute(s3);
            result = starter.execute(s4);
            result = starter.execute(s5);

            //SELECT EXCEPTIONS
            String se1 = "SELECT WHERE 'lastname'>10";
            String se2 = "SELECT WHERE 'lastN'='Федоров'";
            String se3 = "SELECT WHERE 'cost'<='10.1'";

//            result = starter.execute(se1);
//            result = starter.execute(se2);
//            result = starter.execute(se3);

            //UPDATE
            String u1 = "UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3";
            String u2 = "UPDATE VALUES 'active'=true where 'Active'=false";
            String u3 = "UPDATE VALUES 'lastName' = 'Попов' , 'id'=5, 'age'=null, 'active'=true WHERE 'id'=1";

            result = starter.execute(u1);
            result = starter.execute(u2);
            result = starter.execute(u3);

            result = starter.execute(s1);

            //DELETE
            String d1 = "DELETE WHERE 'id'=3";
            String d2 = "DELETE";

            result = starter.execute(d1);
            result = starter.execute(d2);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
