package com.b77.leetcodeapi.leetcodeapi.model.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "login_record")
@Table(name = "login_record", indexes = {})

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    UserEntry userEntry;

    LocalDateTime loginTime;

    //String ipAddress;
}
