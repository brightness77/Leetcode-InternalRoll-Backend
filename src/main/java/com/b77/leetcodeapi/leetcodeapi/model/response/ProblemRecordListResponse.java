package com.b77.leetcodeapi.leetcodeapi.model.response;

import com.b77.leetcodeapi.leetcodeapi.model.entity.problem.ProblemRecord;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class ProblemRecordListResponse {

    private int totalItems;

    private List<ProblemRecord> problemRecordList;

    private int totalPages;

    private int currentPage;

}
