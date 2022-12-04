package com.b77.leetcodeapi.leetcodeapi.controller.user;

import com.b77.leetcodeapi.leetcodeapi.model.request.CreateUserRequest;
import com.b77.leetcodeapi.leetcodeapi.model.user.Admin;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.provider.KeywordProvider;
import com.b77.leetcodeapi.leetcodeapi.service.user.AdminService;
import com.b77.leetcodeapi.leetcodeapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;


    @Autowired
    KeywordProvider keywordProvider;





    @PostMapping("/create")
    public UserEntry createUser(@RequestBody CreateUserRequest createUserRequest){

        //existing username check
        if(userService.getUserByUsername(createUserRequest.getUsername()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        //existing email check
        if(userService.getUserByEmail(createUserRequest.getEmail()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        //blocklist username check
        if(keywordProvider.getUsernameBlockSet().contains(createUserRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad username");
        }

        return userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword(), createUserRequest.getEmail(), createUserRequest.getClientHost());
    }




    @GetMapping("/currentUser")
    public UserEntry currentUser(@AuthenticationPrincipal UserEntry userEntry){
        if(userEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not logged in!");
        } else {
            return userService.getUserByUsername(userEntry.getUsername());
        }
    }



    @GetMapping("/username/{username}")
    public UserEntry getUserByUsername(@PathVariable String username) {
        UserEntry userEntry = userService.getUserByUsername(username);

        if(userEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found");
        }

        return userEntry;
    }


}
