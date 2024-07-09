package com.example.CodeOnCloud.controller;

import com.example.CodeOnCloud.Service.QuestionService;
import com.example.CodeOnCloud.models.MessageDTO;
import com.example.CodeOnCloud.models.QuestionEntity;
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

        QuestionEntity entity = questionService.find(questionId);
        if(entity == null) return null;
        entity.setId("");
        entity.setTest_input("");
        entity.setTest_output("");
        entity.setQues_end_time(null);

        return entity;
    }

}
