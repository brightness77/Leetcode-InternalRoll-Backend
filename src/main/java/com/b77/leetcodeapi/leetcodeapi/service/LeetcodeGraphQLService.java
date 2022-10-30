package com.b77.leetcodeapi.leetcodeapi.service;

import com.b77.leetcodeapi.leetcodeapi.model.Submission;
import com.b77.leetcodeapi.leetcodeapi.model.UserSubmissionCalendar;
import com.b77.leetcodeapi.leetcodeapi.repository.AllQuestionCounts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class LeetcodeGraphQLService {

    private final String ALLQUESTIONSCOUNT = "allQuestionsCount";

    private final LocalDate METADATE = LocalDate.parse("1970-01-01");


    @Autowired
    AllQuestionCounts allQuestionCounts;

    public void getUserStatsByUsername(String username){
        String queryString = String.format("{\"query\":\"query getUserProfile($username: String!) { allQuestionsCount { difficulty count } matchedUser(username: $username) { contributions { points } profile { reputation ranking } submissionCalendar submitStats { acSubmissionNum { difficulty count submissions } totalSubmissionNum { difficulty count submissions } } } } \",\"variables\":{\"username\":\"%s\"}}", username);

        String jsonString = queryFromGraphQL(queryString);

        if("Error".equals(jsonString)){
            //error status
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
        } catch (JsonProcessingException exception){

        }
    }

    public UserSubmissionCalendar getSubmissionDaysByUsername(String username, int days){
        UserSubmissionCalendar calendar = getSubmissionCalendarByUsername(username);

        UserSubmissionCalendar seven = new UserSubmissionCalendar();
        LocalDate today = LocalDate.now();

        List<Submission> subs = calendar.getSubmissionList();
        int count = 0;
        for(int i = subs.size() - 1; i >= 0 && count < days; i--, count++){
            if(subs.get(i).getDate().plusDays(days).isAfter(today)){
                seven.addSubmission(subs.get(i));
            } else {
                break;
            }
        }

        return seven;
    }



    public UserSubmissionCalendar getSubmissionCalendarByUsername(String username){
        String queryString = String.format("{\"query\":\"query getUserProfile($username: String!) { matchedUser(username: $username) { submissionCalendar } } \",\"variables\":{\"username\":\"%s\"}}", username);

        String jsonString = queryFromGraphQL(queryString);

        if("Error".equals(jsonString)){
            //error status
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            String submissionCalendarString = rootNode.get("data").get("matchedUser").get("submissionCalendar").asText("No Submisison!");

            UserSubmissionCalendar calendar = parseSubmissionCalendar(submissionCalendarString);
            return calendar;
        } catch (JsonProcessingException exception){
            return null;
        }
    }


    public void getAllQuestionCount(){
        String queryString = String.format("{\"query\":\"query { allQuestionsCount { difficulty count } }");

        String jsonString = queryFromGraphQL(queryString);

        if("Error".equals(jsonString)){
            //error status
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode countNode = rootNode.get("data").get(ALLQUESTIONSCOUNT);

            allQuestionCounts.setAllCount(countNode.get(0).get("count").asInt(0));
            allQuestionCounts.setEasyCount(countNode.get(1).get("count").asInt(0));
            allQuestionCounts.setAllCount(countNode.get(2).get("count").asInt(0));
            allQuestionCounts.setAllCount(countNode.get(3).get("count").asInt(0));

        } catch (JsonProcessingException exception){

        }
    }



    //PRIVATE METHODS

    private String queryFromGraphQL(String queryString){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(queryString, mediaType);

        Request request = new Request.Builder()
                .url("https://leetcode.com/graphql/")
                .method("POST", body)
                //.addHeader("referer", String.format("https://leetcode.com/%s/", username))
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();

            //get response
            String responseString = response.body().string();
            return responseString;

        } catch (IOException exception){
            return "Error";
        }
    }


    private UserSubmissionCalendar parseSubmissionCalendar(String string){

        UserSubmissionCalendar calendar = new UserSubmissionCalendar();
        System.out.println("Parsing " + string);

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
            LocalDate day = METADATE.plusDays(days);

            calendar.addSubmission(day, count);
        }

        //need to sort calendar
        calendar.sortCalendar();
        return calendar;
    }
}
