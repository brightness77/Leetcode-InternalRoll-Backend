package com.b77.leetcodeapi.leetcodeapi.model.response;

import com.b77.leetcodeapi.leetcodeapi.model.BillboardEntry;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BillboardReturn {

    LocalDate leetcodeDate;

    List<BillboardEntry> billEntryList;

    LocalDateTime timeStamp;

}
