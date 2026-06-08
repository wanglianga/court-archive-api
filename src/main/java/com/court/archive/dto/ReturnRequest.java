package com.court.archive.dto;

import javax.validation.constraints.NotBlank;

public class ReturnRequest {
    @NotBlank(message = "借阅记录ID不能为空")
    private String recordId;

    private String returnRemark;

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public String getReturnRemark() { return returnRemark; }
    public void setReturnRemark(String returnRemark) { this.returnRemark = returnRemark; }
}
