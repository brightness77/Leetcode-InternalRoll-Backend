package com.b77.leetcodeapi.leetcodeapi.service.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.tag.TopicRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.tag.TopicTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TopicTagService {

    @Autowired
    TopicTagRepository topicTagRepository;

    @Autowired
    TopicRecordRepository topicRecordRepository;



    public List<TopicTag> getAllTopicTags(){
        return null;
    }

    public boolean isValidTopicTagName(String name){
        TopicTag topicTag =  topicTagRepository.getByName(name);
        return topicTag != null;
    }


    public TopicTag getTopicTagByName(String name){
        return topicTagRepository.getByName(name);
    }


    public TopicRecord getTopicRecordByUserEntryAndTopicTag(UserEntry userEntry, TopicTag topicTag){
        return topicRecordRepository.getByUserEntryAndTopicTag(userEntry, topicTag);
    }

    public TopicRecord updateTopicRecord(UserEntry userEntry, TopicTag topicTag, List<ProblemRecord> problemRecordList){

        //first create all topic record if not exists
        this.createAllTopicRecordForUser(userEntry);

        TopicRecord topicRecord = this.getTopicRecordByUserEntryAndTopicTag(userEntry, topicTag);

        //set total problems
        topicRecord.setTotalProblems(problemRecordList.size());

        //total accepted and proficiency
        long totalAC = 0L;
        long totalProf = 0;
        for(ProblemRecord problemRecord: problemRecordList){
            totalAC += problemRecord.getAcceptedCount();
            totalProf += problemRecord.getRecentProficiency();
        }

        topicRecord.setTotalAC(totalAC);
        topicRecord.setAverageProficiency((double)totalProf / problemRecordList.size());

        topicRecordRepository.save(topicRecord);

        return topicRecord;

    }


    public List<TopicRecord> getAllByUserEntry(UserEntry userEntry){
        return topicRecordRepository.getByUserEntry(userEntry);
    }


    public Page<TopicRecord> getAllByUserEntry(UserEntry userEntry, int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("topicTag.name").ascending());

        return topicRecordRepository.getByUserEntryAndTotalProblemsGreaterThan(userEntry, 0, pageable);
    }



    //================== PRIVATE METHODS ====================


    private void createAllTopicRecordForUser(UserEntry userEntry){

        //already did check
        List<TopicRecord> topicRecordList = this.getAllByUserEntry(userEntry);
        if(topicRecordList != null && !topicRecordList.isEmpty()){
            return;
        }

        List<TopicTag> topicTagList = topicTagRepository.findAll();

        for(TopicTag topicTag: topicTagList){
            TopicRecord topicRecord = topicRecordRepository.getByUserEntryAndTopicTag(userEntry, topicTag);

            if(topicRecord == null) {
                topicRecord = TopicRecord.builder()
                        .totalProblems(0)
                        .totalAC(0)
                        .averageProficiency(0.0)
                        .userEntry(userEntry)
                        .topicTag(topicTag)
                        .build();

                topicRecordRepository.save(topicRecord);
            }
        }
    }




}
