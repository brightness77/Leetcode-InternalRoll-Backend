package com.b77.leetcodeapi.leetcodeapi.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LeetcodeConfig {

    private final String[] CATEGORIES = {
            "algorithms",
            "database" ,
            "shell"};

    //Interval is in hours
    private final int PROBLEM_UPDATE_INTERVAL = 6;

    private final int TOPIC_UPDATE_INTERVAL = 24;

}
