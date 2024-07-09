package com.example.CodeOnCloud.Service;

import com.example.CodeOnCloud.models.ContestEntity;
import com.example.CodeOnCloud.models.QuestionEntity;
import com.example.CodeOnCloud.models.UserEntity;
import com.example.CodeOnCloud.repository.ContestRepo;
import com.example.CodeOnCloud.repository.QuestionRepo;
import com.example.CodeOnCloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    ContestRepo contestRepo;

    public List<QuestionEntity> getQuestions(String username){

        Optional<UserEntity> entity = userRepository.findByUsername(username);
        if (entity.isPresent()) {
            UserEntity userEntity = entity.get();
            List<QuestionEntity> list = new ArrayList<>();
            for(String ques_id : userEntity.getQuestions_prepared()){
                list.add(questionRepo.findById(ques_id).orElse(null));
            }
            return list;
        }
        return null;
    }

    public List<ContestEntity> getContests(String username) {

        Optional<UserEntity> entity = userRepository.findByUsername(username);
        if (entity.isPresent()) {
            UserEntity userEntity = entity.get();
            List<ContestEntity> list = new ArrayList<>();
            for(String contest_id : userEntity.getContest_prepared()){
                list.add(contestRepo.findById(contest_id).orElse(null));
            }
            return list;
        }
        return null;
    }
}
