package com.b77.leetcodeapi.leetcodeapi.model.entity.problem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity(name = "problem")
@Table(name = "problem", indexes = {
        @Index(name = "questionID_index", columnList = "questionID", unique = true) ,
        @Index(name = "frontendID_index", columnList = "frontendID", unique = true) ,
        @Index(name = "difficulty_index", columnList = "difficulty", unique = false) ,
})

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProblemEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    long id;

    @Column(nullable = false)
    int questionID;

    @Column(nullable = false)
    int frontendID;

    @Column(nullable = false)
    int difficulty;

    String title;

    @NotEmpty
    @Column(nullable = false)
    String titleSlug;

    @NotEmpty
    @Column(nullable = false)
    String category;

    String url;

    long likes;

    long dislikes;

    long totalSubmissions;

    long totalAccepted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime timeStamp;


    // ========== mapped relationships ==========

    @ManyToMany
    @JoinTable(name = "problem_topic",
        joinColumns = @JoinColumn(name = "topictag_id"),
        inverseJoinColumns = @JoinColumn(name = "problem_id"))
    //@JsonManagedReference("problem_topictag")
    Set<TopicTag> topicTags;

//    @ManyToMany(mappedBy = "origProblem")
//    List<ProblemEntry> similarProblems;

    @OneToMany(mappedBy = "problemEntry")
    @JsonBackReference("problemEntry-problemRecord")
    List<ProblemRecord> problemRecordList;


    //========== Not provided now ==========

    //String content;

    //String hint;

}
