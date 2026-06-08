package com.court.archive.dto;

import com.court.archive.entity.ArchiveBox;
import com.court.archive.entity.CaseInfo;
import lombok.Data;

import java.util.List;

@Data
public class CaseDirectory {
    private CaseInfo caseInfo;
    private List<ArchiveBox> boxList;
    private String processHint;
}
