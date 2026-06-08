package com.court.archive.service;

import com.court.archive.dto.ApprovalNode;
import com.court.archive.dto.CaseNumberParseResult;
import com.court.archive.dto.ProcessRoutingResult;
import com.court.archive.enums.ApprovalType;
import com.court.archive.enums.CaseType;
import com.court.archive.enums.SpecialFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessRoutingService {

    @Autowired
    private CaseNumberParserService caseNumberParserService;

    public ProcessRoutingResult route(String caseNumber) {
        ProcessRoutingResult result = new ProcessRoutingResult();
        result.setCaseNumber(caseNumber);
        result.setValid(false);

        CaseNumberParseResult parseResult = caseNumberParserService.parse(caseNumber);

        if (!parseResult.isValid() && parseResult.getCaseType() == null) {
            result.setErrorMessage(parseResult.getErrorMessage());
            return result;
        }

        result.setValid(true);
        CaseType caseType = parseResult.getCaseType();
        if (caseType != null) {
            result.setCaseTypeName(caseType.getDesc());
        }

        List<SpecialFlag> flags = parseResult.getSpecialFlags();
        List<String> flagNames = new ArrayList<>();
        for (SpecialFlag flag : flags) {
            flagNames.add(flag.getKeyword() + "(" + flag.getDesc() + ")");
        }
        result.setSpecialFlags(flagNames);

        ApprovalType approvalType = determineApprovalType(caseType, flags);
        result.setApprovalType(approvalType);
        result.setApprovalTypeName(approvalType.getName());

        List<ApprovalNode> approvalChain = buildApprovalChain(approvalType, caseType, flags);
        result.setApprovalChain(approvalChain);

        int totalDays = 0;
        for (ApprovalNode node : approvalChain) {
            if (!node.isSkipped() && node.getEstimatedDays() != null) {
                totalDays += node.getEstimatedDays();
            }
        }
        result.setTotalEstimatedDays(totalDays);

        result.setProcessDescription(buildProcessDescription(caseType, approvalType, flags, totalDays));

        return result;
    }

    private ApprovalType determineApprovalType(CaseType caseType, List<SpecialFlag> flags) {
        if (flags != null && flags.contains(SpecialFlag.MAJOR)) {
            return ApprovalType.FULL_COURT_APPROVAL;
        }

        if (caseType == null) {
            return ApprovalType.DIRECT_APPROVAL;
        }

        switch (caseType) {
            case CIVIL:
            case COMMERCIAL:
            case INTELLECTUAL:
                return ApprovalType.JUDGE_APPROVAL;
            case CRIMINAL:
            case ADMINISTRATIVE:
                return ApprovalType.PRESIDENT_APPROVAL;
            case EXECUTION:
            case MARITIME:
            case FOREIGN:
                return ApprovalType.JUDGE_APPROVAL;
            default:
                return ApprovalType.DIRECT_APPROVAL;
        }
    }

    private List<ApprovalNode> buildApprovalChain(ApprovalType approvalType, CaseType caseType, List<SpecialFlag> flags) {
        List<ApprovalNode> chain = new ArrayList<>();
        int order = 1;

        boolean hasConfidential = flags != null && flags.contains(SpecialFlag.CONFIDENTIAL);
        boolean hasUrgent = flags != null && flags.contains(SpecialFlag.URGENT);
        boolean hasSummary = flags != null && flags.contains(SpecialFlag.SUMMARY);
        boolean hasMajor = flags != null && flags.contains(SpecialFlag.MAJOR);

        if (hasConfidential) {
            ApprovalNode securityNode = new ApprovalNode();
            securityNode.setOrder(order++);
            securityNode.setNodeName("保密审查");
            securityNode.setRoleName("保密专员");
            securityNode.setEstimatedDays(1);
            securityNode.setDescription("涉密案件需先进行保密审查，确认信息安全等级");
            securityNode.setSkipped(false);
            chain.add(securityNode);
        }

        ApprovalNode clerkNode = new ApprovalNode();
        clerkNode.setOrder(order++);
        clerkNode.setNodeName("立案登记");
        clerkNode.setRoleName("书记员");
        clerkNode.setEstimatedDays(1);
        clerkNode.setDescription("书记员对案件材料进行形式审查和登记立案");
        clerkNode.setSkipped(false);
        chain.add(clerkNode);

        if (hasSummary) {
            ApprovalNode summaryJudgeNode = new ApprovalNode();
            summaryJudgeNode.setOrder(order++);
            summaryJudgeNode.setNodeName("独任法官审批");
            summaryJudgeNode.setRoleName("独任法官");
            summaryJudgeNode.setEstimatedDays(1);
            summaryJudgeNode.setDescription("简易程序案件，由独任法官直接审批");
            summaryJudgeNode.setSkipped(false);
            chain.add(summaryJudgeNode);
        } else if (approvalType == ApprovalType.DIRECT_APPROVAL) {
            ApprovalNode directNode = new ApprovalNode();
            directNode.setOrder(order++);
            directNode.setNodeName("直接审批");
            directNode.setRoleName("书记员");
            directNode.setEstimatedDays(1);
            directNode.setDescription("简单案件由书记员直接审批完成");
            directNode.setSkipped(false);
            chain.add(directNode);
        } else {
            if (hasUrgent) {
                ApprovalNode judgeNode = new ApprovalNode();
                judgeNode.setOrder(order++);
                judgeNode.setNodeName("庭长审批");
                judgeNode.setRoleName("庭长");
                judgeNode.setEstimatedDays(3);
                judgeNode.setDescription("庭长审批环节");
                judgeNode.setSkipped(true);
                judgeNode.setSkipReason("紧急案件，已跳过庭长审批");
                chain.add(judgeNode);
            } else if (approvalType == ApprovalType.JUDGE_APPROVAL || approvalType == ApprovalType.PRESIDENT_APPROVAL || approvalType == ApprovalType.FULL_COURT_APPROVAL) {
                ApprovalNode judgeNode = new ApprovalNode();
                judgeNode.setOrder(order++);
                judgeNode.setNodeName("庭长审批");
                judgeNode.setRoleName("庭长");
                judgeNode.setEstimatedDays(3);
                judgeNode.setDescription("庭长对案件进行审核，确认案件事实和法律适用");
                judgeNode.setSkipped(false);
                chain.add(judgeNode);
            }

            if (approvalType == ApprovalType.PRESIDENT_APPROVAL || approvalType == ApprovalType.FULL_COURT_APPROVAL) {
                ApprovalNode presidentNode = new ApprovalNode();
                presidentNode.setOrder(order++);
                presidentNode.setNodeName("分管院长审批");
                presidentNode.setRoleName("分管副院长");
                presidentNode.setEstimatedDays(3);
                presidentNode.setDescription("分管副院长对重大敏感案件进行审批");
                presidentNode.setSkipped(false);
                chain.add(presidentNode);
            }

            if (hasMajor || approvalType == ApprovalType.FULL_COURT_APPROVAL) {
                ApprovalNode fullCourtNode = new ApprovalNode();
                fullCourtNode.setOrder(order++);
                fullCourtNode.setNodeName("审判委员会讨论");
                fullCourtNode.setRoleName("审判委员会");
                fullCourtNode.setEstimatedDays(5);
                fullCourtNode.setDescription("重大、疑难、复杂案件提交审判委员会讨论决定");
                fullCourtNode.setSkipped(false);
                chain.add(fullCourtNode);
            }
        }

        ApprovalNode archiveNode = new ApprovalNode();
        archiveNode.setOrder(order++);
        archiveNode.setNodeName("归档确认");
        archiveNode.setRoleName("档案管理员");
        archiveNode.setEstimatedDays(1);
        archiveNode.setDescription("案件审批完成后，档案管理员完成卷宗归档");
        archiveNode.setSkipped(false);
        chain.add(archiveNode);

        return chain;
    }

    private String buildProcessDescription(CaseType caseType, ApprovalType approvalType, List<SpecialFlag> flags, int totalDays) {
        StringBuilder sb = new StringBuilder();

        if (caseType != null) {
            sb.append(caseType.getDesc());
        } else {
            sb.append("未识别类型案件");
        }
        sb.append("，");

        if (flags != null && !flags.isEmpty()) {
            sb.append("特殊标识：");
            for (int i = 0; i < flags.size(); i++) {
                if (i > 0) sb.append("、");
                sb.append(flags.get(i).getKeyword());
            }
            sb.append("，");
        }

        sb.append("审批流程类型：").append(approvalType.getName());
        sb.append("，预计总耗时：").append(totalDays).append("个工作日。");

        if (flags != null && flags.contains(SpecialFlag.URGENT)) {
            sb.append("紧急案件已跳过庭长审批环节。");
        }
        if (flags != null && flags.contains(SpecialFlag.CONFIDENTIAL)) {
            sb.append("涉密案件已增加保密审查节点。");
        }

        return sb.toString();
    }
}
