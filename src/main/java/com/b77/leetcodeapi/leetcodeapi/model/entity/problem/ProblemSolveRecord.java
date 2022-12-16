package com.b77.leetcodeapi.leetcodeapi.model.entity.problem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity(name = "problem_solve_record")
@Table(name = "problem_solve_record", indexes = {
        //@Index(name = "questionID", columnList = "questionID", unique = true) ,
})

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProblemSolveRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime utcStartTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime utcEndTime;

    @Min(value = 0, message = "Proficiency minimum is 0")
    @Max(value = 5, message = "Proficiency maximum is 5")
    private int proficiency;

    @ManyToOne()
    @JoinColumn(name = "problem_record_id")
    @JsonIgnore
    ProblemRecord problemRecord;

    // ========== mapped relationships ==========
}
