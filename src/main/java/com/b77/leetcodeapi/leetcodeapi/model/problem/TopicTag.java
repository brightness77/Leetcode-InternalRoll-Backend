package com.b77.leetcodeapi.leetcodeapi.model.problem;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "topic_tag")
@Table(name = "topic_tag", indexes = {})

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@ToString(exclude = { "problemEntryList" })
@EqualsAndHashCode(exclude = { "problemEntryList" })

public class TopicTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    long id;

    @NotEmpty
    String name;

    @NotEmpty
    String slug;

    @ManyToMany(mappedBy = "topicTags")
    @JsonIgnore
    //@JsonBackReference("problem_topictag")
    List<ProblemEntry> problemEntryList;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime timeStamp;

}
