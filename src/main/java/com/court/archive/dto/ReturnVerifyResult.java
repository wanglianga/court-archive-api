package com.court.archive.dto;

import lombok.Data;

@Data
public class ReturnVerifyResult {
    private String recordId;
    private String caseNo;
    private String caseName;
    private String boxNo;
    private String borrowerName;
    private String currentResponsible;
    private String verifyStatus;
    private String verifyMessage;
}
