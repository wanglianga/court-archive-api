package com.court.archive.dto;

import javax.validation.constraints.NotBlank;

public class CaseNumberParseRequest {
    @NotBlank(message = "案号不能为空")
    private String caseNumber;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
}
