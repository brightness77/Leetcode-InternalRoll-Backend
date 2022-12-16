package com.b77.leetcodeapi.leetcodeapi.controller.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeProblemService;
import com.b77.leetcodeapi.leetcodeapi.service.problem.ProblemRecordService;
import com.b77.leetcodeapi.leetcodeapi.service.problem.ProblemSolveRecordService;
import com.b77.leetcodeapi.leetcodeapi.service.problem.TopicTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/problemrecord")
public class ProblemRecordController {

    @Autowired
    private LeetcodeProblemService leetcodeProblemService;

    @Autowired
    private ProblemRecordService problemRecordService;

    @Autowired
    private ProblemSolveRecordService problemSolveRecordService;

    @Autowired
    private TopicTagService topicTagService;


    @GetMapping("/record/{titleSlug}")
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
    public Page<ProblemRecord> getAllProblemRecord(
            @AuthenticationPrincipal UserEntry userEntry,
            @RequestParam(name = "page", required = true, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "topicTag", required = false) String topicTagName){

        if(topicTagName == null || topicTagName.isEmpty()){
            //situation that no topic tag input
            return problemRecordService.getAllProblemRecordOfUser(userEntry, page, size);
        } else {
            //situation with topic tag input
            if(!topicTagService.isValidTopicTagName(topicTagName)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topic tag name!");
            }
            return problemRecordService.getAllProblemRecordOfUserByTopicTag(userEntry, page, size, topicTagName);
        }

    }


}
