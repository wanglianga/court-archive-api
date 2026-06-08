package com.court.archive.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReturnRequest {
    @NotBlank(message = "借阅记录ID不能为空")
    private String recordId;

    private String returnRemark;
}
