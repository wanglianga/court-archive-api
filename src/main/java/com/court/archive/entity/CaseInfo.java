package com.court.archive.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaseInfo {
    private String caseNo;
    private String caseName;
    private String caseType;
    private String responsiblePerson;
    private LocalDateTime createTime;
}
