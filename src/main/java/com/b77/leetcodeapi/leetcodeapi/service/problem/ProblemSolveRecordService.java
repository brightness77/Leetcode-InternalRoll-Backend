package com.b77.leetcodeapi.leetcodeapi.service.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemSolveRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemSolveRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProblemSolveRecordService {

    @Autowired
    ProblemSolveRecordRepository problemSolveRecordRepository;

    @Autowired
    ProblemRecordService problemRecordService;


    public ProblemSolveRecord getProblemSolveRecordById(long id){
        return problemSolveRecordRepository.getReferenceById(id);
    }



    //old method of no proficiency input
    public ProblemSolveRecord createProblemSolveRecord(ProblemRecord problemRecord){
        return createProblemSolveRecord(problemRecord, 0);
    }

    public ProblemSolveRecord createProblemSolveRecord(UserEntry userEntry, ProblemEntry problemEntry, int proficiency){

        //get problem record first
        ProblemRecord problemRecord = problemRecordService.getProblemRecord(userEntry, problemEntry);

        return createProblemSolveRecord(problemRecord, proficiency);
    }



    public ProblemSolveRecord createProblemSolveRecord(ProblemRecord problemRecord, int proficiency){
        ProblemSolveRecord problemSolveRecord = ProblemSolveRecord.builder()
                .utcStartTime(LocalDateTime.now())
                .utcEndTime(LocalDateTime.now())
                .proficiency(proficiency)
                .problemRecord(problemRecord)
                .build();

        problemSolveRecordRepository.save(problemSolveRecord);

        //always update problem record when modifying problem solve record!
        problemRecordService.updateProblemRecord(problemSolveRecord.getProblemRecord());

        return problemSolveRecord;
    }



    public List<ProblemSolveRecord> getAllProblemSolveRecordOfUserAndProblem(UserEntry userEntry, ProblemEntry problemEntry){
        //get problem record first
        ProblemRecord problemRecord = problemRecordService.getProblemRecord(userEntry, problemEntry);

        return getAllProblemSolveRecordOfProblemRecord(problemRecord);
    }


    public Page<ProblemSolveRecord> getAllProblemSolveRecordOfUserAndProblem(UserEntry userEntry, ProblemEntry problemEntry, int page, int size){
        //get problem record first
        ProblemRecord problemRecord = problemRecordService.getProblemRecord(userEntry, problemEntry);
        Pageable pageable = PageRequest.of(page, size, Sort.by("utcEndTime").descending());

        return problemSolveRecordRepository.getByProblemRecord(problemRecord, pageable);
    }



    public List<ProblemSolveRecord> getAllProblemSolveRecordOfProblemRecord(ProblemRecord problemRecord){
        List<ProblemSolveRecord> problemSolveRecordList = problemSolveRecordRepository.getByProblemRecord(problemRecord);

        return problemSolveRecordList;
    }



    public Page<ProblemSolveRecord> getSolveRecordOfUser(UserEntry userEntry, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("utcEndTime").descending());

        Page<ProblemSolveRecord> problemSolveRecords = problemSolveRecordRepository.getByUser(userEntry.getId(), pageable);

        return problemSolveRecords;
    }


}
