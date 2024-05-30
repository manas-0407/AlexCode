package com.example.AlexCode.controller;

import com.example.AlexCode.Service.ContestService;
import com.example.AlexCode.models.ContestEntity;
import com.example.AlexCode.models.LeaderBoardEntity;
import com.example.AlexCode.models.MessageDTO;
import com.example.AlexCode.models.QuestionEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/contest")
public class ContestRoute {

    @Autowired
    ContestService contestService;

    @PostMapping(value = "/create")
    public ResponseEntity<MessageDTO> create(@RequestBody ContestEntity contestEntity){
        String result = contestService.create(contestEntity);
        MessageDTO message = new MessageDTO();
        if(result != null) message.setMessage("Failed to create contest");
        else message.setMessage("Contest Created");

        message.setError(result);
        return ResponseEntity.ok().body(message);
    }

    @GetMapping(value = "/{contest_id}")
    public String getContest(@PathVariable(value = "contest_id") String contest_id){
        return contestService.getContest(contest_id);
    }

    @GetMapping(value = "/{contest_id}/question")
    public List<QuestionEntity> getContestAllQuestion(@PathVariable(value = "contest_id") String contest_id){

        return contestService.getContestALLQuestion(contest_id);
    }

    @GetMapping(value = "/{contest_id}/question/{question_id}")
    public ResponseEntity<QuestionEntity> getContestQuestionId(@PathVariable(value = "contest_id") String contest_id,
                                                               @PathVariable(value = "question_id") String question_id){

        return ResponseEntity.ok(contestService.getContestQuestionById(contest_id,question_id));
    }

    @PostMapping(value = "/{contest_id}/question/{question_id}/submit")
    public ResponseEntity<String> sumbit(@PathVariable(value = "contest_id") String contest_id,
                                         @PathVariable(value = "question_id") String question_id,
                                         @RequestBody String code) throws JsonProcessingException {
        // Check if under time or not

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        code = mapper.readValue(code , String.class);

        String submit_response = contestService.run_question(contest_id,code , question_id);

        return ResponseEntity.ok(submit_response);
    }

    @GetMapping(value = "/{contest_id}/leaderboard")
    public List<LeaderBoardEntity> getPaginatedLeaderBoard(
            @PathVariable(value = "contest_id") String contest_id,
            @RequestParam(value = "pageNumber" , defaultValue = "0" , required = false) int pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "100" , required = false) int pageSize
    ){

        return contestService.getPaginatedLeaderBoard(contest_id , pageNumber , pageSize);
    }

    @GetMapping( value = "/upcoming")
    public List<ContestEntity> getUpcomingContest(){
        return contestService.upcoming_Contest();
    }

    @GetMapping( value = "/running")
    public List<ContestEntity> getRunningContest(){
        return contestService.running_Contest();
    }
}
