package com.b77.leetcodeapi.leetcodeapi.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserConfig {

    private final int MAX_LEETCODE_ACCOUNT_PER_USER = 10;

}
