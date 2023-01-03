package com.b77.leetcodeapi.leetcodeapi.service.user;

import com.b77.leetcodeapi.leetcodeapi.model.entity.user.LoginRecord;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.model.response.UserDiffACResponse;
import com.b77.leetcodeapi.leetcodeapi.provider.KeywordProvider;
import com.b77.leetcodeapi.leetcodeapi.configuration.LeetcodeConfig;
import com.b77.leetcodeapi.leetcodeapi.provider.LeetcodeData;
import com.b77.leetcodeapi.leetcodeapi.repository.user.LoginRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.user.UserRepository;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeProblemService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRecordRepository loginRecordRepository;

    @Autowired
    private LeetcodeProblemService leetcodeProblemService;

    @Autowired
    KeywordProvider keywordProvider;

    @Autowired
    LeetcodeConfig leetcodeConfig;

    @Autowired
    LeetcodeData leetcodeData;




    public UserEntry createUser(String username, String password, String email, String clientHost){
        String encodedPassword = passwordEncoder.encode(password);

        UserEntry userEntry = UserEntry.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .utcRegisterTime(LocalDateTime.now())
                .easyAC(0)
                .mediumAC(0)
                .hardAC(0)
                .allAC(0)
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

    public boolean isUsernameUsed(String username){
        UserEntry userEntry = userRepository.getByUsernameAllIgnoreCase(username);
        return userEntry != null;
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



    public boolean isValidUsername(String username){
        // Regex to check valid username.
        String regex = "^[A-Za-z]\\w{5,29}$";

        // Check blocklist
        if(keywordProvider.getUsernameBlockSet().contains(username)){
            return false;
        }

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the length of username is not valid
        if (username == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given username
        // and regular expression.
        Matcher m = p.matcher(username);

        // Return if the username
        // matched the ReGex
        return m.matches();
    }


    public UserDiffACResponse getUserDiffACResponse(UserEntry userEntry){
        //get leetcode data
        updateTotalCount();

        UserDiffACResponse userDiffACResponse = UserDiffACResponse.builder()
                .easyTotal(leetcodeData.getTotalEasy())
                .mediumTotal(leetcodeData.getTotalMedium())
                .hardTotal(leetcodeData.getTotalHard())
                .allTotal(leetcodeData.getTotalAll())

                .easyAC(userEntry.getEasyAC() == null ? 0 : userEntry.getEasyAC())
                .mediumAC(userEntry.getMediumAC() == null ? 0 : userEntry.getMediumAC())
                .hardAC(userEntry.getHardAC() == null ? 0 : userEntry.getHardAC())
                .allAC(userEntry.getAllAC() == null ? 0 : userEntry.getAllAC())
                .build();

        return userDiffACResponse;

    }



    public void getUserRecentSolveCount(UserEntry userEntry, int days){
        //WIP
    }



    // ========== PRIVATE METHOD ==========

    private void updateTotalCount(){
        //check
        if(leetcodeData.getTotalCountTimeStamp() != null &&
                leetcodeData.getTotalCountTimeStamp().plusHours(leetcodeConfig.getPROBLEM_UPDATE_INTERVAL()).isAfter(LocalDateTime.now())){
            //no need to update situation
            return;
        }

        //get data
        int[] countData = leetcodeProblemService.getAllQuestionCount();

        //set count
        leetcodeData.setTotalAll(countData[0]);
        leetcodeData.setTotalEasy(countData[1]);
        leetcodeData.setTotalMedium(countData[2]);
        leetcodeData.setTotalHard(countData[3]);

        //set time
        leetcodeData.setTotalCountTimeStamp(LocalDateTime.now());

    }

}
