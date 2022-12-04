package com.b77.leetcodeapi.leetcodeapi.service.leetcode;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.TopicTagRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class LeetcodeProblemService {

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    TopicTagRepository topicTagRepository;

    @Autowired
    LeetcodeAPIService leetcodeAPIService;




    // ========== config data ==========

    private final String[] CATEGORIES = {
            "algorithms",
            "database" ,
            "shell"};

    //Interval is in hours
    private final int PROBLEM_UPDATE_INTERVAL = 6;
    private final int TOPIC_UPDATE_INTERVAL = 24;





    // ========== public method ==========


    public void updateNewProblems(){

        for(String category: CATEGORIES){

            //query for all problems list
            String jsonString = leetcodeAPIService.getProblemsByCategory(category);

            ObjectMapper mapper = new ObjectMapper();
            List<ProblemEntry> problemEntryList = new ArrayList<>();

            //parsing initial list
            try{
                JsonNode rootNode = mapper.readTree(jsonString);
                JsonNode problemsJsonArrayNode = rootNode.get("stat_status_pairs");

                for(int i = 0; i < problemsJsonArrayNode.size(); i++){
                    JsonNode problemNode = problemsJsonArrayNode.get(i);

                    //skip premium questions
                    if(problemNode.get("paid_only").asBoolean()){
                        continue;
                    }

                    JsonNode statNode = problemNode.get("stat");

                    ProblemEntry problemEntry = ProblemEntry.builder()
                            .questionID(statNode.get("question_id").asInt(0))
                            .frontendID(statNode.get("frontend_question_id").asInt(0))
                            .difficulty(problemNode.get("difficulty").get("level").asInt(-1))
                            .title(statNode.get("question__title").asText("Error"))
                            .titleSlug(statNode.get("question__title_slug").asText("Error"))
                            .category(category)
                            .build();

                    problemEntryList.add(problemEntry);
                }

            } catch (JsonProcessingException exception){
                System.out.println("Json string parse error: " + exception.getMessage());
            }

            //compare with current data and form new problems list
            for(ProblemEntry problemEntry: problemEntryList){
                if(problemRepository.getByTitleSlug(problemEntry.getTitleSlug()) == null){
                    //if we do not have this problem, update this one
                    this.updateProblem(problemEntry);
                }
            }
        }

    }



    public void forceUpdateAllProblems(){
        List<ProblemEntry> problemEntryList = problemRepository.getByFrontendIDGreaterThan(0);

        for(ProblemEntry problemEntry: problemEntryList){
            this.forceUpdateProblem(problemEntry);
        }
    }


    public void updateProblem(ProblemEntry problemEntry){
        updateProblem(problemEntry, false);
    }


    public void updateProblem(ProblemEntry problemEntry, boolean isForced){

        System.out.println("Trying to updating problem " + problemEntry.getFrontendID() + ". " + problemEntry.getTitle());

        if(problemEntry.getTimeStamp() == null ||
                problemEntry.getTimeStamp().plusHours(PROBLEM_UPDATE_INTERVAL).isAfter(LocalDateTime.now()) ||
                isForced
        ){
            //update problem
            //if there is no time stamp
            //or time stamp is overdue
            //or its forced
            this.forceUpdateProblem(problemEntry);
        } else {
            System.out.println("Does not need to update!");
        }
    }



    public ProblemEntry getProblemByTitleSlug(String titleSlug){
        ProblemEntry problemEntry = problemRepository.getByTitleSlug(titleSlug);
        if(problemEntry != null){
            updateProblem(problemEntry);
        }
        return problemEntry;
    }


    public ProblemEntry getProblemByFrontendId(int frontendId){
        ProblemEntry problemEntry = problemRepository.getByFrontendID(frontendId);
        if(problemEntry != null){
            updateProblem(problemEntry);
        }
        return problemEntry;
    }



    public void getAllQuestionCount(){
        String queryString = String.format("{\"query\":\"query { allQuestionsCount { difficulty count } }");

        String jsonString = leetcodeAPIService.queryFromGraphQL(queryString);

        if("Error".equals(jsonString)){
            //error status
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode countNode = rootNode.get("data").get("allQuestionsCount");

            int allCount = countNode.get(0).get("count").asInt(0);
            int easyCount = countNode.get(1).get("count").asInt(0);
            int mediumCount = countNode.get(2).get("count").asInt(0);
            int hardCount = countNode.get(3).get("count").asInt(0);

        } catch (JsonProcessingException exception){

        }
    }



    // ========== PRIVATE METHODS ==========




    private void forceUpdateProblem(ProblemEntry problemEntry){
        System.out.println("Updating problem " + problemEntry.getFrontendID() + ". " + problemEntry.getTitle());

        String jsonString = leetcodeAPIService.getProblemByTitleSlug(problemEntry.getTitleSlug());

        //System.out.println("Queried question string is " + jsonString);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> diffMap = new HashMap<String, Integer>(){{
            put("Error", 0);
            put("Easy", 1);
            put("Medium", 2);
            put("Hard", 3);
        }};

        //parsing initial list
        try{
            JsonNode questionNode = mapper.readTree(jsonString).get("data").get("question");

            //update question id, frontend id
            problemEntry.setQuestionID(questionNode.get("questionId").asInt(0));
            problemEntry.setFrontendID(questionNode.get("questionFrontendId").asInt(0));

            //update difficulty
            String diffString = questionNode.get("difficulty").asText("Error");
            problemEntry.setDifficulty(diffMap.get(diffString));

            //update title and titleslug
            problemEntry.setTitle(questionNode.get("title").asText());
            problemEntry.setTitleSlug(questionNode.get("titleSlug").asText());

            //update url
            problemEntry.setUrl(leetcodeAPIService.getProblemURLPrefix() + problemEntry.getTitleSlug());

            //update likes, dislikes
            problemEntry.setLikes(questionNode.get("likes").asLong());
            problemEntry.setDislikes(questionNode.get("dislikes").asLong());

            //update subs and acs
            long totalSubs = 0, totalAc = 0;
            JsonNode statsNode = mapper.readTree(questionNode.get("stats").asText());

            problemEntry.setTotalSubmissions(statsNode.get("totalSubmissionRaw").asLong());
            problemEntry.setTotalAccepted(statsNode.get("totalAcceptedRaw").asLong());

            //update topicTags
            int topicTagSize = questionNode.get("topicTags").size();

            Set<TopicTag> topicTagsSet = problemEntry.getTopicTags();
            if(topicTagsSet == null){
                topicTagsSet = new HashSet<>();
            }

            for(int i = 0; i < topicTagSize; i++){
                JsonNode curNode = questionNode.get("topicTags").get(i);
                String curTopicTagSlug = curNode.get("slug").asText();
                TopicTag topicTag = null;

                System.out.println("Cur slug is " + curTopicTagSlug);

                //check topic tags first
                if(topicTagRepository.getBySlug(curTopicTagSlug) != null){
                    //topicTag = this.updateTopicTag(curNode.get("name").asText("Error"), curNode.get("slug").asText("Error"));
                    topicTag = topicTagRepository.getBySlug(curTopicTagSlug);
                } else {
                    topicTag = this.createTopicTag(curNode.get("name").asText("Error"), curNode.get("slug").asText("Error"));
                }
                topicTagsSet.add(topicTag);
            }
            problemEntry.setTopicTags(topicTagsSet);

            //update content, no need now
            //problemEntry.setContent(questionNode.get("content").asText("Error in Content!"));

        } catch (JsonProcessingException exception){
            System.out.println("Problem parsing error: " + exception.getMessage());
        }

        //update timestamps
        problemEntry.setTimeStamp(LocalDateTime.now());

        problemRepository.save(problemEntry);

        System.out.println("Successfully updated problem!");
    }



    private TopicTag createTopicTag(String name, String nameSlug){

        System.out.println("Creating topic tag " + name);

        TopicTag topicTag = TopicTag.builder()
                .name(name)
                .slug(nameSlug)
                .timeStamp(LocalDateTime.now())
                .build();

        topicTagRepository.save(topicTag);
        System.out.println("Creating topic tag " + name + " succeed!");
        return topicTag;
    }

    private TopicTag updateTopicTag(String name, String nameSlug){
        TopicTag topicTag = topicTagRepository.getBySlug(nameSlug);

        System.out.println("Updating topic tag " + name);

        //check time
        if (topicTag.getTimeStamp() != null && topicTag.getTimeStamp().plusHours(TOPIC_UPDATE_INTERVAL).isAfter(LocalDateTime.now())) {
            //if it is updated within interval
            return topicTag;
        }

        topicTag.setTimeStamp(LocalDateTime.now());
        topicTag.setName(name);

        topicTagRepository.save(topicTag);
        System.out.println("Updating topic tag " + name + " succeed!");
        return topicTag;
    }
}
