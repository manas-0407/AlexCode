package com.example.CodeOnCloud.Service;

import com.example.CodeOnCloud.models.ContestEntity;
import com.example.CodeOnCloud.models.LeaderBoardEntity;
import com.example.CodeOnCloud.repository.ContestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderBoardService {

    @Autowired
    ContestService contestService;

    @Autowired
    ContestRepo contestRepo;

    @Scheduled( fixedRate = 8000L ) // every 10 minutes
    public void scheduled_sorting() {

        List<ContestEntity> runningContests = contestService.running_Contest();

        for (ContestEntity contest : runningContests) {
            for(LeaderBoardEntity leaderBoard : contest.getLeaderBoard()){
                LeaderBoardEntity map_entity = contest.getUser_map().get(leaderBoard.getUsername());
                leaderBoard.setTime(map_entity.getTime());
                leaderBoard.setPoint(map_entity.getPoint());
                leaderBoard.setQuestion_done(map_entity.getQuestion_done());
            }
            sort_leaderBoard(contest.getLeaderBoard());
            contestRepo.save(contest);
        }
    }

    // Sorts in Descending Order
    private void sort_leaderBoard(List<LeaderBoardEntity> list){

        list.sort((obj1, obj2) -> {
            if (obj1.getPoint() != obj2.getPoint()) return -obj1.getPoint() + obj2.getPoint();
            return obj1.getTime().compareTo(obj2.getTime());
        });

    }
}

