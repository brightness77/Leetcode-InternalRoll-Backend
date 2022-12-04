package com.b77.leetcodeapi.leetcodeapi.service.user;

import com.b77.leetcodeapi.leetcodeapi.model.user.Admin;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.user.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    @Autowired
    UserService userService;

    @Autowired
    AdminRepository adminRepository;

    public Admin updateAdminProfile(UserEntry userEntry){
        if(userEntry.getAdmin() == null){
            Admin admin = new Admin();
            admin.setUserEntry(userEntry);
            adminRepository.save(admin);
            return admin;
        } else {
            return userEntry.getAdmin();
        }
    }


    public boolean isUserAdmin(UserEntry userEntry){
        Admin admin = userEntry.getAdmin();
        return admin != null;
    }

}
