package com.b77.leetcodeapi.leetcodeapi.controller.user;

import com.b77.leetcodeapi.leetcodeapi.model.request.CreateUserRequest;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.model.response.UserDiffACResponse;
import com.b77.leetcodeapi.leetcodeapi.provider.KeywordProvider;
import com.b77.leetcodeapi.leetcodeapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;




    @PostMapping("/create")
    public UserEntry createUser(@RequestBody CreateUserRequest createUserRequest){

        //bad username input check
        if(!userService.isValidUsername(createUserRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad username");
        }

        //existing username check
        if(userService.isUsernameUsed(createUserRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        //existing email check
        if(userService.getUserByEmail(createUserRequest.getEmail()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
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


    @GetMapping("/acstats/{username}")
    public UserDiffACResponse getUserACStats(@PathVariable String username) {
        UserEntry userEntry = userService.getUserByUsername(username);

        if(userEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found");
        }

        return userService.getUserDiffACResponse(userEntry);
    }


}
