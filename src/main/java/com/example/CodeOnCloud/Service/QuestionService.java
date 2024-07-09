package com.example.CodeOnCloud.Service;

import com.example.CodeOnCloud.models.QuestionEntity;
import com.example.CodeOnCloud.models.UserEntity;
import com.example.CodeOnCloud.repository.QuestionRepo;
import com.example.CodeOnCloud.repository.UserRepository;
import com.example.CodeOnCloud.utility.Id_Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    Id_Generator idGenerator;

    public boolean save_question(QuestionEntity entity){

        String gen_ques_id = idGenerator.generate_id(entity.getTitle().trim());
        entity.setId(gen_ques_id);

        if(questionRepo.findById(entity.getId()).isPresent()) return false;

        entity.setQues_end_time(LocalDateTime.of(2050 , 12 , 31 , 5 , 30));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy(SecurityContextHolder.getContext()
                .getAuthentication().getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> userEntity = userRepository.findByUsername(authentication.getName());

        if (userEntity.isPresent()){
            UserEntity userEntity1 = userEntity.get();
            userEntity1.getQuestions_prepared().add(gen_ques_id);
            userRepository.save(userEntity1);
        }

        questionRepo.save(entity);

        return true;
    }

    public List<QuestionEntity> findQuestionBeforeNow(){
        return questionRepo.findAllByQuesEndTimeBefore(LocalDateTime.now());
    }

    public QuestionEntity find(String questionId) {

        Optional<QuestionEntity> entity = questionRepo.findById(questionId);
        if (entity.isEmpty()) return null;

        if (entity.get().getQues_end_time().isBefore(LocalDateTime.now())) return entity.get();
        return null;
    }
}
