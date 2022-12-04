package com.b77.leetcodeapi.leetcodeapi.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity(name = "roll_admin")
@Table(name = "roll_admin", indexes = {})

@Data
@Builder
@ToString(exclude = {"userEntry"})
@EqualsAndHashCode(exclude = {"userEntry"})
@AllArgsConstructor
@NoArgsConstructor

public class Admin {

    @Id
    @Setter(AccessLevel.NONE)
    Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    @MapsId
    @JsonBackReference("user-admin")
    UserEntry userEntry;

}
