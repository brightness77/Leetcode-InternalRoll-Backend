package com.b77.leetcodeapi.leetcodeapi.service.leetcode;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;


import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class LeetcodeAPIService {

    //config data
    private final String[] LANGS = {
            "bash",
            "c",
            "cpp",
            "csharp",
            "golang",
            "java",
            "javascript",
            "kotlin",
            "mysql",
            "php",
            "python",
            "python3",
            "ruby",
            "rust",
            "scala",
            "swift"
    };

    private final Map<String, String> URLS = new HashMap<String, String>() {{
        put("base", "https://leetcode.com");
        put("graphql", "https://leetcode.com/graphql");
        put("login", "https://leetcode.com/accounts/login/");
        put("problems", "https://leetcode.com/api/problems/$category/");
        put("problem", "https://leetcode.com/problems/$slug/description/");
        put("problemPrefix", "https://leetcode.com/problems/");
        put("test", "https://leetcode.com/problems/$slug/interpret_solution/");
        put("session", "https://leetcode.com/session/");
        put("submit", "https://leetcode.com/problems/$slug/submit/");
        put("submissions", "https://leetcode.com/api/submissions/$slug");
        put("submission", "https://leetcode.com/submissions/detail/$id/");
        put("verify", "https://leetcode.com/submissions/detail/$id/check/");
        put("favorites" ,"https://leetcode.com/list/api/questions");
        put("favorite_delete", "https://leetcode.com/list/api/questions/$hash/$id");
    }};





    public String getProblemURLPrefix(){
        return URLS.get("problemPrefix");
    }


    /*
    Query from leetcode api to get a category of problem
    Return Json String as result
     */
    public String getProblemsByCategory(String category){

        MediaType mediaType = MediaType.parse("application/json");

        String curUrl = URLS.get("problems");
        curUrl = curUrl.replace("$category", category);

        Request request = new Request.Builder()
                .url(curUrl)
                .method("GET", null)
                //.addHeader("referer", String.format("https://leetcode.com/%s/", username))
                .addHeader("Content-Type", "application/json")

                .build();

        String result = sendHttpRequest(request);
        return result;

    }


    public String getProblemByTitleSlug(String titleSlug){
        String queryString = String.format("{ \"query\": \"query getQuestionDetail($titleSlug: String!) " +
                "{ question(titleSlug: $titleSlug) " +
                "{ questionId " +
                "questionFrontendId " +
                "questionType " +
                "canSeeQuestion " +
                "difficulty " +
                "title titleSlug " +
                "content " +
                "stats " +
                "codeDefinition " +
                "sampleTestCase " +
                "enableRunCode " +
                "metaData " +
                "frequency " +
                "likes " +
                "dislikes " +
                "similarQuestions " +
                "submitUrl " +
                "topicTags { name slug translatedName __typename } " +
                "companyTags { name frequencies } " +
                "solution { content } " +
                "} " +
                "}\" ," +
                "\"variables\": { \"titleSlug\": \"%s\" }}",
                titleSlug);

        //System.out.println("query string is " + queryString);

        return queryFromGraphQL(queryString);
    }



    public String getUserStatsByUsername(String username){

        String queryString = String.format("{\"query\":\"query getUserProfile($username: String!) { " +
                "allQuestionsCount { difficulty count } " +
                "matchedUser(username: $username) { " +
                "contributions { points } " +
                "profile { reputation ranking } " +
                "submissionCalendar submitStats { " +
                "acSubmissionNum { difficulty count submissions } " +
                "totalSubmissionNum { difficulty count submissions } } } }" +
                " \",\"variables\":{\"username\":\"%s\"}}",
                username);

        return queryFromGraphQL(queryString);

    }



    //================== PRIVATE METHODS =====================



    /*
    Query from leetcode graphql service
    Return Json String as result
    Return "Error" if error detected
     */
    public String queryFromGraphQL(String queryString){

        System.out.println("Queried from leetcode.com");
        //System.out.println(queryString);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(queryString, mediaType);

        String curUrl = URLS.get("graphql");

        Request request = new Request.Builder()
                .url(curUrl)
                .method("POST", body)
                //.addHeader("referer", String.format("https://leetcode.com/%s/", username))
                .addHeader("Content-Type", "application/json")
                .build();

        String responseString = sendHttpRequest(request);

        return responseString;
    }


    /*
    Simple http request sender
     */

    private String sendHttpRequest(Request request){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        try {
            Response response = client.newCall(request).execute();

            //get response
            String responseString = response.body().string();

            //error handling
            //returning type is html page
            if(responseString.contains("<!DOCTYPE html>")){
                return "Error";
            }

            return responseString;

        } catch (IOException exception){
            return "Error";
        }
    }


}
