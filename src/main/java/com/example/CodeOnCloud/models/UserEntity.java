package com.example.CodeOnCloud.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "user_table")
public class UserEntity {

    @Id
    private String id;
    private String username;
    private String password;
    private List<String> questions_prepared;
    private List<String> contest_prepared;

}
