package com.b77.leetcodeapi.leetcodeapi.repository.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemSolveRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemSolveRecordRepository extends JpaRepository<ProblemSolveRecord, Long> {

    public List<ProblemSolveRecord> getByProblemRecord(ProblemRecord problemRecord);

}
