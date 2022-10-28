package com.b77.leetcodeapi.leetcodeapi.service;

import com.b77.leetcodeapi.leetcodeapi.repository.AllQuestionCounts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import okhttp3.*;

import java.io.IOException;

@Service
public class StatsService {

    @Autowired
    AllQuestionCounts allQuestionCounts;

    public void getUserStatsByUsername(String username){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");
        String query = String.format("{\"query\":\"query getUserProfile($username: String!) { allQuestionsCount { difficulty count } matchedUser(username: $username) { contributions { points } profile { reputation ranking } submissionCalendar submitStats { acSubmissionNum { difficulty count submissions } totalSubmissionNum { difficulty count submissions } } } } \",\"variables\":{\"username\":\"%s\"}}", username);
        RequestBody body = RequestBody.create(query, mediaType);

        Request request = new Request.Builder()
                .url("https://leetcode.com/graphql/")
                .method("POST", body)
                .addHeader("referer", String.format("https://leetcode.com/%s/", username))
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();

            //get response
            String responseString = response.body().string();
            ObjectMapper mapper = new ObjectMapper();

            //get data node of json
            JsonNode dataNode = mapper.readTree(responseString).get("data");
            return;

        } catch (IOException exception){
            return;
        }
    }

}
