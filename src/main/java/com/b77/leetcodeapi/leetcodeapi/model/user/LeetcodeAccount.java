package com.b77.leetcodeapi.leetcodeapi.model.user;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity(name = "leetcode_account")
@Table(name = "leetcode_account", indexes = {})

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LeetcodeAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotEmpty
    @Column(nullable = false)
    private String leetcodeAccountName;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @JsonBackReference()
    private UserEntry userEntry;

    //String leetcode_accountName;

    //String leetcode_name;

    //String leetcode_password;
}
