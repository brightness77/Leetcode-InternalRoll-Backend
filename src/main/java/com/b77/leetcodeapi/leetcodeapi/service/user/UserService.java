package com.b77.leetcodeapi.leetcodeapi.service.user;

import com.b77.leetcodeapi.leetcodeapi.model.user.Admin;
import com.b77.leetcodeapi.leetcodeapi.model.user.LoginRecord;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.user.LoginRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRecordRepository loginRecordRepository;




    public UserEntry createUser(String username, String password, String email, String clientHost){
        String encodedPassword = passwordEncoder.encode(password);

        UserEntry userEntry = UserEntry.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .utcRegisterTime(LocalDateTime.now())
                .build();

        //try parsing ip address
        InetAddress inetAddress = null;
        try{
            if(clientHost != null && !clientHost.isEmpty()){
                inetAddress = InetAddress.getByName(clientHost);
            }
        } catch (UnknownHostException | SecurityException exception) {
            System.out.println("Cannot parse client host, " + exception.getMessage());
        }

        if(inetAddress != null){
            //successfully retrieved
            userEntry.setRegisterIPAddress(inetAddress);
        }

        userRepository.save(userEntry);
        return userEntry;
    }



    public UserEntry getUserByUsername(String username){
        return userRepository.getByUsername(username);
    }

    public UserEntry getUserByEmail(String email){
        return userRepository.getByEmail(email);
    }



    public void recordLogin(UserEntry userEntry){
        LoginRecord loginRecord = LoginRecord.builder()
                .userEntry(userEntry)
                .loginTime(LocalDateTime.now())
                .build();

        loginRecordRepository.save(loginRecord);
    }



    public List<UserEntry> getAllUsers(){
        return userRepository.getByUsernameIsNotNull();
    }





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntry userEntry = this.getUserByUsername(username);

        if (userEntry == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return userEntry;
        }
    }




    // ========== PRIVATE METHOD ==========

}
