package com.b77.leetcodeapi.leetcodeapi.service;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.problem.TopicTagEntry;
import com.b77.leetcodeapi.leetcodeapi.model.request.ProblemQueryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProblemService {

    @Autowired
    LeetcodeAPIService leetcodeAPIService;


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


    public ProblemEntry getProblemByID(int problemID) {
        //not implemented for now
        return null;
    }


    public ProblemEntry getProblemByTitleSlug(String titleSlug){

        String jsonString = leetcodeAPIService.getProblemByTitleSlug(titleSlug);

        return null;
    }


    public List<ProblemEntry> getProblemsListByCategory(String category) {

        int categoryIndex = -1;
        if("algorithms".equals(category)){
            categoryIndex = 0;
        } else if ("database".equals(category)){
            categoryIndex = 1;
        } else if ("shell".equals(category)){
            categoryIndex = 2;
        } else {
            return null;
        }

        if (allProblems[categoryIndex] != null && allProblemsTimestamp[categoryIndex] != null && allProblemsTimestamp[categoryIndex].plusHours(1L).isAfter(LocalDateTime.now())) {
            //we have problems recorded, and its not expired
            return allProblems[categoryIndex];
        }

        String jsonString = leetcodeAPIService.getProblemsByCategory(category);

        ObjectMapper mapper = new ObjectMapper();

        List<ProblemEntry> problemEntryList = new ArrayList<>();

        //parsing initial list
        try{
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode problemsJsonArrayNode = rootNode.get("stat_status_pairs");

            for(int i = 0; i < problemsJsonArrayNode.size(); i++){
                JsonNode problemNode = problemsJsonArrayNode.get(i);

                //skip premium questions for now
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
                        .topicTags(new ArrayList<>())
                        .build();

                problemEntryList.add(problemEntry);
            }

        } catch (JsonProcessingException exception){
            System.out.println(exception.getMessage());
            return null;
        }

        //try update each question
        //updating here is vert slow!!!
//        for(Problem problem: problemList){
//            updateProblemByTitleSlug(problem);
//        }

        //successfully updated
        allProblems[categoryIndex] = problemEntryList;
        allProblemsTimestamp[categoryIndex] = LocalDateTime.now();

        return problemEntryList;

    }


    public ProblemEntry updateProblem(ProblemEntry problemEntry){
        String jsonString = leetcodeAPIService.getProblemByTitleSlug(problemEntry.getTitleSlug());

        //System.out.println(jsonString);

        ObjectMapper mapper = new ObjectMapper();

        //parsing initial list
        try{
            JsonNode questionNode = mapper.readTree(jsonString).get("data").get("question");

            //update content
            problemEntry.setContent(questionNode.get("content").asText("Error in Content!"));

            //System.out.println("size of problem " + problem.getTitle() + " got topic tag size of " + questionNode.get("topicTags").size());

            //update topicTags
            for(int i1 = 0; i1 < questionNode.get("topicTags").size(); i1++){
                String topicTag = questionNode.get("topicTags").get(i1).get("name").asText("Error");
                problemEntry.getTopicTags().add(topicTag);
            }

        } catch (JsonProcessingException exception){
            System.out.println(exception.getMessage());
        }

        return problemEntry;
    }


    public ProblemEntry getRandomProblem(ProblemQueryRequest request){

        //first get all problems
        List<ProblemEntry> problemEntryList = new ArrayList<>();
        for(String category: request.getCategories()){
            problemEntryList.addAll(getProblemsListByCategory(category));
        }

        //filter topic tags
        //not valid now
        if(false){
            List<ProblemEntry> tempList1 = new ArrayList<>();
            Set<String> topicTagSet = new HashSet<>(request.getTopicTags());

            for(ProblemEntry problemEntry : problemEntryList){
                for(String topicTag: problemEntry.getTopicTags()){
                    if(topicTagSet.contains(topicTag)){
                        tempList1.add(problemEntry);
                        break;
                    }
                }
            }
            problemEntryList = tempList1;
        }

        //filter companies
        //not provided now

        //filter difficulties
        List<ProblemEntry> tempList3 = new ArrayList<>();
        boolean[] diffFilter = new boolean[4];
        for(int difficulty: request.getDifficulties()){
            diffFilter[difficulty] = true;
        }

        for(ProblemEntry problemEntry : problemEntryList){
            if(diffFilter[0] || diffFilter[problemEntry.getDifficulty()]){
                tempList3.add(problemEntry);
            }
        }
        problemEntryList = tempList3;

        if(problemEntryList.size() == 0){
            return null;
        }

        //choose a problem
        Random random = new Random();
        ProblemEntry problemEntry = problemEntryList.get(random.nextInt(problemEntryList.size()));

        //update problem
        updateProblem(problemEntry);

        return problemEntry;
    }




    //================== PRIVATE METHODS ====================


    private List<String> testAllTopicTags(){
        List<ProblemEntry> problemEntryList = getProblemsListByCategory("shell");

        for(ProblemEntry problemEntry: problemEntryList){
            System.out.println("Updating problem" + problemEntry.getFrontendID());
            updateProblem(problemEntry);
        }

        Set<String> set = new HashSet<>();
        for(ProblemEntry problemEntry: problemEntryList){
            for(String topic: problemEntry.getTopicTags()){
                set.add(topic);
            }
        }

        return new ArrayList<>(set);
    }




    //================== PRIVATE FIELDS ====================

    //temp hardcode part
    private List<ProblemEntry>[] allProblems = new List[3];
    private LocalDateTime[] allProblemsTimestamp = new LocalDateTime[3];
}
