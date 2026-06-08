package com.court.archive.controller;

import com.court.archive.common.Result;
import com.court.archive.dto.CaseNumberParseRequest;
import com.court.archive.dto.CaseNumberParseResult;
import com.court.archive.dto.ProcessRoutingResult;
import com.court.archive.entity.ApprovalStrategyTemplate;
import com.court.archive.service.CaseNumberParserService;
import com.court.archive.service.ProcessRoutingService;
import com.court.archive.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/case")
@CrossOrigin(origins = "*")
public class CaseNumberController {

    @Autowired
    private CaseNumberParserService caseNumberParserService;

    @Autowired
    private ProcessRoutingService processRoutingService;

    @Autowired
    private DataStore dataStore;

    @PostMapping("/parse")
    public Result<CaseNumberParseResult> parseCaseNumber(@Valid @RequestBody CaseNumberParseRequest request) {
        CaseNumberParseResult result = caseNumberParserService.parse(request.getCaseNumber());
        if (!result.isValid()) {
            return Result.error(400, result.getErrorMessage());
        }
        return Result.success("案号解析成功", result);
    }

    @GetMapping("/parse/{caseNumber}")
    public Result<CaseNumberParseResult> parseCaseNumberGet(@PathVariable String caseNumber) {
        CaseNumberParseResult result = caseNumberParserService.parse(caseNumber);
        if (!result.isValid()) {
            return Result.error(400, result.getErrorMessage());
        }
        return Result.success("案号解析成功", result);
    }

    @PostMapping("/route")
    public Result<ProcessRoutingResult> routeProcess(@Valid @RequestBody CaseNumberParseRequest request) {
        ProcessRoutingResult result = processRoutingService.route(request.getCaseNumber());
        if (!result.isValid()) {
            return Result.error(400, result.getErrorMessage());
        }
        return Result.success("流程路由成功，已生成审批链路", result);
    }

    @GetMapping("/route/{caseNumber}")
    public Result<ProcessRoutingResult> routeProcessGet(@PathVariable String caseNumber) {
        ProcessRoutingResult result = processRoutingService.route(caseNumber);
        if (!result.isValid()) {
            return Result.error(400, result.getErrorMessage());
        }
        return Result.success("流程路由成功，已生成审批链路", result);
    }

    @PostMapping("/directory-route")
    public Result<ProcessRoutingResult> directoryRoute(@Valid @RequestBody CaseNumberParseRequest request) {
        ProcessRoutingResult result = processRoutingService.route(request.getCaseNumber());
        if (!result.isValid()) {
            return Result.error(400, result.getErrorMessage());
        }
        String msg = String.format(
                "案号目录联动路由成功：案件类型[%s]+审级[%s] → 匹配策略[%s] → 当前处理人[%s(%s)] → 下一步[%s]",
                result.getCaseTypeName() != null ? result.getCaseTypeName() : "未识别",
                result.getCaseLevel() != null ? result.getCaseLevel() : "一审",
                result.getStrategyTemplateName() != null ? result.getStrategyTemplateName() : "默认策略",
                result.getCurrentExpectedHandlerRole() != null ? result.getCurrentExpectedHandlerRole() : "待指派",
                result.getCurrentExpectedHandler() != null ? result.getCurrentExpectedHandler() : "",
                result.getNextAction() != null ? result.getNextAction() : "按流程推进"
        );
        return Result.success(msg, result);
    }

    @GetMapping("/directory-route/{caseNumber}")
    public Result<ProcessRoutingResult> directoryRouteGet(@PathVariable String caseNumber) {
        ProcessRoutingResult result = processRoutingService.route(caseNumber);
        if (!result.isValid()) {
            return Result.error(400, result.getErrorMessage());
        }
        String msg = String.format(
                "案号目录联动路由成功：案件类型[%s]+审级[%s] → 匹配策略[%s] → 当前处理人[%s(%s)] → 下一步[%s]",
                result.getCaseTypeName() != null ? result.getCaseTypeName() : "未识别",
                result.getCaseLevel() != null ? result.getCaseLevel() : "一审",
                result.getStrategyTemplateName() != null ? result.getStrategyTemplateName() : "默认策略",
                result.getCurrentExpectedHandlerRole() != null ? result.getCurrentExpectedHandlerRole() : "待指派",
                result.getCurrentExpectedHandler() != null ? result.getCurrentExpectedHandler() : "",
                result.getNextAction() != null ? result.getNextAction() : "按流程推进"
        );
        return Result.success(msg, result);
    }

    @GetMapping("/templates")
    public Result<List<ApprovalStrategyTemplate>> listTemplates() {
        return Result.success("已加载审批策略模板列表", new ArrayList<>(dataStore.strategyTemplateMap.values()));
    }

    @GetMapping("/demo-cases")
    public Result<List<String>> listDemoCases() {
        List<String> demoCases = new ArrayList<>();
        demoCases.add("(2024)京0101民初字第001号 —— 民事一审（庭长审批）");
        demoCases.add("(2024)京02民终字第256号 —— 民事二审（分管副院长审批）");
        demoCases.add("(2024)京0101刑初字第088号 —— 刑事一审（分管副院长+会签）");
        demoCases.add("(2024)京0101刑终字第099号 —— 刑事二审（审委会审批）");
        demoCases.add("(2024)京0101行初字第015号 —— 行政一审（分管副院长审批）");
        demoCases.add("(2024)京0101执字第500号 —— 执行案件（执行局长审批）");
        demoCases.add("(2024)京0101民初字第123-130号 —— 民事系列案（流水号段）");
        demoCases.add("(2024)京0101知民初字第066号 —— 知识产权（保密专员+分管副院长）");
        demoCases.add("(2024)京0101外民初字第008号 —— 涉外案件（外事办会签）");
        demoCases.add("紧急(2024)京0101民初字第001号 —— 紧急民事（跳过庭长审批）");
        demoCases.add("涉密(2024)京0101民初字第001号 —— 涉密民事（增加保密审查）");
        demoCases.add("重大(2024)京0101刑初字第088号 —— 重大刑事（升级审委会）");
        return Result.success("演示案号列表", demoCases);
    }
}
