package com.court.archive.dto;

import com.court.archive.enums.ApprovalType;

import java.util.List;

public class ProcessRoutingResult {
    private String caseNumber;
    private String caseTypeName;
    private ApprovalType approvalType;
    private String approvalTypeName;
    private List<ApprovalNode> approvalChain;
    private Integer totalEstimatedDays;
    private List<String> specialFlags;
    private String processDescription;
    private boolean valid;
    private String errorMessage;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public ApprovalType getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(ApprovalType approvalType) {
        this.approvalType = approvalType;
    }

    public String getApprovalTypeName() {
        return approvalTypeName;
    }

    public void setApprovalTypeName(String approvalTypeName) {
        this.approvalTypeName = approvalTypeName;
    }

    public List<ApprovalNode> getApprovalChain() {
        return approvalChain;
    }

    public void setApprovalChain(List<ApprovalNode> approvalChain) {
        this.approvalChain = approvalChain;
    }

    public Integer getTotalEstimatedDays() {
        return totalEstimatedDays;
    }

    public void setTotalEstimatedDays(Integer totalEstimatedDays) {
        this.totalEstimatedDays = totalEstimatedDays;
    }

    public List<String> getSpecialFlags() {
        return specialFlags;
    }

    public void setSpecialFlags(List<String> specialFlags) {
        this.specialFlags = specialFlags;
    }

    public String getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(String processDescription) {
        this.processDescription = processDescription;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
