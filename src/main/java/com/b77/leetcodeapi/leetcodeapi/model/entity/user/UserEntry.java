package com.b77.leetcodeapi.leetcodeapi.model.entity.user;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemRecord;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity(name = "roll_users")
@Table(name = "roll_users", indexes = {
        @Index(name = "email_index", columnList = "email", unique = true),
        @Index(name = "username_index", columnList = "username", unique = true)})

@Data
@Builder
@ToString(exclude = {"password"})
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class UserEntry implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @NotNull(message = "Email cannot be empty")
    @Column(nullable = false)
    private String email;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime utcRegisterTime;

    @OneToMany(mappedBy = "userEntry", cascade = CascadeType.ALL)
    @JsonManagedReference("user_leetcodeAccount")
    private List<LeetcodeAccount> leetcodeAccounts;

    private InetAddress registerIPAddress;

    private String nickname;

    private String adminNickname;

    //ac count
    private Integer easyAC;

    private Integer mediumAC;

    private Integer hardAC;

    private Integer allAC;





    // ========== MAPPED ==========

    @OneToOne(mappedBy = "userEntry")
    @JsonManagedReference("user-admin")
    Admin admin;


    @OneToMany(mappedBy = "userEntry")
    @JsonManagedReference("user-problemRecord")
    List<ProblemRecord> problemRecordList;


    // ========== AUTHORITY ==========

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(this.admin != null){
            System.out.println("Granting " + this.username + " admin role!");
            authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
        }
        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
