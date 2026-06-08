package com.court.archive.dto;

import javax.validation.constraints.NotBlank;

public class BorrowRequest {
    @NotBlank(message = "案号不能为空")
    private String caseNo;

    @NotBlank(message = "卷宗盒编号不能为空")
    private String boxNo;

    @NotBlank(message = "借阅人ID不能为空")
    private String borrowerId;

    private Integer borrowDays;

    private String remark;

    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }
    public String getBoxNo() { return boxNo; }
    public void setBoxNo(String boxNo) { this.boxNo = boxNo; }
    public String getBorrowerId() { return borrowerId; }
    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }
    public Integer getBorrowDays() { return borrowDays; }
    public void setBorrowDays(Integer borrowDays) { this.borrowDays = borrowDays; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
