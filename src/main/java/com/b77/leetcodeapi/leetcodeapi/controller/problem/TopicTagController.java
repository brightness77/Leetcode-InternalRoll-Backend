package com.b77.leetcodeapi.leetcodeapi.controller.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.service.problem.TopicTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/record")
    public Page<TopicRecord> getAllTopicRecord(
            @AuthenticationPrincipal UserEntry userEntry,
            @RequestParam(name = "page", required = true, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size){



        return topicTagService.getAllByUserEntry(userEntry, page, size);

    }

}
