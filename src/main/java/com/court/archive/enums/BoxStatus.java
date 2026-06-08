package com.court.archive.enums;

public enum BoxStatus {
    IN_STOCK("在库"),
    BORROWED("已借出"),
    APPROVING("审批中"),
    LOST("遗失"),
    OVERDUE("逾期未还");

    private final String desc;

    BoxStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
