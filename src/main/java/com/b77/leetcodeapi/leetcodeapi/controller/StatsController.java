package com.b77.leetcodeapi.leetcodeapi.controller;

import com.b77.leetcodeapi.leetcodeapi.model.UserSubmissionCalendar;
import com.b77.leetcodeapi.leetcodeapi.service.UserStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private UserStatService userStatService;

    @GetMapping("/user7days")
    public UserSubmissionCalendar getUser7days(@RequestParam(name = "username", defaultValue = "leap-code") String username) {
        return userStatService.getSubmissionDaysByUsername(username, 7);
    }

    @GetMapping("/userToday")
    public UserSubmissionCalendar getUserToday(@RequestParam(name = "username", defaultValue = "leap-code") String username) {
        return userStatService.getSubmissionDaysByUsername(username, 1);
    }
}
