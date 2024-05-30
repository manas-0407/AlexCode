package com.example.AlexCode.repository;

import com.example.AlexCode.models.ContestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContestRepo extends MongoRepository<ContestEntity , String> {

    @Query("{ 'start' : { $gt: ?0 } }")
    List<ContestEntity> findAllByStartAfter(LocalDateTime currentDateTime);

    @Query("{ 'start' : { $lt: ?0 }, 'end' : { $gt: ?0 } }")
    List<ContestEntity> findAllRunningContests(LocalDateTime currentDateTime);

}
