package com.court.archive.dto;

import com.court.archive.enums.ActionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OverdueInfo {
    private String recordId;
    private String caseNo;
    private String caseName;
    private String boxNo;
    private String borrowerId;
    private String borrowerName;
    private LocalDateTime borrowTime;
    private LocalDateTime dueTime;
    private long overdueDays;

    private String lastActionType;
    private String lastActionDesc;
    private String lastOperator;
    private LocalDateTime lastActionTime;
}
