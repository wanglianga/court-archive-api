package com.court.archive.entity;

import com.court.archive.enums.BoxStatus;

public class ArchiveBox {
    private String boxNo;
    private String caseNo;
    private String boxName;
    private BoxStatus status;
    private String location;
    private String currentResponsible;

    public String getBoxNo() { return boxNo; }
    public void setBoxNo(String boxNo) { this.boxNo = boxNo; }
    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }
    public String getBoxName() { return boxName; }
    public void setBoxName(String boxName) { this.boxName = boxName; }
    public BoxStatus getStatus() { return status; }
    public void setStatus(BoxStatus status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCurrentResponsible() { return currentResponsible; }
    public void setCurrentResponsible(String currentResponsible) { this.currentResponsible = currentResponsible; }
}
