package com.example.AlexCode.controller;

import com.example.AlexCode.Service.QuestionService;
import com.example.AlexCode.models.MessageDTO;
import com.example.AlexCode.models.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/question")
public class QuestionRoute {

    @Autowired
    QuestionService questionService;

    @PostMapping(value = "/create")
    public ResponseEntity<MessageDTO> create(@RequestBody QuestionEntity questionEntity){
        MessageDTO message = new MessageDTO();
        boolean result = questionService.save_question(questionEntity);
        if(result){
            message.setMessage("Question saved");
            message.setError(null);
        }else {
            message.setMessage("Failed to save Question");
            message.setError("Question already present with given Title");
        }

        return ResponseEntity.ok().body(message);
    }

    @GetMapping(value = "/all")
    public List<QuestionEntity> getAllQuestion(){
        return questionService.findQuestionBeforeNow();
    }

    @GetMapping(value = "/id_search/{question_id}")
    public QuestionEntity getbyId(@PathVariable(value = "question_id", required = true) String questionId){
        Optional<QuestionEntity> entityOptional = questionService.find(questionId);
        return entityOptional.orElse(null);
    }

}
