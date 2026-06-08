package com.court.archive.enums;

public enum ApprovalType {
    DIRECT_APPROVAL("直接审批", "书记员直接审批"),
    JUDGE_APPROVAL("庭长审批", "庭长审批后生效"),
    PRESIDENT_APPROVAL("分管院长审批", "分管院长审批后生效"),
    FULL_COURT_APPROVAL("审委会审批", "审判委员会审批");

    private final String name;
    private final String desc;

    ApprovalType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
