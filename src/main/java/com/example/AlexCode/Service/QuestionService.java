package com.example.AlexCode.Service;

import com.example.AlexCode.models.QuestionEntity;
import com.example.AlexCode.repository.QuestionRepo;
import com.example.AlexCode.utility.Id_Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    Id_Generator idGenerator;

    public boolean save_question(QuestionEntity entity){

        entity.setId(idGenerator.generate_id(entity.getTitle().trim()));

        if(questionRepo.findById(entity.getId()).isPresent()) return false;

        entity.setQues_end_time(LocalDateTime.of(2050 , 12 , 31 , 5 , 30));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        questionRepo.save(entity);

        return true;
    }

    public List<QuestionEntity> findQuestionBeforeNow(){
        return questionRepo.findAllByQuesEndTimeBefore(LocalDateTime.now());
    }

    public Optional<QuestionEntity> find(String questionId) {
        return questionRepo.findById(questionId);
    }
}
