package com.b77.leetcodeapi.leetcodeapi.model.entity.user;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity(name = "leetcode_account")
@Table(name = "leetcode_account", indexes = {
        @Index(name = "account_user_index", columnList = "leetcodeAccountName, user_id", unique = true)})

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode

public class LeetcodeAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotEmpty
    @Column(nullable = false, unique = false)
    private String leetcodeAccountName;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @JsonBackReference("user_leetcodeAccount")
    private UserEntry userEntry;

    //String leetcode_accountName;

    //String leetcode_name;

    //String leetcode_password;
}
