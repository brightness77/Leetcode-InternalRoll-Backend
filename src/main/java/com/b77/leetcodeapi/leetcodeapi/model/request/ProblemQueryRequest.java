package com.b77.leetcodeapi.leetcodeapi.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProblemQueryRequest {

    boolean newProblem;

    String category;

    List<String> topicTags;

    List<Integer> difficulties;

    //proficiency and count will be disabled if it is new to a user
    int proficiencyLow;

    int proficiencyHigh;

    long countMin;

    long countMax;

    int frontendID;


    //List<String> companies;
}
