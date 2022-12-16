package com.b77.leetcodeapi.leetcodeapi.controller.user;

import com.b77.leetcodeapi.leetcodeapi.model.request.LeetcodeAccountRequest;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.LeetcodeAccount;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.configuration.UserConfig;
import com.b77.leetcodeapi.leetcodeapi.service.user.LeetcodeAccountService;
import com.b77.leetcodeapi.leetcodeapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/leetcodeaccount")
public class LeetcodeAccountController {

    @Autowired
    UserConfig userConfig;

    @Autowired
    LeetcodeAccountService leetcodeAccountService;

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public LeetcodeAccount addLeetcodeAccount(
            @RequestBody LeetcodeAccountRequest leetcodeAccountRequest,
            @AuthenticationPrincipal UserEntry userEntry){

        //existing account
        if(leetcodeAccountService.getAccountByAccountName(leetcodeAccountRequest.getAccountName()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leetcode account already exists!");
        }
        if(userEntry.getLeetcodeAccounts().size() > userConfig.getMAX_LEETCODE_ACCOUNT_PER_USER()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Maximum leetcode account reached!");
        }

        return leetcodeAccountService.addLeetcodeAccount(userEntry, leetcodeAccountRequest.getAccountName());
    }


    @PostMapping("/delete")
    public LeetcodeAccount deleteLeetcodeAccount(
            @RequestBody LeetcodeAccountRequest leetcodeAccountRequest,
            @AuthenticationPrincipal UserEntry userEntry){

        //existing account
        LeetcodeAccount leetcodeAccount = leetcodeAccountService.getAccountByAccountName(leetcodeAccountRequest.getAccountName());
        if(leetcodeAccount == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leetcode account does not exist");
        } else {
            //user
            if(!userEntry.getLeetcodeAccounts().contains(leetcodeAccount)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete account dose not belongs to you");
            }
        }

        LeetcodeAccount deletedAccount = leetcodeAccountService.deleteLeetcodeAccount(leetcodeAccount);

        if(deletedAccount == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leetcode account does not exist");
        }

        return deletedAccount;
    }

}
