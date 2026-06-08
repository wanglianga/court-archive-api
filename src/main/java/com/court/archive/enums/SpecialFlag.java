package com.court.archive.enums;

public enum SpecialFlag {
    URGENT("紧急", "紧急案件，跳过庭长审批"),
    CONFIDENTIAL("涉密", "涉密案件，增加保密审查节点"),
    MAJOR("重大", "重大案件，需审委会审批"),
    SUMMARY("简易", "简易程序案件，简化审批流程");

    private final String keyword;
    private final String desc;

    SpecialFlag(String keyword, String desc) {
        this.keyword = keyword;
        this.desc = desc;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getDesc() {
        return desc;
    }
}
