package com.court.archive.dto;

import com.court.archive.entity.ArchiveBox;
import com.court.archive.entity.CaseInfo;

import java.util.List;

public class CaseDirectory {
    private CaseInfo caseInfo;
    private List<ArchiveBox> boxList;
    private String processHint;

    public CaseInfo getCaseInfo() { return caseInfo; }
    public void setCaseInfo(CaseInfo caseInfo) { this.caseInfo = caseInfo; }
    public List<ArchiveBox> getBoxList() { return boxList; }
    public void setBoxList(List<ArchiveBox> boxList) { this.boxList = boxList; }
    public String getProcessHint() { return processHint; }
    public void setProcessHint(String processHint) { this.processHint = processHint; }
}
