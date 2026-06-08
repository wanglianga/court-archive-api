package com.court.archive.enums;

public enum CaseType {
    CIVIL("民", "民事案件"),
    CRIMINAL("刑", "刑事案件"),
    ADMINISTRATIVE("行", "行政案件"),
    EXECUTION("执", "执行案件"),
    COMMERCIAL("商", "商事案件"),
    INTELLECTUAL("知", "知识产权案件"),
    MARITIME("海", "海事案件"),
    FOREIGN("外", "涉外案件");

    private final String code;
    private final String desc;

    CaseType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CaseType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (CaseType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
