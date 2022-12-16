package com.b77.leetcodeapi.leetcodeapi.repository.tag;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicTagRepository extends JpaRepository<TopicTag, Long> {

    public TopicTag getBySlug(String nameSlug);

    public TopicTag getByName(String name);

}
