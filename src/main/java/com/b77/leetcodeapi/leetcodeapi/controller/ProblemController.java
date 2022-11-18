package com.b77.leetcodeapi.leetcodeapi.controller;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.request.ProblemQueryRequest;
import com.b77.leetcodeapi.leetcodeapi.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    ProblemService problemService;

    @GetMapping("/getrandom")
    public ProblemEntry getRandomProblem() {
        ProblemQueryRequest request = new ProblemQueryRequest();
        request.getCategories().add("algorithms");
        request.getDifficulties().add(0);

        return getRandomProblem(request);
    }

    @PostMapping("/getrandom")
    public ProblemEntry getRandomProblem(@RequestBody ProblemQueryRequest requestBody) {
        ProblemEntry problemEntry = problemService.getRandomProblem(requestBody);

        if(problemEntry == null){
            return null;
        } else {
            return problemEntry;
        }
    }
}
