package com.b77.leetcodeapi.leetcodeapi.service.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.TopicTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicTagService {

    @Autowired
    TopicTagRepository topicTagRepository;

    public List<TopicTag> getAllTopicTags(){
        return null;
    }

}
