package com.b77.leetcodeapi.leetcodeapi.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemQueryRequest {

    List<String> categories;

    List<String> topicTags;

    List<String> companies;

    List<Integer> difficulties;


    public ProblemQueryRequest() {
        categories = new ArrayList<>();
        topicTags = new ArrayList<>();
        companies = new ArrayList<>();
        difficulties = new ArrayList<>();
    }
}
