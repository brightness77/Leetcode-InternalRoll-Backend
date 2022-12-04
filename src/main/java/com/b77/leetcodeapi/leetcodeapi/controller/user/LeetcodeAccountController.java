package com.b77.leetcodeapi.leetcodeapi.controller.user;

import com.b77.leetcodeapi.leetcodeapi.model.request.AddLeetcodeAccountRequest;
import com.b77.leetcodeapi.leetcodeapi.model.user.LeetcodeAccount;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.service.user.LeetcodeAccountService;
import com.b77.leetcodeapi.leetcodeapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/leetcodeaccount")
public class LeetcodeAccountController {

    @Autowired
    LeetcodeAccountService leetcodeAccountService;

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public LeetcodeAccount addLeetcodeAccount(
            @RequestBody AddLeetcodeAccountRequest addRequest,
            @AuthenticationPrincipal UserEntry userEntry){

        //existing account
        if(leetcodeAccountService.getAccountByAccountName(addRequest.getAccountName()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leetcode account already exists");
        }

        return leetcodeAccountService.addLeetcodeAccount(userEntry, addRequest.getAccountName());
    }

}
