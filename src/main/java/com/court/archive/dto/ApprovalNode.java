package com.court.archive.dto;

public class ApprovalNode {
    private Integer order;
    private String nodeName;
    private String roleName;
    private String expectedHandler;
    private String nextAction;
    private Integer estimatedDays;
    private String description;
    private boolean skipped;
    private String skipReason;
    private boolean countersign;
    private Integer countersignCount;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getExpectedHandler() {
        return expectedHandler;
    }

    public void setExpectedHandler(String expectedHandler) {
        this.expectedHandler = expectedHandler;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public String getSkipReason() {
        return skipReason;
    }

    public void setSkipReason(String skipReason) {
        this.skipReason = skipReason;
    }

    public boolean isCountersign() {
        return countersign;
    }

    public void setCountersign(boolean countersign) {
        this.countersign = countersign;
    }

    public Integer getCountersignCount() {
        return countersignCount;
    }

    public void setCountersignCount(Integer countersignCount) {
        this.countersignCount = countersignCount;
    }
}
