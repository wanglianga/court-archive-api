package com.court.archive.service;

import com.court.archive.dto.CaseNumberParseResult;
import com.court.archive.enums.CaseType;
import com.court.archive.enums.SpecialFlag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CaseNumberParserService {

    private static final Pattern STANDARD_PATTERN = Pattern.compile(
            "\\((\\d{4})\\)([\\u4e00-\\u9fa5]*)(\\d*)[\\u4e00-\\u9fa5]*([民初刑知行商海外])[初终复执申]字第(\\d+)号"
    );

    private static final Pattern SIMPLE_PATTERN = Pattern.compile(
            "([民初刑知行商海外])[初终复执申]?(\\d*)[\\u4e00-\\u9fa5]*(\\d{4})第(\\d+)号"
    );

    private static final Pattern BRACKET_YEAR_PATTERN = Pattern.compile(
            "\\((\\d{4})\\)([\\u4e00-\\u9fa5]*)(\\d*)([民初刑知行商海外])[初终复执申]字第(\\d+)号"
    );

    public CaseNumberParseResult parse(String caseNumber) {
        CaseNumberParseResult result = new CaseNumberParseResult();
        result.setCaseNumber(caseNumber);
        result.setValid(false);

        if (caseNumber == null || caseNumber.trim().isEmpty()) {
            result.setErrorMessage("案号不能为空");
            return result;
        }

        String trimmed = caseNumber.trim();
        result.setNormalizedCaseNumber(trimmed);

        List<SpecialFlag> flags = detectSpecialFlags(trimmed);
        result.setSpecialFlags(flags);
        List<String> flagNames = new ArrayList<>();
        for (SpecialFlag flag : flags) {
            flagNames.add(flag.getKeyword() + "(" + flag.getDesc() + ")");
        }
        result.setSpecialFlagNames(flagNames);

        String cleanCaseNumber = removeSpecialFlags(trimmed);

        CaseType caseType = detectCaseType(cleanCaseNumber);
        if (caseType != null) {
            result.setCaseType(caseType);
            result.setCaseTypeName(caseType.getDesc());
        }

        Matcher standardMatcher = STANDARD_PATTERN.matcher(cleanCaseNumber);
        if (standardMatcher.matches()) {
            fillFromStandardPattern(result, standardMatcher);
            return result;
        }

        Matcher bracketMatcher = BRACKET_YEAR_PATTERN.matcher(cleanCaseNumber);
        if (bracketMatcher.matches()) {
            fillFromBracketPattern(result, bracketMatcher);
            return result;
        }

        Matcher simpleMatcher = SIMPLE_PATTERN.matcher(cleanCaseNumber);
        if (simpleMatcher.matches()) {
            fillFromSimplePattern(result, simpleMatcher);
            return result;
        }

        if (caseType != null) {
            result.setValid(true);
            result.setErrorMessage("案号格式非标准格式，但已识别案件类型");
            return result;
        }

        result.setErrorMessage("无法解析案号格式，请检查案号是否正确。正确格式示例：(2025)京0101民初字第123号 或 民初0101民2025第123号");
        return result;
    }

    private void fillFromStandardPattern(CaseNumberParseResult result, Matcher matcher) {
        result.setValid(true);
        result.setYear(Integer.parseInt(matcher.group(1)));
        String courtArea = matcher.group(2);
        String courtNum = matcher.group(3);
        result.setCourtCode(courtArea + courtNum);
        String caseTypeCode = matcher.group(4);
        CaseType caseType = CaseType.fromCode(caseTypeCode);
        if (caseType != null) {
            result.setCaseType(caseType);
            result.setCaseTypeName(caseType.getDesc());
        }
        result.setCaseLevel(detectCaseLevel(result.getCaseNumber()));
        result.setSerialNumber(Integer.parseInt(matcher.group(5)));
    }

    private void fillFromBracketPattern(CaseNumberParseResult result, Matcher matcher) {
        result.setValid(true);
        result.setYear(Integer.parseInt(matcher.group(1)));
        String courtArea = matcher.group(2);
        String courtNum = matcher.group(3);
        result.setCourtCode(courtArea + courtNum);
        String caseTypeCode = matcher.group(4);
        CaseType caseType = CaseType.fromCode(caseTypeCode);
        if (caseType != null) {
            result.setCaseType(caseType);
            result.setCaseTypeName(caseType.getDesc());
        }
        result.setCaseLevel(detectCaseLevel(result.getCaseNumber()));
        result.setSerialNumber(Integer.parseInt(matcher.group(5)));
    }

    private void fillFromSimplePattern(CaseNumberParseResult result, Matcher matcher) {
        result.setValid(true);
        String caseTypeCode = matcher.group(1);
        CaseType caseType = CaseType.fromCode(caseTypeCode);
        if (caseType != null) {
            result.setCaseType(caseType);
            result.setCaseTypeName(caseType.getDesc());
        }
        result.setCourtCode(matcher.group(2));
        result.setYear(Integer.parseInt(matcher.group(3)));
        result.setCaseLevel(detectCaseLevel(result.getCaseNumber()));
        result.setSerialNumber(Integer.parseInt(matcher.group(4)));
    }

    private CaseType detectCaseType(String caseNumber) {
        if (caseNumber.contains("民")) return CaseType.CIVIL;
        if (caseNumber.contains("刑")) return CaseType.CRIMINAL;
        if (caseNumber.contains("行")) return CaseType.ADMINISTRATIVE;
        if (caseNumber.contains("执")) return CaseType.EXECUTION;
        if (caseNumber.contains("商")) return CaseType.COMMERCIAL;
        if (caseNumber.contains("知")) return CaseType.INTELLECTUAL;
        if (caseNumber.contains("海")) return CaseType.MARITIME;
        if (caseNumber.contains("外")) return CaseType.FOREIGN;
        return null;
    }

    private String detectCaseLevel(String caseNumber) {
        if (caseNumber.contains("初")) return "一审";
        if (caseNumber.contains("终")) return "二审";
        if (caseNumber.contains("复")) return "再审";
        if (caseNumber.contains("执")) return "执行";
        if (caseNumber.contains("申")) return "申诉";
        return "一审";
    }

    private List<SpecialFlag> detectSpecialFlags(String caseNumber) {
        List<SpecialFlag> flags = new ArrayList<>();
        for (SpecialFlag flag : SpecialFlag.values()) {
            if (caseNumber.contains(flag.getKeyword())) {
                flags.add(flag);
            }
        }
        return flags;
    }

    private String removeSpecialFlags(String caseNumber) {
        String result = caseNumber;
        for (SpecialFlag flag : SpecialFlag.values()) {
            result = result.replace(flag.getKeyword(), "");
        }
        return result;
    }
}
