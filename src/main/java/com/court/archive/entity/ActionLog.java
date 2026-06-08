package com.court.archive.entity;

import com.court.archive.enums.ActionType;

import java.time.LocalDateTime;

public class ActionLog {
    private String logId;
    private String caseNo;
    private String boxNo;
    private String recordId;
    private ActionType actionType;
    private String actionDesc;
    private String operator;
    private LocalDateTime actionTime;

    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }
    public String getBoxNo() { return boxNo; }
    public void setBoxNo(String boxNo) { this.boxNo = boxNo; }
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public String getActionDesc() { return actionDesc; }
    public void setActionDesc(String actionDesc) { this.actionDesc = actionDesc; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public LocalDateTime getActionTime() { return actionTime; }
    public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }
}
