package com.b77.leetcodeapi.leetcodeapi.controller.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemSolveRecord;
import com.b77.leetcodeapi.leetcodeapi.model.request.ProblemQueryRequest;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemSolveRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeProblemService;
import com.b77.leetcodeapi.leetcodeapi.service.problem.ProblemRecordService;
import com.b77.leetcodeapi.leetcodeapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    ProblemRecordService problemRecordService;

    @Autowired
    LeetcodeProblemService leetcodeProblemService;


    @GetMapping("/id/{id}")
    public ProblemEntry getProblemByFrontendId(@PathVariable(name = "id") int frontendId) {
        ProblemEntry problemEntry = leetcodeProblemService.getProblemByFrontendId(frontendId);
        if(problemEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Frontend ID does not exists!");
        } else {
            return problemEntry;
        }
    }


    @GetMapping("/titleslug/{titleSlug}")
    public ProblemEntry getProblemByTitleSlug(@PathVariable(name = "titleSlug") String titleSlug){
        ProblemEntry problemEntry = leetcodeProblemService.getProblemByTitleSlug(titleSlug);
        if(problemEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title slug does not exists!");
        } else {
            return problemEntry;
        }
    }



    @GetMapping("/getrandom")
    public ProblemEntry getRandomProblem(@AuthenticationPrincipal UserEntry userEntry) {
        ProblemQueryRequest request = ProblemQueryRequest.builder()
                .newProblem(true)
                .category("algorithms")
                .topicTags(new ArrayList<>())
                .difficulties(new ArrayList<>())
                .proficiencyHigh(5)
                .proficiencyLow(0)
                .countMin(0)
                .countMax(Long.MAX_VALUE)
                .frontendID(0)
                .build();

        ProblemEntry problemEntry = this.getRandomProblem(request, userEntry);
        if(problemEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No result found!");
        } else {
            return problemEntry;
        }
    }


    @PostMapping("/getrandom")
    public ProblemEntry getRandomProblem(
            @RequestBody ProblemQueryRequest requestBody,
            @AuthenticationPrincipal UserEntry userEntry) {

        //these two random methods has to be one get with request param instead of request body

        ProblemEntry problemEntry = problemRecordService.getRandomProblem(requestBody, userEntry);

        if(problemEntry == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No result found!");
        } else {
            return problemEntry;
        }
    }



    // =========== ADMIN API ===========

    @GetMapping("/updatenew")
    @RolesAllowed({ "ROLE_Admin" })
    public String updateNewProblems(){
        leetcodeProblemService.updateNewProblems();
        return "Successfully update new problems!";
    }


    @GetMapping("/forceupdateall")
    @RolesAllowed({ "ROLE_Admin" })
    public String forceUpdateAllProblems(){
        leetcodeProblemService.forceUpdateAllProblems();
        return "Successfully force update all problems!";
    }


}
