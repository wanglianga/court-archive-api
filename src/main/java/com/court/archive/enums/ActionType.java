package com.court.archive.enums;

public enum ActionType {
    CREATE_CASE("创建案卷"),
    BORROW_REQUEST("借阅申请"),
    APPROVE_BORROW("批准借阅"),
    REJECT_BORROW("拒绝借阅"),
    PICK_UP("取走卷宗"),
    RETURN_REQUEST("归还申请"),
    CONFIRM_RETURN("确认归还"),
    OVERDUE_REMIND("逾期提醒"),
    EXCEPTION_HANDLE("异常处理");

    private final String desc;

    ActionType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
