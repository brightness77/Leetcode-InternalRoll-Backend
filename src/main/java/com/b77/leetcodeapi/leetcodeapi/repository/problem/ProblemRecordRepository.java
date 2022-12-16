package com.b77.leetcodeapi.leetcodeapi.repository.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRecordRepository extends JpaRepository<ProblemRecord, Long> {

    public ProblemRecord getByProblemEntryAndUserEntry(ProblemEntry problemEntry, UserEntry userEntry);

    public List<ProblemRecord> getByUserEntryAndAcceptedCountIsGreaterThan(UserEntry userEntry, long count);

    public Page<ProblemRecord> getByUserEntryAndAcceptedCountIsGreaterThan(UserEntry userEntry, long count, Pageable pageable);

    @Query(value = "SELECT r FROM problem_record r " +
            "JOIN r.problemEntry pe " +
            "JOIN pe.topicTags tt " +
            "WHERE tt.id=:topictag_id AND " +
            "r.userEntry.id=:user_id AND " +
            "r.acceptedCount > 0")
    public Page<ProblemRecord> getByUserAndTopicTag(
            @Param("user_id") long userId,
            @Param("topictag_id") long topicTagId,
            Pageable pageable);


    @Query(value = "SELECT r FROM problem_record r " +
            "JOIN r.problemEntry pe " +
            "JOIN pe.topicTags tt " +
            "WHERE tt.id=:topictag_id AND " +
            "r.userEntry.id=:user_id AND " +
            "r.acceptedCount > 0")
    public List<ProblemRecord> getByUserAndTopicTag(
            @Param("user_id") long userId,
            @Param("topictag_id") long topicTagId);


}
