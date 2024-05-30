package com.example.AlexCode.utility;

import com.example.AlexCode.models.Code;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;


public class POJO_JSON_Mapper {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Code code = new Code();
        code.setCode("import java.util.*;\n" +
                "class HelloWorld{\n" +
                "static Scanner sc;public static void main(String[] args){\n" +
                "sc = new Scanner(System.in);\n" +
                "int t=sc.nextInt();\n" +
                "while(t-- > 0){\n" +
                "int n=sc.nextInt();\n" +
                "System.out.println(\"Manas RAI and n is \"+n);\n" +
                "}\n" +
                "}\n" +
                "}");

        code.setInput("4\n2\n69\n96\n21");
        code.setLang_code(1);
        code.setDateTime(LocalDateTime.now());
        System.err.println(code.getCode());
        try {
            String json = mapper.writeValueAsString(code);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

