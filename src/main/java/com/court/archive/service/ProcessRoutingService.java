package com.court.archive.service;

import com.court.archive.dto.ApprovalNode;
import com.court.archive.dto.CaseNumberParseResult;
import com.court.archive.dto.ProcessRoutingResult;
import com.court.archive.entity.ApprovalStrategyTemplate;
import com.court.archive.entity.ArchiveBox;
import com.court.archive.enums.ApprovalType;
import com.court.archive.enums.CaseType;
import com.court.archive.enums.SpecialFlag;
import com.court.archive.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessRoutingService {

    @Autowired
    private CaseNumberParserService caseNumberParserService;

    @Autowired
    private DataStore dataStore;

    private static final Map<String, String> ROLE_HANDLER_MAP = new HashMap<>();

    static {
        ROLE_HANDLER_MAP.put("书记员", "书记员小王");
        ROLE_HANDLER_MAP.put("执行书记员", "执行书记员小李");
        ROLE_HANDLER_MAP.put("保密专员", "保密专员张科");
        ROLE_HANDLER_MAP.put("庭长", "王庭长");
        ROLE_HANDLER_MAP.put("执行局长", "陈执行局长");
        ROLE_HANDLER_MAP.put("分管副院长", "刘副院长");
        ROLE_HANDLER_MAP.put("审判委员会", "审委会秘书处");
        ROLE_HANDLER_MAP.put("外事办公室", "外事办李主任");
        ROLE_HANDLER_MAP.put("独任法官", "独任法官赵法官");
        ROLE_HANDLER_MAP.put("档案管理员", "档案管理员老周");
    }

    private static final Map<String, String> ROLE_NEXT_ACTION_MAP = new HashMap<>();

    static {
        ROLE_NEXT_ACTION_MAP.put("书记员", "提交庭长审批");
        ROLE_NEXT_ACTION_MAP.put("执行书记员", "提交执行局长审批");
        ROLE_NEXT_ACTION_MAP.put("保密专员", "移交书记员登记");
        ROLE_NEXT_ACTION_MAP.put("庭长", "提交分管副院长审批或直接归档");
        ROLE_NEXT_ACTION_MAP.put("执行局长", "批准借阅并通知取卷");
        ROLE_NEXT_ACTION_MAP.put("分管副院长", "审批后移交档案管理员归档");
        ROLE_NEXT_ACTION_MAP.put("审判委员会", "讨论决定后反馈结果");
        ROLE_NEXT_ACTION_MAP.put("外事办公室", "出具涉外审查意见后返回");
        ROLE_NEXT_ACTION_MAP.put("独任法官", "直接批准借阅");
        ROLE_NEXT_ACTION_MAP.put("档案管理员", "完成卷宗归档或出库");
    }

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
        result.setNormalizedCaseNumber(parseResult.getNormalizedCaseNumber());
        result.setCaseType(parseResult.getCaseType());
        result.setCaseTypeName(parseResult.getCaseTypeName());
        result.setCaseLevel(parseResult.getCaseLevel());
        result.setCourtCode(parseResult.getCourtCode());
        result.setAdministrativeDivision(parseResult.getAdministrativeDivision());
        result.setYear(parseResult.getYear());
        result.setSerialNumberStart(parseResult.getSerialNumberStart());
        result.setSerialNumberEnd(parseResult.getSerialNumberEnd());

        CaseType caseType = parseResult.getCaseType();
        String caseLevel = parseResult.getCaseLevel();
        List<SpecialFlag> flags = parseResult.getSpecialFlags();

        List<String> flagNames = new ArrayList<>();
        for (SpecialFlag flag : flags) {
            flagNames.add(flag.getKeyword() + "(" + flag.getDesc() + ")");
        }
        result.setSpecialFlags(flagNames);

        ApprovalStrategyTemplate template = dataStore.findStrategyTemplate(caseType, caseLevel);
        if (template != null) {
            result.setStrategyTemplateId(template.getTemplateId());
            result.setStrategyTemplateName(template.getTemplateName());
            result.setStrategyTemplate(template);
            result.setPhysicalLocationRule(template.getPhysicalLocationRule());
            result.setStartNodeRole(template.getStartNodeRole());
            result.setRequiredApprovalRoles(template.getRequiredApprovalRoles());
            result.setCountersignNodeCount(template.getCountersignNodeCount());
            result.setJumpConditions(template.getJumpConditions());
        }

        List<ArchiveBox> matchedBoxes = findMatchedBoxes(caseNumber, parseResult);
        result.setMatchedBoxes(matchedBoxes);
        result.setMatchedBoxCount(matchedBoxes.size());

        if (!matchedBoxes.isEmpty()) {
            ArchiveBox firstBox = matchedBoxes.get(0);
            result.setPhysicalLocation(firstBox.getPhysicalLocation());
            if (firstBox.getStrategyTemplateId() != null && template == null) {
                ApprovalStrategyTemplate boxTemplate = dataStore.strategyTemplateMap.get(firstBox.getStrategyTemplateId());
                if (boxTemplate != null) {
                    result.setStrategyTemplateId(boxTemplate.getTemplateId());
                    result.setStrategyTemplateName(boxTemplate.getTemplateName());
                    result.setStrategyTemplate(boxTemplate);
                    result.setPhysicalLocationRule(boxTemplate.getPhysicalLocationRule());
                    result.setStartNodeRole(boxTemplate.getStartNodeRole());
                    result.setRequiredApprovalRoles(boxTemplate.getRequiredApprovalRoles());
                    result.setCountersignNodeCount(boxTemplate.getCountersignNodeCount());
                    result.setJumpConditions(boxTemplate.getJumpConditions());
                    template = boxTemplate;
                }
            }
        }

        ApprovalType approvalType = determineApprovalType(caseType, flags, template);
        result.setApprovalType(approvalType);
        result.setApprovalTypeName(approvalType.getName());

        List<ApprovalNode> approvalChain = buildApprovalChain(approvalType, caseType, flags, template);
        result.setApprovalChain(approvalChain);

        ApprovalNode firstActiveNode = null;
        for (ApprovalNode node : approvalChain) {
            if (!node.isSkipped()) {
                firstActiveNode = node;
                break;
            }
        }
        if (firstActiveNode != null) {
            result.setCurrentExpectedHandlerRole(firstActiveNode.getRoleName());
            result.setCurrentExpectedHandler(firstActiveNode.getExpectedHandler());
            result.setNextAction(firstActiveNode.getNextAction());
        }

        int totalDays = 0;
        for (ApprovalNode node : approvalChain) {
            if (!node.isSkipped() && node.getEstimatedDays() != null) {
                totalDays += node.getEstimatedDays();
            }
        }
        result.setTotalEstimatedDays(totalDays);

        result.setProcessDescription(buildProcessDescription(parseResult, approvalType, flags, totalDays, template, matchedBoxes));

        return result;
    }

    private List<ArchiveBox> findMatchedBoxes(String caseNumber, CaseNumberParseResult parseResult) {
        List<ArchiveBox> allBoxes = new ArrayList<>(dataStore.boxMap.values());
        return allBoxes.stream()
                .filter(box -> {
                    if (box.getCaseNo() != null && box.getCaseNo().equals(caseNumber)) {
                        return true;
                    }
                    if (parseResult.getYear() != null && parseResult.getSerialNumberStart() != null && box.getCaseNo() != null) {
                        CaseNumberParseResult boxParse = caseNumberParserService.parse(box.getCaseNo());
                        if (boxParse.isValid()) {
                            if (boxParse.getCaseType() == parseResult.getCaseType()
                                    && parseResult.getYear().equals(boxParse.getYear())) {
                                if (parseResult.getSerialNumberEnd() != null
                                        && boxParse.getSerialNumberStart() != null) {
                                    return boxParse.getSerialNumberStart() >= parseResult.getSerialNumberStart()
                                            && boxParse.getSerialNumberStart() <= parseResult.getSerialNumberEnd();
                                }
                            }
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    private ApprovalType determineApprovalType(CaseType caseType, List<SpecialFlag> flags, ApprovalStrategyTemplate template) {
        if (flags != null && flags.contains(SpecialFlag.MAJOR)) {
            return ApprovalType.FULL_COURT_APPROVAL;
        }

        if (template != null && template.getRequiredApprovalRoles() != null) {
            List<String> roles = template.getRequiredApprovalRoles();
            if (roles.contains("审判委员会")) return ApprovalType.FULL_COURT_APPROVAL;
            if (roles.contains("分管副院长")) return ApprovalType.PRESIDENT_APPROVAL;
            if (roles.contains("庭长") || roles.contains("执行局长")) return ApprovalType.JUDGE_APPROVAL;
            return ApprovalType.DIRECT_APPROVAL;
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

    private List<ApprovalNode> buildApprovalChain(ApprovalType approvalType, CaseType caseType,
                                                   List<SpecialFlag> flags, ApprovalStrategyTemplate template) {
        List<ApprovalNode> chain = new ArrayList<>();
        int order = 1;

        boolean hasConfidential = flags != null && flags.contains(SpecialFlag.CONFIDENTIAL);
        boolean hasUrgent = flags != null && flags.contains(SpecialFlag.URGENT);
        boolean hasSummary = flags != null && flags.contains(SpecialFlag.SUMMARY);
        boolean hasMajor = flags != null && flags.contains(SpecialFlag.MAJOR);
        int countersignCount = template != null && template.getCountersignNodeCount() != null ? template.getCountersignNodeCount() : 0;

        String startRole = template != null && template.getStartNodeRole() != null ? template.getStartNodeRole() : "书记员";

        if (hasConfidential && (!"保密专员".equals(startRole))) {
            ApprovalNode securityNode = createNode(order++, "保密审查", "保密专员", 1,
                    "涉密案件需先进行保密审查，确认信息安全等级", false, null, countersignCount);
            chain.add(securityNode);
        }

        if ("保密专员".equals(startRole) && !hasConfidential) {
            startRole = "书记员";
        }

        if ("书记员".equals(startRole) || "执行书记员".equals(startRole)) {
            ApprovalNode clerkNode = createNode(order++,
                    "书记员".equals(startRole) ? "立案登记" : "执行立案登记",
                    startRole, 1,
                    "书记员对案件材料进行形式审查和登记立案",
                    false, null, countersignCount);
            chain.add(clerkNode);
        }

        if (hasSummary) {
            ApprovalNode summaryJudgeNode = createNode(order++, "独任法官审批", "独任法官", 1,
                    "简易程序案件，由独任法官直接审批", false, null, countersignCount);
            chain.add(summaryJudgeNode);
        } else if (approvalType == ApprovalType.DIRECT_APPROVAL && template == null) {
            ApprovalNode directNode = createNode(order++, "直接审批", "书记员", 1,
                    "简单案件由书记员直接审批完成", false, null, countersignCount);
            chain.add(directNode);
        } else {
            if (template != null && template.getRequiredApprovalRoles() != null) {
                for (String role : template.getRequiredApprovalRoles()) {
                    if ("书记员".equals(role) || "执行书记员".equals(role) || "保密专员".equals(role)) {
                        continue;
                    }
                    if (hasUrgent && ("庭长".equals(role) || "执行局长".equals(role))) {
                        ApprovalNode skipNode = createNode(order++, role + "审批", role,
                                role.contains("执行") ? 2 : 3, role + "审批环节", true,
                                "紧急案件，已跳过" + role + "审批", countersignCount);
                        chain.add(skipNode);
                        continue;
                    }
                    String nodeName;
                    int days;
                    String desc;
                    switch (role) {
                        case "庭长":
                            nodeName = "庭长审批";
                            days = 3;
                            desc = "庭长对案件进行审核，确认案件事实和法律适用";
                            break;
                        case "执行局长":
                            nodeName = "执行局长审批";
                            days = 2;
                            desc = "执行局长审核执行案件借阅必要性";
                            break;
                        case "分管副院长":
                            nodeName = "分管副院长审批";
                            days = 3;
                            desc = "分管副院长对重大敏感案件进行审批";
                            break;
                        case "审判委员会":
                            nodeName = "审判委员会讨论";
                            days = 5;
                            desc = "重大、疑难、复杂案件提交审判委员会讨论决定";
                            break;
                        case "外事办公室":
                            nodeName = "外事办公室审查";
                            days = 3;
                            desc = "涉外案件需经外事办公室进行合规性审查";
                            break;
                        default:
                            nodeName = role + "审批";
                            days = 2;
                            desc = role + "进行审批";
                    }
                    ApprovalNode node = createNode(order++, nodeName, role, days, desc, false, null, countersignCount);
                    chain.add(node);
                }
            } else {
                if (hasUrgent) {
                    ApprovalNode judgeNode = createNode(order++, "庭长审批", "庭长", 3,
                            "庭长审批环节", true, "紧急案件，已跳过庭长审批", countersignCount);
                    chain.add(judgeNode);
                } else if (approvalType == ApprovalType.JUDGE_APPROVAL
                        || approvalType == ApprovalType.PRESIDENT_APPROVAL
                        || approvalType == ApprovalType.FULL_COURT_APPROVAL) {
                    ApprovalNode judgeNode = createNode(order++, "庭长审批", "庭长", 3,
                            "庭长对案件进行审核，确认案件事实和法律适用",
                            false, null, countersignCount);
                    chain.add(judgeNode);
                }

                if (approvalType == ApprovalType.PRESIDENT_APPROVAL || approvalType == ApprovalType.FULL_COURT_APPROVAL) {
                    ApprovalNode presidentNode = createNode(order++, "分管副院长审批", "分管副院长", 3,
                            "分管副院长对重大敏感案件进行审批", false, null, countersignCount);
                    chain.add(presidentNode);
                }

                if (hasMajor || approvalType == ApprovalType.FULL_COURT_APPROVAL) {
                    ApprovalNode fullCourtNode = createNode(order++, "审判委员会讨论", "审判委员会", 5,
                            "重大、疑难、复杂案件提交审判委员会讨论决定",
                            false, null, countersignCount);
                    chain.add(fullCourtNode);
                }
            }
        }

        ApprovalNode archiveNode = createNode(order++, "归档确认", "档案管理员", 1,
                "案件审批完成后，档案管理员完成卷宗归档/出库", false, null, countersignCount);
        chain.add(archiveNode);

        return chain;
    }

    private ApprovalNode createNode(int order, String nodeName, String roleName, int estimatedDays,
                                     String description, boolean skipped, String skipReason, int countersignCount) {
        ApprovalNode node = new ApprovalNode();
        node.setOrder(order);
        node.setNodeName(nodeName);
        node.setRoleName(roleName);
        node.setEstimatedDays(estimatedDays);
        node.setDescription(description);
        node.setSkipped(skipped);
        node.setSkipReason(skipReason);
        node.setExpectedHandler(ROLE_HANDLER_MAP.getOrDefault(roleName, "待指派-" + roleName));
        node.setNextAction(ROLE_NEXT_ACTION_MAP.getOrDefault(roleName, "按流程推进"));
        if (countersignCount > 0 && ("庭长".equals(roleName) || "分管副院长".equals(roleName))) {
            node.setCountersign(true);
            node.setCountersignCount(countersignCount);
        }
        return node;
    }

    private String buildProcessDescription(CaseNumberParseResult parseResult, ApprovalType approvalType,
                                            List<SpecialFlag> flags, int totalDays,
                                            ApprovalStrategyTemplate template, List<ArchiveBox> matchedBoxes) {
        StringBuilder sb = new StringBuilder();

        if (parseResult.getAdministrativeDivision() != null) {
            sb.append("【").append(parseResult.getAdministrativeDivision()).append("】");
        }
        if (parseResult.getCaseTypeName() != null) {
            sb.append(parseResult.getCaseTypeName());
        } else {
            sb.append("未识别类型案件");
        }
        if (parseResult.getCaseLevel() != null) {
            sb.append("（").append(parseResult.getCaseLevel()).append("）");
        }
        if (parseResult.getYear() != null) {
            sb.append("-").append(parseResult.getYear()).append("年度");
        }
        if (parseResult.getSerialNumberStart() != null) {
            sb.append(" 第").append(parseResult.getSerialNumberStart());
            if (parseResult.getSerialNumberEnd() != null && !parseResult.getSerialNumberEnd().equals(parseResult.getSerialNumberStart())) {
                sb.append("-").append(parseResult.getSerialNumberEnd());
            }
            sb.append("号");
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

        if (template != null) {
            sb.append("匹配策略模板：").append(template.getTemplateName()).append("，");
        }
        if (!matchedBoxes.isEmpty()) {
            sb.append("匹配卷宗盒数量：").append(matchedBoxes.size()).append("个，");
        }

        sb.append("审批流程类型：").append(approvalType.getName());
        sb.append("，预计总耗时：").append(totalDays).append("个工作日。");

        if (template != null && template.getCountersignNodeCount() != null && template.getCountersignNodeCount() > 0) {
            sb.append("本流程含").append(template.getCountersignNodeCount()).append("个会签节点。");
        }

        if (flags != null && flags.contains(SpecialFlag.URGENT)) {
            sb.append("紧急案件已跳过庭长审批环节。");
        }
        if (flags != null && flags.contains(SpecialFlag.CONFIDENTIAL)) {
            sb.append("涉密案件已增加保密审查节点。");
        }

        return sb.toString();
    }
}
