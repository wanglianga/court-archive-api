package com.court.archive.controller;

import com.court.archive.common.Result;
import com.court.archive.dto.CaseNumberParseRequest;
import com.court.archive.dto.CaseNumberParseResult;
import com.court.archive.dto.ProcessRoutingResult;
import com.court.archive.service.CaseNumberParserService;
import com.court.archive.service.ProcessRoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/case")
@CrossOrigin(origins = "*")
public class CaseNumberController {

    @Autowired
    private CaseNumberParserService caseNumberParserService;

    @Autowired
    private ProcessRoutingService processRoutingService;

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
}
