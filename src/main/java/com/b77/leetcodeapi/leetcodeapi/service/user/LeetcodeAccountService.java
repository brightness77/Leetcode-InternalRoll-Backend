package com.b77.leetcodeapi.leetcodeapi.service.user;

import com.b77.leetcodeapi.leetcodeapi.model.user.LeetcodeAccount;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.user.LeetcodeAccountRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LeetcodeAccountService {

    @Autowired
    LeetcodeAccountRepository leetcodeAccountRepository;

    @Autowired
    UserService userService;


    public LeetcodeAccount addLeetcodeAccount(UserEntry userEntry, String leetcodeAccountName){

        LeetcodeAccount leetcodeAccount = LeetcodeAccount.builder()
                .leetcodeAccountName(leetcodeAccountName)
                .userEntry(userEntry)
                .build();

        leetcodeAccountRepository.save(leetcodeAccount);

        return leetcodeAccount;
    }


    public LeetcodeAccount getAccountByAccountName(String accountName){
        return leetcodeAccountRepository.getByLeetcodeAccountName(accountName);
    }

}
