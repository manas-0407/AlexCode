package com.example.AlexCode.repository;

import com.example.AlexCode.models.QuestionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionRepo extends MongoRepository<QuestionEntity , String> {

    @Query("{ 'ques_end_time' : { $lt: ?0 } }")
    List<QuestionEntity> findAllByQuesEndTimeBefore(LocalDateTime currentDateTime);

    List<QuestionEntity> findAllByCreatedBy(String username);

}
