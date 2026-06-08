package com.court.archive.entity;

import com.court.archive.enums.BorrowStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowRecord {
    private String recordId;
    private String caseNo;
    private String boxNo;
    private String borrowerId;
    private String borrowerName;
    private BorrowStatus status;
    private LocalDateTime borrowTime;
    private LocalDateTime dueTime;
    private LocalDateTime returnTime;
    private String approver;
    private String remark;
}
