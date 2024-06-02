package com.example.AlexCode.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashSet;

@Data
public class LeaderBoardEntity {
    @Id
    String username;
    int point;
    Long time; // In min
    HashSet<String> question_done;
}
