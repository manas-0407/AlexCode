package com.example.AlexCode.controller;

import com.example.AlexCode.Service.UserService;
import com.example.AlexCode.models.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserRoute {

    @Autowired
    UserService userService;

    @GetMapping(value = "/questions")
    public List<QuestionEntity> getQuestionForUser(){

        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userService.getQuestions(username);
    }

}
