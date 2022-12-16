package com.b77.leetcodeapi.leetcodeapi.model.entity.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name = "topic_record")
@Table(name = "topic_record", indexes = {
        @Index(name = "user_topic_index", columnList = "user_id, topic_id", unique = true) ,
})

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TopicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    private int totalProblems;

    private long totalAC;

    private double averageProficiency;




    // ========== mapped relationships ==========

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntry userEntry;

    @ManyToOne()
    @JoinColumn(name = "topic_id")
    private TopicTag topicTag;

}
