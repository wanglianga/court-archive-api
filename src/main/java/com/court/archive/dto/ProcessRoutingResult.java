package com.court.archive.dto;

import com.court.archive.entity.ApprovalStrategyTemplate;
import com.court.archive.entity.ArchiveBox;
import com.court.archive.enums.ApprovalType;
import com.court.archive.enums.CaseType;

import java.util.List;

public class ProcessRoutingResult {
    private String caseNumber;
    private String normalizedCaseNumber;
    private CaseType caseType;
    private String caseTypeName;
    private String caseLevel;
    private String courtCode;
    private String administrativeDivision;
    private Integer year;
    private Integer serialNumberStart;
    private Integer serialNumberEnd;
    private ApprovalType approvalType;
    private String approvalTypeName;
    private List<ApprovalNode> approvalChain;
    private Integer totalEstimatedDays;
    private List<String> specialFlags;
    private String processDescription;
    private boolean valid;
    private String errorMessage;
    private String strategyTemplateId;
    private String strategyTemplateName;
    private ApprovalStrategyTemplate strategyTemplate;
    private List<ArchiveBox> matchedBoxes;
    private Integer matchedBoxCount;
    private String currentExpectedHandler;
    private String currentExpectedHandlerRole;
    private String nextAction;
    private String physicalLocation;
    private String physicalLocationRule;
    private String startNodeRole;
    private List<String> requiredApprovalRoles;
    private Integer countersignNodeCount;
    private List<String> jumpConditions;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getNormalizedCaseNumber() {
        return normalizedCaseNumber;
    }

    public void setNormalizedCaseNumber(String normalizedCaseNumber) {
        this.normalizedCaseNumber = normalizedCaseNumber;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getCaseLevel() {
        return caseLevel;
    }

    public void setCaseLevel(String caseLevel) {
        this.caseLevel = caseLevel;
    }

    public String getCourtCode() {
        return courtCode;
    }

    public void setCourtCode(String courtCode) {
        this.courtCode = courtCode;
    }

    public String getAdministrativeDivision() {
        return administrativeDivision;
    }

    public void setAdministrativeDivision(String administrativeDivision) {
        this.administrativeDivision = administrativeDivision;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSerialNumberStart() {
        return serialNumberStart;
    }

    public void setSerialNumberStart(Integer serialNumberStart) {
        this.serialNumberStart = serialNumberStart;
    }

    public Integer getSerialNumberEnd() {
        return serialNumberEnd;
    }

    public void setSerialNumberEnd(Integer serialNumberEnd) {
        this.serialNumberEnd = serialNumberEnd;
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

    public String getStrategyTemplateId() {
        return strategyTemplateId;
    }

    public void setStrategyTemplateId(String strategyTemplateId) {
        this.strategyTemplateId = strategyTemplateId;
    }

    public String getStrategyTemplateName() {
        return strategyTemplateName;
    }

    public void setStrategyTemplateName(String strategyTemplateName) {
        this.strategyTemplateName = strategyTemplateName;
    }

    public ApprovalStrategyTemplate getStrategyTemplate() {
        return strategyTemplate;
    }

    public void setStrategyTemplate(ApprovalStrategyTemplate strategyTemplate) {
        this.strategyTemplate = strategyTemplate;
    }

    public List<ArchiveBox> getMatchedBoxes() {
        return matchedBoxes;
    }

    public void setMatchedBoxes(List<ArchiveBox> matchedBoxes) {
        this.matchedBoxes = matchedBoxes;
    }

    public Integer getMatchedBoxCount() {
        return matchedBoxCount;
    }

    public void setMatchedBoxCount(Integer matchedBoxCount) {
        this.matchedBoxCount = matchedBoxCount;
    }

    public String getCurrentExpectedHandler() {
        return currentExpectedHandler;
    }

    public void setCurrentExpectedHandler(String currentExpectedHandler) {
        this.currentExpectedHandler = currentExpectedHandler;
    }

    public String getCurrentExpectedHandlerRole() {
        return currentExpectedHandlerRole;
    }

    public void setCurrentExpectedHandlerRole(String currentExpectedHandlerRole) {
        this.currentExpectedHandlerRole = currentExpectedHandlerRole;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public String getPhysicalLocation() {
        return physicalLocation;
    }

    public void setPhysicalLocation(String physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    public String getPhysicalLocationRule() {
        return physicalLocationRule;
    }

    public void setPhysicalLocationRule(String physicalLocationRule) {
        this.physicalLocationRule = physicalLocationRule;
    }

    public String getStartNodeRole() {
        return startNodeRole;
    }

    public void setStartNodeRole(String startNodeRole) {
        this.startNodeRole = startNodeRole;
    }

    public List<String> getRequiredApprovalRoles() {
        return requiredApprovalRoles;
    }

    public void setRequiredApprovalRoles(List<String> requiredApprovalRoles) {
        this.requiredApprovalRoles = requiredApprovalRoles;
    }

    public Integer getCountersignNodeCount() {
        return countersignNodeCount;
    }

    public void setCountersignNodeCount(Integer countersignNodeCount) {
        this.countersignNodeCount = countersignNodeCount;
    }

    public List<String> getJumpConditions() {
        return jumpConditions;
    }

    public void setJumpConditions(List<String> jumpConditions) {
        this.jumpConditions = jumpConditions;
    }
}
