package com.court.archive.enums;

public enum BorrowStatus {
    PENDING("待审批"),
    APPROVED("已批准"),
    REJECTED("已拒绝"),
    BORROWING("借阅中"),
    RETURNED("已归还"),
    OVERDUE("已逾期");

    private final String desc;

    BorrowStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
