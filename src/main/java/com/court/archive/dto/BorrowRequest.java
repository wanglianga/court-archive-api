package com.court.archive.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BorrowRequest {
    @NotBlank(message = "案号不能为空")
    private String caseNo;

    @NotBlank(message = "卷宗盒编号不能为空")
    private String boxNo;

    @NotBlank(message = "借阅人ID不能为空")
    private String borrowerId;

    private Integer borrowDays;

    private String remark;
}
