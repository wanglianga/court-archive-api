package com.court.archive.entity;

import com.court.archive.enums.CaseType;

import java.util.List;

public class ApprovalStrategyTemplate {
    private String templateId;
    private String templateName;
    private CaseType caseType;
    private String caseLevel;
    private String physicalLocationRule;
    private String startNodeRole;
    private List<String> requiredApprovalRoles;
    private Integer countersignNodeCount;
    private List<String> jumpConditions;
    private String description;

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public CaseType getCaseType() { return caseType; }
    public void setCaseType(CaseType caseType) { this.caseType = caseType; }

    public String getCaseLevel() { return caseLevel; }
    public void setCaseLevel(String caseLevel) { this.caseLevel = caseLevel; }

    public String getPhysicalLocationRule() { return physicalLocationRule; }
    public void setPhysicalLocationRule(String physicalLocationRule) { this.physicalLocationRule = physicalLocationRule; }

    public String getStartNodeRole() { return startNodeRole; }
    public void setStartNodeRole(String startNodeRole) { this.startNodeRole = startNodeRole; }

    public List<String> getRequiredApprovalRoles() { return requiredApprovalRoles; }
    public void setRequiredApprovalRoles(List<String> requiredApprovalRoles) { this.requiredApprovalRoles = requiredApprovalRoles; }

    public Integer getCountersignNodeCount() { return countersignNodeCount; }
    public void setCountersignNodeCount(Integer countersignNodeCount) { this.countersignNodeCount = countersignNodeCount; }

    public List<String> getJumpConditions() { return jumpConditions; }
    public void setJumpConditions(List<String> jumpConditions) { this.jumpConditions = jumpConditions; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
