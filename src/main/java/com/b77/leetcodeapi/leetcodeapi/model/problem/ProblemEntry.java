package com.b77.leetcodeapi.leetcodeapi.model.problem;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProblemEntry {

    //int id;

    int questionID;

    int frontendID;

    int difficulty;

    String title;

    String titleSlug;

    String content;

    List<String> topicTags;

}
