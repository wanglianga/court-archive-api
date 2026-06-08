package com.court.archive.entity;

import com.court.archive.enums.BoxStatus;
import lombok.Data;

@Data
public class ArchiveBox {
    private String boxNo;
    private String caseNo;
    private String boxName;
    private BoxStatus status;
    private String location;
    private String currentResponsible;
}
