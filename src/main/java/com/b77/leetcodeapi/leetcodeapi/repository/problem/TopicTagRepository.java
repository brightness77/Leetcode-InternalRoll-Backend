package com.b77.leetcodeapi.leetcodeapi.repository.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.TopicTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicTagRepository extends JpaRepository<TopicTag, Long> {

    public TopicTag getBySlug(String nameSlug);

    public TopicTag getByName(String name);

}
