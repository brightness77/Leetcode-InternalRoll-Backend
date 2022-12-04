package com.b77.leetcodeapi.leetcodeapi.repository.user;

import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntry, Long> {

    public UserEntry getByUsername(String username);

    public UserEntry getByEmail(String email);


    public List<UserEntry> getByUsernameIsNotNull();

}
