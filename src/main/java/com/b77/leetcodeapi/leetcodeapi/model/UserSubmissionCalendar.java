package com.b77.leetcodeapi.leetcodeapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class UserSubmissionCalendar {

    private String accountName;

    private List<Submission> submissionList;

    public UserSubmissionCalendar(String accountName){
        this.accountName = accountName;
        submissionList = new ArrayList<>();
    }

    public void addSubmission(LocalDate date, int count){
        submissionList.add(new Submission(date, count));
    }

    public void addSubmission(Submission submission){
        submissionList.add(submission);
    }


    public void sortCalendar(){
        Collections.sort(submissionList);
    }
}
