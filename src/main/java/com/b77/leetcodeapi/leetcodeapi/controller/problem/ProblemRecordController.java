package com.b77.leetcodeapi.leetcodeapi.controller.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemSolveRecord;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeProblemService;
import com.b77.leetcodeapi.leetcodeapi.service.problem.ProblemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/problemrecord")
public class ProblemRecordController {

    @Autowired
    LeetcodeProblemService leetcodeProblemService;

    @Autowired
    ProblemRecordService problemRecordService;


    @GetMapping("/{titleSlug}")
    public ProblemRecord getProblemRecord(
            @PathVariable(name = "titleSlug") String problemTitleSlug,
            @AuthenticationPrincipal UserEntry userEntry){

        //check valid title slug
        ProblemEntry problemEntry =  leetcodeProblemService.getProblemByTitleSlug(problemTitleSlug);
        if(problemEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem does not exists!");
        }

        return problemRecordService.getProblemRecord(userEntry, problemEntry);
    }


    @GetMapping("/allRecord")
    public List<ProblemRecord> getAllProblemRecord(@AuthenticationPrincipal UserEntry userEntry){
        return null;
    }


    @GetMapping("/{titleSlug}/createSolve")
    public ProblemSolveRecord createProblemSolveRecord(
            @PathVariable(name = "titleSlug") String problemTitleSlug,
            @RequestParam(name = "proficiency", required = true) int proficiency,
            @AuthenticationPrincipal UserEntry userEntry){

        //check valid title slug
        ProblemEntry problemEntry =  leetcodeProblemService.getProblemByTitleSlug(problemTitleSlug);
        if(problemEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem does not exists!");
        }

        //check proficiency validity
        if(proficiency < 0 || proficiency > 5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid proficiency!");
        }

        ProblemSolveRecord solveRecord = problemRecordService.createProblemSolveRecord(userEntry, problemEntry, proficiency);

        return solveRecord;
    }



    @GetMapping("/{titleSlug}/allSolves")
    public List<ProblemSolveRecord> getAllProblemSolveRecord(
            @PathVariable(name = "titleSlug") String problemTitleSlug,
            @AuthenticationPrincipal UserEntry userEntry){

        //check valid title slug
        ProblemEntry problemEntry =  leetcodeProblemService.getProblemByTitleSlug(problemTitleSlug);
        if(problemEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem does not exists!");
        }

        List<ProblemSolveRecord> solveRecordList = problemRecordService.getAllProblemSolveRecordByProblem(userEntry, problemEntry);

        if(solveRecordList == null){
            solveRecordList = new ArrayList<>();
        }

        return solveRecordList;
    }




    @GetMapping("/{titleSlug}/updatesolve")
    public ProblemRecord updateProblemSolveRecord(
            @RequestParam(name = "problemRecordSolveId", required = true) long problemRecordSolveID,
            @RequestParam(name = "proficiency", required = true) int proficiency,
            @AuthenticationPrincipal UserEntry userEntry){

        //check valid problem solve record id
        ProblemSolveRecord problemSolveRecord = problemRecordService.getProblemSolveRecordById(problemRecordSolveID);
        if(problemSolveRecord == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid problem solve record id");
        }

        //check current solve record belongs to current user
        if(problemSolveRecord.getProblemRecord().getUserEntry().getId() != userEntry.getId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update problem record of another user!");
        }

        return problemRecordService.updateProblemSolveRecord(problemSolveRecord, userEntry, proficiency);
    }

}
