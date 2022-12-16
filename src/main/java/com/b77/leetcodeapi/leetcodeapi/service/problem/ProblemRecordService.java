package com.b77.leetcodeapi.leetcodeapi.service.problem;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.*;
import com.b77.leetcodeapi.leetcodeapi.model.request.ProblemQueryRequest;
import com.b77.leetcodeapi.leetcodeapi.model.entity.user.UserEntry;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.problem.ProblemSolveRecordRepository;
import com.b77.leetcodeapi.leetcodeapi.repository.user.UserRepository;
import com.b77.leetcodeapi.leetcodeapi.service.leetcode.LeetcodeProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ProblemRecordService {

    @Autowired
    private LeetcodeProblemService leetcodeProblemService;

    @Autowired
    private TopicTagService topicTagService;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemRecordRepository problemRecordRepository;

    @Autowired
    private ProblemSolveRecordRepository problemSolveRecordRepository;

    @Autowired
    private UserRepository userRepository;


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
                    TopicTag queriedTopicTag = topicTagService.getTopicTagByName(topicTagName);
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
        }

        //return the existing one
        return problemRecord;
    }


    public Page<ProblemRecord> getAllProblemRecordOfUser(UserEntry userEntry, int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("problemEntry.frontendID").ascending());

        Page<ProblemRecord> problemRecordList = problemRecordRepository.getByUserEntryAndAcceptedCountIsGreaterThan(userEntry, 0, pageable);

        return problemRecordList;
    }


    public List<ProblemRecord> getAllProblemRecordOfUser(UserEntry userEntry){

        List<ProblemRecord> problemRecordList = problemRecordRepository.getByUserEntryAndAcceptedCountIsGreaterThan(userEntry, 0);

        return problemRecordList;
    }


    public Page<ProblemRecord> getAllProblemRecordOfUserByTopicTag(UserEntry userEntry, int page, int size, String topicTagName){

        Pageable pageable = PageRequest.of(page, size, Sort.by("problemEntry.frontendID").ascending());
        TopicTag topicTag = topicTagService.getTopicTagByName(topicTagName);

        Page<ProblemRecord> problemRecordList = problemRecordRepository.getByUserAndTopicTag(userEntry.getId(), topicTag.getId(), pageable);

        return problemRecordList;
    }



    public List<ProblemRecord> getAllProblemRecordOfUserByTopicTag(UserEntry userEntry, TopicTag topicTag){

        List<ProblemRecord> problemRecordList = problemRecordRepository.getByUserAndTopicTag(userEntry.getId(), topicTag.getId());

        return problemRecordList;
    }



    public ProblemRecord updateProblemRecord(ProblemRecord problemRecord){

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

        //update topic record
        for(TopicTag topicTag: problemRecord.getProblemEntry().getTopicTags()){

            List<ProblemRecord> problemRecordList = this.getAllProblemRecordOfUserByTopicTag(problemRecord.getUserEntry(), topicTag);
            //update topic tag record
            topicTagService.updateTopicRecord(problemRecord.getUserEntry(), topicTag, problemRecordList);
        }

        //update user stats
        this.updateUserStats(problemRecord.getUserEntry());

        return problemRecord;
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

        //no need to create topic record here as we create all at once

        return problemRecord;
    }


    private void updateUserStats(UserEntry userEntry){
        //System.out.println("Updating user stats");
        List<ProblemRecord> problemRecordList = this.getAllProblemRecordOfUser(userEntry);

        int easy = 0, medium = 0, hard = 0;
        for(ProblemRecord problemRecord: problemRecordList){
            if(problemRecord.getProblemEntry().getDifficulty() == 1){
                easy++;
            } else if(problemRecord.getProblemEntry().getDifficulty() == 2){
                medium++;
            } else {
                hard++;
            }
        }

        userEntry.setEasyAC(easy);
        userEntry.setMediumAC(medium);
        userEntry.setHardAC(hard);
        userEntry.setAllAC(problemRecordList.size());

        userRepository.save(userEntry);
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
