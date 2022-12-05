package com.b77.leetcodeapi.leetcodeapi.service.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemSolveRecord;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemSolveRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

        return problemSolveRecord;
    }



    public List<ProblemSolveRecord> getAllProblemSolveRecordOfProblem(UserEntry userEntry, ProblemEntry problemEntry){
        //get problem record first
        ProblemRecord problemRecord = problemRecordService.getProblemRecord(userEntry, problemEntry);

        return getAllProblemSolveRecordOfProblem(problemRecord);
    }




    public List<ProblemSolveRecord> getAllProblemSolveRecordOfProblem(ProblemRecord problemRecord){
        List<ProblemSolveRecord> problemSolveRecordList = problemSolveRecordRepository.getByProblemRecord(problemRecord);

        return problemSolveRecordList;
    }

}
