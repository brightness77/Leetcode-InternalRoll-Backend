package com.b77.leetcodeapi.leetcodeapi.repository.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProblemRepository extends JpaRepository<ProblemEntry, Long> {

    public ProblemEntry getByQuestionID(int questionID);

    public ProblemEntry getByTitleSlug(String titleSlug);

    public ProblemEntry getByFrontendID(int frontendID);




    public List<ProblemEntry> getByFrontendIDGreaterThan(int frontendId);


    public List<ProblemEntry> findByCategory(String category);

//    @Query(value = "SELECT p FROM problem " +
//            "JOIN p.problemRecordList pr " +
//            "WHERE pr.")
//    public List<ProblemEntry> findRandomOldProblem();

}
