package com.b77.leetcodeapi.leetcodeapi.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDiffACResponse {

    private int easyAC;

    private int easyTotal;

    private int mediumAC;

    private int mediumTotal;

    private int hardAC;

    private int hardTotal;

    private int allAC;

    private int allTotal;

}
