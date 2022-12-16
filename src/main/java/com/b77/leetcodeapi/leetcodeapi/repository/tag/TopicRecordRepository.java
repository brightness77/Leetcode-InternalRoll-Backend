package com.b77.leetcodeapi.leetcodeapi.repository.tag;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRecordRepository extends JpaRepository<TopicRecord, Long> {

    public TopicRecord getByUserEntryAndTopicTag(UserEntry userEntry, TopicTag topicTag);

    public List<TopicRecord> getByUserEntry(UserEntry userEntry);

    public Page<TopicRecord> getByUserEntryAndTotalProblemsGreaterThan(UserEntry userEntry, int totalProblems, Pageable pageable);

}
