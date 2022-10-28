package com.b77.leetcodeapi.leetcodeapi.repository;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AllQuestionCounts {

    private int allCount;

    private int easyCount;

    private int mediumCount;

    private int hardCount;
}
