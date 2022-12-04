package com.b77.leetcodeapi.leetcodeapi.service;

import com.b77.leetcodeapi.leetcodeapi.model.Submission;
import com.b77.leetcodeapi.leetcodeapi.model.UserSubmissionCalendar;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeAPIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserStatService {

    @Autowired
    LeetcodeAPIService leetcodeAPIService;


    //config data
    private final LocalDate STARTDATE = LocalDate.parse("1970-01-01");

    private final int DIFF_TO_UTC = 0;


    public int getDIFF_TO_UTC(){
        return DIFF_TO_UTC;
    }


    public void getUserStatsByUsername(String username){

        String jsonString = leetcodeAPIService.getUserStatsByUsername(username);

        if("Error".equals(jsonString)){
            //error status
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
        } catch (JsonProcessingException exception){
            return;
        }
    }

    public UserSubmissionCalendar getSubmissionDaysByUsername(String username, int days){
        UserSubmissionCalendar calendar = getSubmissionCalendarByUsername(username);

        UserSubmissionCalendar daysCalendar = new UserSubmissionCalendar(username);
        LocalDate today = LocalDate.now();

        List<Submission> subs = calendar.getSubmissionList();

        LocalDate curDate = LocalDateTime.now().plusHours(DIFF_TO_UTC).toLocalDate();
        LocalDate nowDate = LocalDateTime.now().plusHours(DIFF_TO_UTC).toLocalDate();
        int i = subs.size() - 1;

        while(curDate.plusDays(days).isAfter(nowDate) || curDate.plusDays(days).isEqual(nowDate)){

            //System.out.println("Cur date is " + curDate + " while now date is " + nowDate);
            while(i >= 0 && subs.get(i).getDate().isAfter(curDate)){
                i--;
            }
            if(i >= 0 && subs.get(i).getDate().isEqual(curDate)){
                daysCalendar.addSubmission(subs.get(i));
            } else {
                //add an empty submission
                daysCalendar.addSubmission(new Submission(curDate, 0));
            }

            curDate = curDate.minusDays(1);
        }

        return daysCalendar;
    }



    public UserSubmissionCalendar getSubmissionCalendarByUsername(String username){

        String jsonString = leetcodeAPIService.getUserStatsByUsername(username);

        if("Error".equals(jsonString)){
            //error status
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            String submissionCalendarString = rootNode.get("data").get("matchedUser").get("submissionCalendar").asText("No Submisison!");

            UserSubmissionCalendar calendar = parseSubmissionCalendar(username, submissionCalendarString);
            return calendar;
        } catch (JsonProcessingException exception){
            return null;
        }
    }




    //================== PRIVATE METHODS =====================




    private UserSubmissionCalendar parseSubmissionCalendar(String accountName, String string){

        UserSubmissionCalendar calendar = new UserSubmissionCalendar(accountName);
        // System.out.println("Parsing " + string);

        //empty submission
        if(string.length() == 2){
            return calendar;
        }

        StringBuilder sb = new StringBuilder(string);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        string = sb.toString();

        String[] submissions = string.split(", ");
        for(String submission: submissions){
            long seconds = Long.parseLong(submission.substring(1, 11));
            int days = (int)(seconds / 86_400);
            int count = Integer.parseInt(submission.substring(14));
            LocalDate day = STARTDATE.plusDays(days);

            calendar.addSubmission(day, count);
        }

        //need to sort calendar
        calendar.sortCalendar();
        return calendar;
    }


}
