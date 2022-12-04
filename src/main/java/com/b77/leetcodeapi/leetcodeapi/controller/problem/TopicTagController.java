package com.b77.leetcodeapi.leetcodeapi.controller.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.service.problem.TopicTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topictag")
public class TopicTagController {

    @Autowired
    TopicTagService topicTagService;

    @GetMapping("/all")
    public List<TopicTag> getAllTopicTags(){
        return topicTagService.getAllTopicTags();
    }

}
