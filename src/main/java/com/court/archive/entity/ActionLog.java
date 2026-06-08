package com.court.archive.entity;

import com.court.archive.enums.ActionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActionLog {
    private String logId;
    private String caseNo;
    private String boxNo;
    private String recordId;
    private ActionType actionType;
    private String actionDesc;
    private String operator;
    private LocalDateTime actionTime;
}
