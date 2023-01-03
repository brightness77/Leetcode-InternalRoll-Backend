package com.b77.leetcodeapi.leetcodeapi.repository.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemSolveRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemSolveRecordRepository extends JpaRepository<ProblemSolveRecord, Long> {

    public List<ProblemSolveRecord> getByProblemRecord(ProblemRecord problemRecord);

    @Query(value = "SELECT sr FROM problem_solve_record sr " +
            "JOIN sr.problemRecord pr " +
            "JOIN pr.userEntry u " +
            "WHERE u.id=:user_id")
    public Page<ProblemSolveRecord> getByUser(@Param("user_id") long userId, Pageable pageable);

    public Page<ProblemSolveRecord> getByProblemRecord(ProblemRecord problemRecord, Pageable pageable);
}
