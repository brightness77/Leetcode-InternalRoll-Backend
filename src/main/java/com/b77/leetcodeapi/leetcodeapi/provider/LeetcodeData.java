package com.b77.leetcodeapi.leetcodeapi.provider;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class LeetcodeData {

    private int totalAll;

    private int totalEasy;

    private int totalMedium;

    private int totalHard;

    private LocalDateTime totalCountTimeStamp;

}
