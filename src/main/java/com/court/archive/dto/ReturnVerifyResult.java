package com.court.archive.dto;

public class ReturnVerifyResult {
    private String recordId;
    private String caseNo;
    private String caseName;
    private String boxNo;
    private String borrowerName;
    private String currentResponsible;
    private String verifyStatus;
    private String verifyMessage;

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }
    public String getCaseName() { return caseName; }
    public void setCaseName(String caseName) { this.caseName = caseName; }
    public String getBoxNo() { return boxNo; }
    public void setBoxNo(String boxNo) { this.boxNo = boxNo; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
    public String getCurrentResponsible() { return currentResponsible; }
    public void setCurrentResponsible(String currentResponsible) { this.currentResponsible = currentResponsible; }
    public String getVerifyStatus() { return verifyStatus; }
    public void setVerifyStatus(String verifyStatus) { this.verifyStatus = verifyStatus; }
    public String getVerifyMessage() { return verifyMessage; }
    public void setVerifyMessage(String verifyMessage) { this.verifyMessage = verifyMessage; }
}
