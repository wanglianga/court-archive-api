package com.court.archive.dto;

import java.time.LocalDateTime;

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

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }
    public String getCaseName() { return caseName; }
    public void setCaseName(String caseName) { this.caseName = caseName; }
    public String getBoxNo() { return boxNo; }
    public void setBoxNo(String boxNo) { this.boxNo = boxNo; }
    public String getBorrowerId() { return borrowerId; }
    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
    public LocalDateTime getBorrowTime() { return borrowTime; }
    public void setBorrowTime(LocalDateTime borrowTime) { this.borrowTime = borrowTime; }
    public LocalDateTime getDueTime() { return dueTime; }
    public void setDueTime(LocalDateTime dueTime) { this.dueTime = dueTime; }
    public long getOverdueDays() { return overdueDays; }
    public void setOverdueDays(long overdueDays) { this.overdueDays = overdueDays; }
    public String getLastActionType() { return lastActionType; }
    public void setLastActionType(String lastActionType) { this.lastActionType = lastActionType; }
    public String getLastActionDesc() { return lastActionDesc; }
    public void setLastActionDesc(String lastActionDesc) { this.lastActionDesc = lastActionDesc; }
    public String getLastOperator() { return lastOperator; }
    public void setLastOperator(String lastOperator) { this.lastOperator = lastOperator; }
    public LocalDateTime getLastActionTime() { return lastActionTime; }
    public void setLastActionTime(LocalDateTime lastActionTime) { this.lastActionTime = lastActionTime; }
}
