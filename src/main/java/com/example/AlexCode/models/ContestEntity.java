package com.example.AlexCode.models;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
@Document(value = "Contest_DB")
public class ContestEntity {

    @Id
    String id;

    @NonNull
    String Title;

    @NonNull
    LocalDateTime start;

    @NonNull
    LocalDateTime end;

    @NonNull
    List<String> question_ids;

    List<LeaderBoardEntity> leaderBoard;

    HashMap<String, LeaderBoardEntity> user_map;

    LocalDateTime createdAt;

    String createdBy;
}

/*

{

  "Title": "Example Contest",
  "start": "2024-05-17T08:00:00",
  "end": "2024-05-17T12:00:00",
  "question_ids": ["1", "2", "3"],

}

 */