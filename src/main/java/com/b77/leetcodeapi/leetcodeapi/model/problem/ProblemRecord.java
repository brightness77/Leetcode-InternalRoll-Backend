package com.b77.leetcodeapi.leetcodeapi.model.problem;

import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity(name = "problem_record")
@Table(name = "problem_record", indexes = {
        @Index(name = "user_problem_index", columnList = "user_id, problem_id", unique = true) ,
})

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProblemRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    private long acceptedCount;

    private long sumProficiency;

    private double averageProficiency;

    private int recentProficiency;



    // ========== mapped relationships ==========

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-problemRecord")
    private UserEntry userEntry;

    @ManyToOne()
    @JoinColumn(name = "problem_id")
    private ProblemEntry problemEntry;

}
