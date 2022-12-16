package com.b77.leetcodeapi.leetcodeapi.repository.user;

import com.b77.leetcodeapi.leetcodeapi.model.entity.user.LeetcodeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LeetcodeAccountRepository extends JpaRepository<LeetcodeAccount, Long> {

    public LeetcodeAccount getByLeetcodeAccountName(String accountName);

    public LeetcodeAccount removeById(long id);

}
