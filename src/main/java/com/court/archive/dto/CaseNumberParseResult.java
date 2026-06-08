package com.court.archive.dto;

import com.court.archive.enums.CaseType;
import com.court.archive.enums.SpecialFlag;

import java.util.List;

public class CaseNumberParseResult {
    private boolean valid;
    private String caseNumber;
    private String normalizedCaseNumber;
    private String courtCode;
    private Integer year;
    private CaseType caseType;
    private String caseTypeName;
    private String caseLevel;
    private Integer serialNumber;
    private List<SpecialFlag> specialFlags;
    private List<String> specialFlagNames;
    private String errorMessage;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getNormalizedCaseNumber() {
        return normalizedCaseNumber;
    }

    public void setNormalizedCaseNumber(String normalizedCaseNumber) {
        this.normalizedCaseNumber = normalizedCaseNumber;
    }

    public String getCourtCode() {
        return courtCode;
    }

    public void setCourtCode(String courtCode) {
        this.courtCode = courtCode;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getCaseLevel() {
        return caseLevel;
    }

    public void setCaseLevel(String caseLevel) {
        this.caseLevel = caseLevel;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<SpecialFlag> getSpecialFlags() {
        return specialFlags;
    }

    public void setSpecialFlags(List<SpecialFlag> specialFlags) {
        this.specialFlags = specialFlags;
    }

    public List<String> getSpecialFlagNames() {
        return specialFlagNames;
    }

    public void setSpecialFlagNames(List<String> specialFlagNames) {
        this.specialFlagNames = specialFlagNames;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
