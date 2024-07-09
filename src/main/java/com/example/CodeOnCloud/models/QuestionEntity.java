package com.example.CodeOnCloud.models;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(value = "Question_DB")
public class QuestionEntity {

    @Id
    String id;

    @NonNull
    String title;

    LocalDateTime ques_end_time;

    @NonNull
    String statement;

    @NonNull
    String test_input;

    @NonNull
    String test_output;
    LocalDateTime createdAt;
    String createdBy;

}


/*

{
  "title": "Example Question",
  "statement": "This is an example statement.",
  "test_input": "Input for testing",
  "test_output": "Expected output",
}

{
  "title": "Example Question3",
  "statement": "This is an example statement 3",
  "test_input": "4\n2\n69\n96\n21",
  "test_output": "Manas RAI and n is 2\nManas RAI and n is 69\nManas RAI and n is 96\nManas RAI and n is 21\n"
}


 */