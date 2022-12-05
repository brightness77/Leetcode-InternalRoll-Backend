package com.b77.leetcodeapi.leetcodeapi.service.problem;

import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemEntry;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemRecord;
import com.b77.leetcodeapi.leetcodeapi.model.problem.ProblemSolveRecord;
import com.b77.leetcodeapi.leetcodeapi.model.problem.TopicTag;
import com.b77.leetcodeapi.leetcodeapi.model.request.ProblemQueryRequest;
import com.b77.leetcodeapi.leetcodeapi.model.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemSolveRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.TopicTagRepository;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeAPIService;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeProblemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ProblemRecordService {

    @Autowired
    LeetcodeProblemService leetcodeProblemService;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    TopicTagRepository topicTagRepository;

    @Autowired
    ProblemRecordRepository problemRecordRepository;

    @Autowired
    ProblemSolveRecordRepository problemSolveRecordRepository;


    // ========== public method ==========


    public ProblemRecord getProblemRecordByID(long id){
        return problemRecordRepository.getReferenceById(id);
    }


    public List<ProblemEntry> getRandomProblems(ProblemQueryRequest request){
        //WIP
        return null;
    }


    public ProblemEntry getRandomProblem(ProblemQueryRequest request, UserEntry userEntry){

        //get all problems
        List<ProblemEntry> problemEntryList = (List)problemRepository.findByCategory(request.getCategory());

        //filters
        List<ProblemEntry> filteredList = new ArrayList<>();
        for(ProblemEntry problemEntry: problemEntryList) {

            //filter new problem
            ProblemRecord problemRecord = problemRecordRepository.getByProblemEntryAndUserEntry(problemEntry, userEntry);
            if(problemRecord == null || problemRecord.getAcceptedCount() == 0){
                if(!request.isNewProblem()){
                    continue;
                }
            } else {
                if(request.isNewProblem()){
                    continue;
                }
            }

            //filter topic tags
            if(!request.getTopicTags().isEmpty()){

//                System.out.println("Filtering topic tags!");
//                System.out.println("Problem " + problemEntry.getFrontendID() + " has topic tags of ");
//                for(TopicTag topicTag : problemEntry.getTopicTags()){
//                    System.out.print(topicTag.getName() + ", ");
//                }
//                System.out.println();

                boolean match = false;

                for(String topicTagName: request.getTopicTags()){
                    TopicTag queriedTopicTag = topicTagRepository.getByName(topicTagName);
                    if(problemEntry.getTopicTags().contains(queriedTopicTag)){
                        //System.out.println("Contains topic tag " + queriedTopicTag.getName());
                        match = true;
                        break;
                    }
                }

                if(!match){
                    continue;
                }
            }

            //filter difficulties
            if(!request.getDifficulties().isEmpty() && !request.getDifficulties().contains(0)){

                boolean match = false;

                for(int difficulty: request.getDifficulties()){
                    if(problemEntry.getDifficulty() == difficulty){
                        match = true;
                        break;
                    }
                }

                if(!match){
                    continue;
                }
            }

            //not valid if request a new problem
            if(!request.isNewProblem() && problemRecord != null ){
                //filter proficiencies
                if(problemRecord.getRecentProficiency() < request.getProficiencyLow() || problemRecord.getRecentProficiency() > request.getProficiencyHigh()){
                    continue;
                }

                //filter count
                if(problemRecord.getAcceptedCount() < request.getCountMin() || problemRecord.getAcceptedCount() > request.getCountMax()){
                    continue;
                }
            }

            //frontend method not provided
            //need to be filtered in advance

            filteredList.add(problemEntry);
        }

        //choosing a problem
        if(filteredList.size() == 0){
            return null;
        }

        System.out.println("Size of choice is " + filteredList.size());
        ProblemEntry problemEntry = filteredList.get(new Random().nextInt(filteredList.size()));

        //update problem
        leetcodeProblemService.updateProblem(problemEntry);

        return problemEntry;
    }


    @Transactional
    public ProblemRecord getProblemRecord(UserEntry userEntry, ProblemEntry problemEntry){
        //assuming problem and user are both valid
        ProblemRecord problemRecord = problemRecordRepository.getByProblemEntryAndUserEntry(problemEntry, userEntry);

        //create problem record to specific user if not exists
        if(problemRecord == null){
            //need to create one
            problemRecord = this.createProblemRecord(userEntry, problemEntry);
        } else {
            //need to update record
            problemRecord = this.updateProblemRecord(problemRecord);
        }

        return problemRecord;
    }


    public List<ProblemRecord> getAllProblemRecordOfUser(UserEntry userEntry){
        List<ProblemRecord> problemRecordList = problemRecordRepository.getByUserEntry(userEntry);

        //sort by frontend id
        Comparator<ProblemRecord> myComp = new Comparator<ProblemRecord>() {
            @Override
            public int compare(ProblemRecord o1, ProblemRecord o2) {
                return o1.getProblemEntry().getFrontendID() - o2.getProblemEntry().getFrontendID();
            }
        };

        Collections.sort(problemRecordList, myComp);

        return problemRecordList;
    }






    //================== PRIVATE METHODS ====================



    private ProblemRecord getProblemRecord(ProblemSolveRecord problemSolveRecord){
        return this.getProblemRecord(problemSolveRecord.getProblemRecord().getUserEntry(), problemSolveRecord.getProblemRecord().getProblemEntry());
    }


    private ProblemRecord createProblemRecord(UserEntry userEntry, ProblemEntry problemEntry){
        ProblemRecord problemRecord = ProblemRecord.builder()
                    .acceptedCount(0L)
                    .sumProficiency(0L)
                    .averageProficiency(0.0)
                    .recentProficiency(0)
                    .userEntry(userEntry)
                    .problemEntry(problemEntry)
                    .build();

        problemRecordRepository.save(problemRecord);
        return problemRecord;
    }


    private ProblemRecord updateProblemRecord(ProblemRecord problemRecord){

        List<ProblemSolveRecord> problemSolveRecordList = problemSolveRecordRepository.getByProblemRecord(problemRecord);

        //update count
        long acceptedCount = problemSolveRecordList.size();
        problemRecord.setAcceptedCount(acceptedCount);

        long sumProficiency = 0L;
        int recentProficiency = 0;
        LocalDateTime recentTime = LocalDateTime.MIN;

        for(ProblemSolveRecord problemSolveRecord: problemSolveRecordList){
            //update sum
            sumProficiency += problemSolveRecord.getProficiency();

            //update recent
            if(recentTime.isBefore(problemSolveRecord.getUtcEndTime())){
                //need to update
                recentTime = problemSolveRecord.getUtcEndTime();
                recentProficiency = problemSolveRecord.getProficiency();
            }
        }

        //update sum proficiency and recent proficiency
        problemRecord.setSumProficiency(sumProficiency);
        problemRecord.setRecentProficiency(recentProficiency);

        //update average
        if(acceptedCount != 0){
            problemRecord.setAverageProficiency((double)sumProficiency / acceptedCount);
        }

        problemRecordRepository.save(problemRecord);
        return problemRecord;
    }



    //================== PRIVATE FIELDS ====================


    //tags
    String[] topicTagsTemp = {"Merge Sort",
            "Reservoir Sampling",
            "String",
            "Math",
            "Segment Tree",
            "Design",
            "Game Theory",
            "Hash Table",
            "Backtracking",
            "Matrix",
            "Minimum Spanning Tree",
            "Rolling Hash",
            "Interactive",
            "Tree",
            "Line Sweep",
            "Suffix Array",
            "Combinatorics",
            "Trie",
            "Eulerian Circuit",
            "Ordered Set",
            "Binary Search Tree",
            "Probability and Statistics",
            "Recursion",
            "Stack",
            "Linked List",
            "Binary Indexed Tree",
            "Graph",
            "Randomized",
            "Brainteaser",
            "Breadth-First Search",
            "Union Find",
            "Counting",
            "Quickselect",
            "Bitmask",
            "Two Pointers",
            "Heap (Priority Queue)",
            "Doubly-Linked List",
            "Rejection Sampling",
            "Biconnected Component",
            "Monotonic Queue",
            "Strongly Connected Component",
            "Dynamic Programming",
            "Bit Manipulation",
            "Divide and Conquer",
            "Sliding Window",
            "Topological Sort",
            "Iterator",
            "Hash Function",
            "Bucket Sort",
            "Greedy",
            "Queue",
            "Shortest Path",
            "Data Stream",
            "Enumeration",
            "Depth-First Search",
            "Binary Tree",
            "String Matching",
            "Prefix Sum",
            "Binary Search",
            "Sorting",
            "Array",
            "Geometry",
            "Radix Sort",
            "Simulation",
            "Counting Sort",
            "Monotonic Stack",
            "Memoization",
            "Number Theory",
            "Database",
            "Shell"};

    //List<String> topicTags = new ArrayList<>(Arrays.asList(topicTagsTemp));

}
