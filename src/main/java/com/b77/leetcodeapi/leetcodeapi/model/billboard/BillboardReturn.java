package com.b77.leetcodeapi.leetcodeapi.model.billboard;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BillboardReturn {

    LocalDate leetcodeDate;

    List<BillboardEntry> billEntryList;

    LocalDateTime timeStamp;

}
