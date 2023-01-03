package com.b77.leetcodeapi.leetcodeapi.controller.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemSolveRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeProblemService;
import com.b77.leetcodeapi.leetcodeapi.service.problem.ProblemSolveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/problemsolverecord")
public class ProblemSolveRecordController {

    @Autowired
    LeetcodeProblemService leetcodeProblemService;

    @Autowired
    ProblemSolveRecordService problemSolveRecordService;



    @PostMapping("/createsolve/{titleSlug}")
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

        ProblemSolveRecord solveRecord = problemSolveRecordService.createProblemSolveRecord(userEntry, problemEntry, proficiency);

        return solveRecord;
    }



    @GetMapping("/allsolves/{titleSlug}")
    public Page<ProblemSolveRecord> getAllProblemSolveRecord(
            @PathVariable(name = "titleSlug") String problemTitleSlug,
            @AuthenticationPrincipal UserEntry userEntry,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){

        ProblemEntry problemEntry =  leetcodeProblemService.getProblemByTitleSlug(problemTitleSlug);
        if(problemEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem does not exists!");
        }

        return problemSolveRecordService.getAllProblemSolveRecordOfUserAndProblem(userEntry, problemEntry, page, size);
    }

}
