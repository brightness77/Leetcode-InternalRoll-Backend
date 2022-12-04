package com.b77.leetcodeapi.leetcodeapi.controller.user;

import com.b77.leetcodeapi.leetcodeapi.model.user.Admin;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.service.user.AdminService;
import com.b77.leetcodeapi.leetcodeapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;



    @GetMapping("/{username}")
    @RolesAllowed({ "ROLE_Admin" })
    public Admin updateAdmin(@PathVariable("username") String username,
                             @AuthenticationPrincipal UserEntry curUserEntry){

        //check username
        UserEntry updateUserEntry = userService.getUserByUsername(username);
        if(updateUserEntry == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found");
        }

        System.out.println(curUserEntry.getUsername());

        //403
        if(!adminService.isUserAdmin(curUserEntry)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        Admin admin = adminService.updateAdminProfile(updateUserEntry);
        return admin;
    }




    @GetMapping("/allusers")
    @RolesAllowed({ "ROLE_Admin" })
    public List<UserEntry> getAllUsers(){
        return userService.getAllUsers();
    }



}
