package com.b77.leetcodeapi.leetcodeapi.repository.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRecordRepository extends JpaRepository<ProblemRecord, Long> {

    public ProblemRecord getByProblemEntryAndUserEntry(ProblemEntry problemEntry, UserEntry userEntry);

    public List<ProblemRecord> getByUserEntry(UserEntry userEntry);

}
