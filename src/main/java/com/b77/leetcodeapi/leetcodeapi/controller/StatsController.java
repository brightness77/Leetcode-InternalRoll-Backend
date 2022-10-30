package com.b77.leetcodeapi.leetcodeapi.controller;

import com.b77.leetcodeapi.leetcodeapi.model.Submission;
import com.b77.leetcodeapi.leetcodeapi.model.UserSubmissionCalendar;
import com.b77.leetcodeapi.leetcodeapi.service.LeetcodeGraphQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private LeetcodeGraphQLService graphQLService;

    @GetMapping("/user7days")
    public UserSubmissionCalendar getUser7days(@RequestParam(name = "username", defaultValue = "leapcode") String username) {
        return graphQLService.getSubmissionDaysByUsername(username, 7);
    }

    @GetMapping("/userToday")
    public UserSubmissionCalendar getUserToday(@RequestParam(name = "username", defaultValue = "leapcode") String username) {
        return graphQLService.getSubmissionDaysByUsername(username, 1);
    }
}
