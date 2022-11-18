package com.b77.leetcodeapi.leetcodeapi.service;

import com.b77.leetcodeapi.leetcodeapi.model.billboard.BillboardEntry;
import com.b77.leetcodeapi.leetcodeapi.model.billboard.BillboardReturn;
import com.b77.leetcodeapi.leetcodeapi.model.UserSubmissionCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BillboardService {

    @Autowired
    UserStatService userStatService;




    public BillboardReturn getTopKSubmissionAccount(int k){

        String[] usernames = new String[]{"wzhao37", "hailongl", "hugodai", "leap-code", "lynnromer"};
        List<UserSubmissionCalendar> calendars = new ArrayList<>();

        for(String username: usernames){
            calendars.add(userStatService.getSubmissionDaysByUsername(username, 1));
        }

        PriorityQueue<BillboardEntry> pq = new PriorityQueue<>((a, b) -> a.getCount() - b.getCount());
        for(UserSubmissionCalendar calendar: calendars){
            pq.offer(new BillboardEntry(calendar.getAccountName(), calendar.getSubmissionList().get(0).getCount()));
            //System.out.println("Pushing name " + calendar.getAccountName() + " with submission " + calendar.getSubmissionList().get(0).getCount());
            if(pq.size() > k){
                pq.poll();
            }
        }

        BillboardReturn billboardReturn = new BillboardReturn();
        while(!pq.isEmpty()){
            billboardReturn.getBillEntryList().add(pq.poll());
        }
        Collections.reverse(billboardReturn.getBillEntryList());

        return billboardReturn;
    }
}
