package com.example.AlexCode.Service;

import com.example.AlexCode.models.QuestionEntity;
import com.example.AlexCode.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    QuestionRepo questionRepo;

    public List<QuestionEntity> getQuestions(String username){

        return questionRepo.findAllByCreatedBy(username);
    }

}
