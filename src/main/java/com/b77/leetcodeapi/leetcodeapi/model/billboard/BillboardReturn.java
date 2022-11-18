package com.b77.leetcodeapi.leetcodeapi.model.billboard;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BillboardReturn {

    LocalDate leetcodeDate;

    List<BillboardEntry> billEntryList;

    public BillboardReturn(){
        leetcodeDate = LocalDateTime.now().plusHours(7).toLocalDate();
        billEntryList = new ArrayList<>();
    }
}
