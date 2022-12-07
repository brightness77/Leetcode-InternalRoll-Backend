package com.b77.leetcodeapi.leetcodeapi.service;

import com.b77.leetcodeapi.leetcodeapi.model.billboard.BillboardEntry;
import com.b77.leetcodeapi.leetcodeapi.model.billboard.BillboardReturn;
import com.b77.leetcodeapi.leetcodeapi.model.UserSubmissionCalendar;
import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BillboardService {

    // ========== private configs ==========

    private final String[] RANK_USERNAMES = new String[]{"wzhao37", "hailongl", "hugodai", "leap-code", "lynnromer"};

    //in seconds
    private final int BILLBOARD_RETURN_INTERVALS = 60;



    @Autowired
    UserStatService userStatService;



    public BillboardReturn getTopKSubmissionAccount(int k){

        if(billboardReturnMap.containsKey(k)){
            BillboardReturn storedReturn = billboardReturnMap.get(k);
            if(storedReturn.getTimeStamp().plusSeconds(BILLBOARD_RETURN_INTERVALS).isAfter(LocalDateTime.now())) {
                //which means it does not need to update
                return storedReturn;
            }
        }

        String[] usernames = RANK_USERNAMES;
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

        BillboardReturn billboardReturn = BillboardReturn.builder()
                .leetcodeDate(LocalDateTime.now().plusHours(userStatService.getDIFF_TO_UTC()).toLocalDate())
                .billEntryList(new ArrayList<>())
                .timeStamp(LocalDateTime.now())
                .build();

        while(!pq.isEmpty()){
            billboardReturn.getBillEntryList().add(pq.poll());
        }
        Collections.reverse(billboardReturn.getBillEntryList());

        //add it to map
        billboardReturnMap.put(k, billboardReturn);

        return billboardReturn;
    }



    // ========== private fields ==========

    //cache for billboard return
    private Map<Integer, BillboardReturn> billboardReturnMap = new HashMap<>();

}
