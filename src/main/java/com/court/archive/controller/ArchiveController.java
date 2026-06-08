package com.court.archive.controller;

import com.court.archive.common.Result;
import com.court.archive.dto.*;
import com.court.archive.entity.*;
import com.court.archive.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @GetMapping("/cases")
    public Result<List<CaseInfo>> listCases() {
        return Result.success(archiveService.listAllCases());
    }

    @GetMapping("/cases/{caseNo}")
    public Result<CaseDirectory> getCaseDirectory(@PathVariable String caseNo) {
        return Result.success("案号目录查询成功，已进入卷宗借阅流程", archiveService.getCaseDirectory(caseNo));
    }

    @PostMapping("/borrow")
    public Result<Map<String, Object>> borrowArchive(@Valid @RequestBody BorrowRequest request) {
        Map<String, Object> result = archiveService.borrowArchive(request);
        Boolean blocked = (Boolean) result.get("blocked");
        if (blocked != null && blocked) {
            return Result.error(403, "借阅申请被拦截：借阅人处于黑名单");
        }
        return Result.success("借阅申请已提交，流程已启动", result);
    }

    @PostMapping("/return")
    public Result<ReturnVerifyResult> verifyReturn(@Valid @RequestBody ReturnRequest request) {
        ReturnVerifyResult result = archiveService.verifyReturn(request);
        return Result.success("归还核验完成，当前责任人：" + result.getCurrentResponsible(), result);
    }

    @GetMapping("/overdue")
    public Result<List<OverdueInfo>> listOverdue() {
        List<OverdueInfo> list = archiveService.listOverdueRecords();
        return Result.success("逾期提醒查询完成，共 " + list.size() + " 条逾期记录（含最近一次动作）", list);
    }

    @GetMapping("/records")
    public Result<List<BorrowRecord>> listRecords() {
        return Result.success(archiveService.listBorrowRecords());
    }
}
