package com.example.AlexCode.Service;

import com.example.AlexCode.models.*;
import com.example.AlexCode.repository.ContestRepo;
import com.example.AlexCode.repository.QuestionRepo;
import com.example.AlexCode.utility.Id_Generator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ContestService {

    @Autowired
    ContestRepo contestRepo;

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    Id_Generator idGenerator;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisTemplate<String , String> redisTemplate;

    @Autowired
    ObjectMapper mapper;

    public String create(ContestEntity contestEntity){

        contestEntity.setId(idGenerator.generate_id(contestEntity.getTitle().trim()));

        if(contestRepo.findById(contestEntity.getId()).isPresent()){
            return "Contest name already used";
        }

        if(contestEntity.getStart().isBefore(LocalDateTime.now()))
            return "Can't create contest as start time passed";

        contestEntity.setLeaderBoard(new ArrayList<>());
        contestEntity.setUser_map(new HashMap<>());
        contestEntity.setCreatedAt(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        contestEntity.setCreatedBy(authentication.getName());

        for(String question_id: contestEntity.getQuestion_ids()){
            Optional<QuestionEntity> entity = questionRepo.findById(question_id);
            if(entity.isPresent()){
                QuestionEntity questionEntity = entity.get();
                questionEntity.setQues_end_time(contestEntity.getEnd());
                questionRepo.save(questionEntity);
            }else return "No such question present with id - " + question_id;
        }
        contestRepo.save(contestEntity);
        return null;
    }

    public String getContest(String contest_id){

        String cached = redisTemplate.opsForValue().get(contest_id);
        if(cached != null){
            return cached;
        }else{

            Optional<ContestEntity> optionalContestEntity = contestRepo.findById(contest_id);
            if(optionalContestEntity.isEmpty()) {
                redisTemplate.opsForValue().set(contest_id , "No Such contest" , 60*60*3 , TimeUnit.SECONDS);
                return "No Such contest";
            }

            ContestEntity contest = optionalContestEntity.get();
            LocalDateTime start_time = contest.getStart();
            LocalDateTime end_time = contest.getEnd();
            if(LocalDateTime.now().isBefore(start_time)) {
                String reply = "Contest not started ";
                redisTemplate.opsForValue().set(contest_id , reply , Duration.between(LocalDateTime.now() , start_time).toSeconds() , TimeUnit.SECONDS);
                return reply;
            }
            else if(LocalDateTime.now().isAfter(end_time)) {
                redisTemplate.opsForValue().set(contest_id , "Contest has ended" , 60*60*3 , TimeUnit.SECONDS);
                return "Contest has ended";
            }
            else {
                String link = "/contest/"+contest_id+"/question";
                String reply = "Contest Running, redirect to "+link;
                redisTemplate.opsForValue().set(contest_id , reply , Duration.between(LocalDateTime.now() , end_time).toSeconds() , TimeUnit.SECONDS);
                return reply;
            }
        }
    }

    public List<QuestionEntity> getContestALLQuestion(String contest_id) throws JsonProcessingException {

        String key = contest_id+"#question";
        String cached = redisTemplate.opsForValue().get(key);

        if(cached != null){ // Redis

            TypeReference<List<QuestionEntity>> typeReference = new TypeReference<>() {};

            return mapper.readValue(cached , typeReference);
        }else{

            Optional<ContestEntity> optionalContestEntity = contestRepo.findById(contest_id);

            if(optionalContestEntity.isEmpty()) {

                String json = mapper.writeValueAsString(List.of());
                redisTemplate.opsForValue().set(key , json , 60*60*3 , TimeUnit.SECONDS);
                return List.of();
            }

            ContestEntity contest = optionalContestEntity.get();
            LocalDateTime start_time = contest.getStart();
            LocalDateTime end_time = contest.getEnd();
            if(LocalDateTime.now().isBefore(start_time)) {
                String json = mapper.writeValueAsString(List.of());
                redisTemplate.opsForValue().set(key , json , Duration.between(LocalDateTime.now() , start_time).toSeconds() , TimeUnit.SECONDS);
                return List.of();
            }
            else if(LocalDateTime.now().isAfter(end_time)) {
                String json = mapper.writeValueAsString(List.of());
                redisTemplate.opsForValue().set(key , json , 60*60*3 , TimeUnit.SECONDS);
                return List.of();
            }
            else {
                List<QuestionEntity> questionEntityList = new ArrayList<>();
                for(String question_id : contest.getQuestion_ids()){
                    Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(question_id);
                    optionalQuestionEntity.ifPresent(questionEntityList::add);
                }

                String json = mapper.writeValueAsString(questionEntityList);
                redisTemplate.opsForValue().set(key , json , Duration.between(LocalDateTime.now() , end_time).toSeconds() , TimeUnit.SECONDS);

                return questionEntityList;
            }
        }
    }

    public QuestionEntity getContestQuestionById(String contest_id,String question_id) throws JsonProcessingException {

        String key = contest_id+"#question#"+question_id;
        String cached = redisTemplate.opsForValue().get(key);
        if (cached!=null){
            if(cached.compareTo("null") == 0) return null;

            return mapper.readValue(cached , QuestionEntity.class);
        }else{

            Optional<ContestEntity> optionalContestEntity = contestRepo.findById(contest_id);

            if(optionalContestEntity.isEmpty()) {
                redisTemplate.opsForValue().set(key , "null" , 60*60*3 , TimeUnit.SECONDS);
                return null;
            }

            ContestEntity contest = optionalContestEntity.get();
            LocalDateTime start_time = contest.getStart();
            LocalDateTime end_time = contest.getEnd();
            if(LocalDateTime.now().isBefore(start_time)) {
                redisTemplate.opsForValue().set(key , "null" , Duration.between(LocalDateTime.now() , start_time).toSeconds() , TimeUnit.SECONDS);
                return null;
            }
            else if(LocalDateTime.now().isAfter(end_time)) {
                redisTemplate.opsForValue().set(key , "null" , 60*60*3 , TimeUnit.SECONDS);
                return null;
            }
            else {
                Optional<QuestionEntity> question = questionRepo.findById(question_id);

                String json = "null";
                if(question.isPresent()){
                    json = mapper.writeValueAsString(question.get());
                }
                redisTemplate.opsForValue().set(key , json , Duration.between(LocalDateTime.now() , end_time).toSeconds() , TimeUnit.SECONDS);
                return question.orElse(null);
            }
        }
    }

    public List<ContestEntity> upcoming_Contest(){
        return contestRepo.findAllByStartAfter(LocalDateTime.now());
    }

    public List<ContestEntity> running_Contest() {
        return contestRepo.findAllRunningContests(LocalDateTime.now());
    }

    public String run_question(String contest_id,
                               String code,String question_id) throws JsonProcessingException {

        Optional<ContestEntity> optionalContestEntity = contestRepo.findById(contest_id);

        if(optionalContestEntity.isEmpty()) return "No such Contest";

        ContestEntity contest = optionalContestEntity.get();

        LocalDateTime start_time = contest.getStart();
        LocalDateTime end_time = contest.getEnd();

        if(LocalDateTime.now().isBefore(start_time))
            return "Contest not started";
        else if(LocalDateTime.now().isAfter(end_time))
            return "Contest has ended";
        else {

            Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(question_id);

            if(optionalQuestionEntity.isPresent()){
                Code code1 = new Code();
                code1.setCode(code);
                code1.setLang_code(1);
                code1.setInput(optionalQuestionEntity.get().getTest_input());
                code1.setDateTime(LocalDateTime.now());


                String json = mapper.writeValueAsString(code1);

//                String url = "http://43.205.181.209:8080/run";
                String url = "http://localhost:8080/run";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");

                HttpEntity<String> request = new HttpEntity<>(json , headers);

                ResponseEntity<Output> response = restTemplate.exchange(url , HttpMethod.POST , request , Output.class);
                String output = String.valueOf(Objects.requireNonNull(response.getBody()).getOutput());

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String current_user = authentication.getName();

                if(!contest.getUser_map().containsKey(current_user)){
                    LeaderBoardEntity leaderBoardEntity = new LeaderBoardEntity();
                    leaderBoardEntity.setUsername(current_user);
                    leaderBoardEntity.setPoint(0);
                    leaderBoardEntity.setTime(0L);
                    leaderBoardEntity.setQuestion_done(new HashSet<>());

                    contest.getLeaderBoard().add(leaderBoardEntity);
                    contest.getUser_map().put(current_user , leaderBoardEntity);
                    contestRepo.save(contest);
                }

                LocalDateTime contest_start_time = contest.getStart();

                if(output.equals(optionalQuestionEntity.get().getTest_output())) {

                    LeaderBoardEntity leaderBoardEntity = contest.getUser_map().get(current_user);

                    if(!leaderBoardEntity.getQuestion_done().contains(question_id)){

                        leaderBoardEntity.getQuestion_done().add(question_id);

                        leaderBoardEntity.setPoint( leaderBoardEntity.getPoint() + 1 );

                        Long time_difference = Math.abs(Duration.between(LocalDateTime.now() , contest_start_time).toSeconds());

                        leaderBoardEntity.setTime(leaderBoardEntity.getTime() + time_difference);
                        contest.getUser_map().put(current_user , leaderBoardEntity);

                        contestRepo.save(contest);
                    }

                    return "Accepted";
                }
                return "Wrong Answer";
            }

            return "No Such Question"; // If Question not present
        }


    }

    public List<LeaderBoardEntity> getPaginatedLeaderBoard(String contest_id , int pageNumber, int pageSize) {

        Optional<ContestEntity> optionalContestEntity = contestRepo.findById(contest_id);
        if(optionalContestEntity.isEmpty()) return null;
        ContestEntity entity = optionalContestEntity.get();
        List<LeaderBoardEntity> list = entity.getLeaderBoard();

        int start_index = pageNumber*pageSize;
        int end_index = Math.min(list.size() , start_index + pageSize);

        if(end_index < start_index) return List.of();

        return IntStream.range(start_index , end_index)
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }

}
