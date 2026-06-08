package com.court.archive.service;

import com.court.archive.dto.*;
import com.court.archive.entity.*;
import com.court.archive.enums.ActionType;
import com.court.archive.enums.BoxStatus;
import com.court.archive.enums.BorrowStatus;
import com.court.archive.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArchiveService {

    @Autowired
    private DataStore dataStore;

    public List<CaseInfo> listAllCases() {
        return new ArrayList<>(dataStore.caseMap.values());
    }

    public CaseDirectory getCaseDirectory(String caseNo) {
        CaseInfo caseInfo = dataStore.caseMap.get(caseNo);
        if (caseInfo == null) {
            throw new RuntimeException("案号不存在: " + caseNo);
        }

        List<ArchiveBox> boxList = dataStore.boxMap.values().stream()
                .filter(b -> caseNo.equals(b.getCaseNo()))
                .collect(Collectors.toList());

        CaseDirectory directory = new CaseDirectory();
        directory.setCaseInfo(caseInfo);
        directory.setBoxList(boxList);

        long inStockCount = boxList.stream().filter(b -> b.getStatus() == BoxStatus.IN_STOCK).count();
        long borrowedCount = boxList.stream().filter(b -> b.getStatus() == BoxStatus.BORROWED).count();
        long overdueCount = boxList.stream().filter(b -> b.getStatus() == BoxStatus.OVERDUE).count();

        String hint;
        if (overdueCount > 0) {
            hint = String.format("当前案号下有%d个卷宗盒逾期未还，请先处理逾期，流程走向：逾期处理->归还核验->归档", overdueCount);
        } else if (borrowedCount > 0) {
            hint = String.format("当前案号下有%d个卷宗盒已借出，%d个在库。在库卷宗盒可走正常借阅流程：借阅申请->审批->取卷", borrowedCount, inStockCount);
        } else {
            hint = String.format("当前案号下所有%d个卷宗盒均在库，可直接走借阅流程：借阅申请->审批->取卷", inStockCount);
        }
        directory.setProcessHint(hint);

        dataStore.addActionLog(caseNo, null, null,
                ActionType.CREATE_CASE, "查看案号目录，进入卷宗借阅流程", "系统用户", LocalDateTime.now());

        return directory;
    }

    public Map<String, Object> borrowArchive(BorrowRequest request) {
        Borrower borrower = dataStore.borrowerMap.get(request.getBorrowerId());
        if (borrower == null) {
            throw new RuntimeException("借阅人不存在: " + request.getBorrowerId());
        }

        if (borrower.isBlacklisted()) {
            Map<String, Object> result = new HashMap<>();
            result.put("blocked", true);
            result.put("blockReason", "借阅人处于黑名单，禁止借阅");
            result.put("borrowerName", borrower.getBorrowerName());
            result.put("blacklistReason", borrower.getBlacklistReason());
            result.put("suggestion", "请联系档案管理员解除黑名单后再申请借阅");

            dataStore.addActionLog(request.getCaseNo(), request.getBoxNo(), null,
                    ActionType.EXCEPTION_HANDLE,
                    String.format("借阅人[%s]因黑名单被拦截: %s", borrower.getBorrowerName(), borrower.getBlacklistReason()),
                    "系统", LocalDateTime.now());
            return result;
        }

        ArchiveBox box = dataStore.boxMap.get(request.getBoxNo());
        if (box == null) {
            throw new RuntimeException("卷宗盒不存在: " + request.getBoxNo());
        }
        if (!request.getCaseNo().equals(box.getCaseNo())) {
            throw new RuntimeException("卷宗盒不属于该案号");
        }

        String processRoute;
        switch (box.getStatus()) {
            case IN_STOCK:
                processRoute = "卷宗盒在库，流程走向：借阅申请->庭长审批(审批期限3天)->取卷";
                break;
            case BORROWED:
                throw new RuntimeException("卷宗盒已借出，流程走向：等待归还->归还核验->重新借阅申请");
            case OVERDUE:
                throw new RuntimeException("卷宗盒逾期未还，流程走向：逾期催收->强制归还->归还核验->归档->重新申请");
            case APPROVING:
                throw new RuntimeException("卷宗盒审批中，流程走向：等待审批结果");
            case LOST:
                throw new RuntimeException("卷宗盒已遗失，流程走向：异常登记->赔偿处理->结案");
            default:
                processRoute = "待处理";
        }

        int borrowDays = request.getBorrowDays() != null ? request.getBorrowDays() : 7;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueTime = now.plusDays(borrowDays);

        BorrowRecord record = new BorrowRecord();
        String recordId = "REC-" + System.currentTimeMillis();
        record.setRecordId(recordId);
        record.setCaseNo(request.getCaseNo());
        record.setBoxNo(request.getBoxNo());
        record.setBorrowerId(borrower.getBorrowerId());
        record.setBorrowerName(borrower.getBorrowerName());
        record.setStatus(BorrowStatus.PENDING);
        record.setBorrowTime(now);
        record.setDueTime(dueTime);
        record.setRemark(request.getRemark());
        dataStore.recordMap.put(recordId, record);

        box.setStatus(BoxStatus.APPROVING);
        box.setCurrentResponsible("审批人-待指派");

        dataStore.addActionLog(request.getCaseNo(), request.getBoxNo(), recordId,
                ActionType.BORROW_REQUEST,
                String.format("借阅人[%s]申请借阅卷宗盒，借阅期限%d天，到期日%s", borrower.getBorrowerName(), borrowDays, dueTime),
                borrower.getBorrowerName(), now);

        Map<String, Object> result = new HashMap<>();
        result.put("blocked", false);
        result.put("recordId", recordId);
        result.put("caseNo", request.getCaseNo());
        result.put("boxNo", request.getBoxNo());
        result.put("boxStatus", box.getStatus().getDesc());
        result.put("processRoute", processRoute);
        result.put("approvalDeadline", now.plusDays(3));
        result.put("borrowDueTime", dueTime);
        result.put("borrowerName", borrower.getBorrowerName());
        result.put("status", BorrowStatus.PENDING.getDesc());
        return result;
    }

    public ReturnVerifyResult verifyReturn(ReturnRequest request) {
        BorrowRecord record = dataStore.recordMap.get(request.getRecordId());
        if (record == null) {
            throw new RuntimeException("借阅记录不存在: " + request.getRecordId());
        }

        CaseInfo caseInfo = dataStore.caseMap.get(record.getCaseNo());
        ArchiveBox box = dataStore.boxMap.get(record.getBoxNo());

        ReturnVerifyResult result = new ReturnVerifyResult();
        result.setRecordId(record.getRecordId());
        result.setCaseNo(record.getCaseNo());
        result.setCaseName(caseInfo != null ? caseInfo.getCaseName() : "");
        result.setBoxNo(record.getBoxNo());
        result.setBorrowerName(record.getBorrowerName());
        result.setCurrentResponsible(box != null ? box.getCurrentResponsible() : "未知");

        boolean isOverdue = record.getDueTime() != null && LocalDateTime.now().isAfter(record.getDueTime());
        if (record.getStatus() == BorrowStatus.RETURNED) {
            result.setVerifyStatus("ALREADY_RETURNED");
            result.setVerifyMessage("该卷宗已完成归还，当前责任人已变更为档案室");
        } else if (isOverdue) {
            result.setVerifyStatus("OVERDUE_RETURN");
            result.setVerifyMessage(String.format("逾期归还，当前责任人：%s，请走逾期处理流程后再完成归还核验", result.getCurrentResponsible()));
        } else {
            record.setStatus(BorrowStatus.RETURNED);
            record.setReturnTime(LocalDateTime.now());
            if (box != null) {
                box.setStatus(BoxStatus.IN_STOCK);
                box.setCurrentResponsible(caseInfo != null ? caseInfo.getResponsiblePerson() : "档案室");
            }
            result.setVerifyStatus("SUCCESS");
            result.setVerifyMessage(String.format("归还核验通过，卷宗已归档，当前责任人已变更为：%s", box != null ? box.getCurrentResponsible() : "档案室"));

            dataStore.addActionLog(record.getCaseNo(), record.getBoxNo(), record.getRecordId(),
                    ActionType.CONFIRM_RETURN,
                    String.format("归还核验通过，备注：%s", request.getReturnRemark() != null ? request.getReturnRemark() : "无"),
                    "档案室管理员", LocalDateTime.now());
        }

        return result;
    }

    public List<OverdueInfo> listOverdueRecords() {
        LocalDateTime now = LocalDateTime.now();
        List<OverdueInfo> result = new ArrayList<>();

        for (BorrowRecord record : dataStore.recordMap.values()) {
            boolean isOverdue = (record.getStatus() == BorrowStatus.BORROWING || record.getStatus() == BorrowStatus.OVERDUE)
                    && record.getDueTime() != null && now.isAfter(record.getDueTime());

            if (isOverdue) {
                if (record.getStatus() == BorrowStatus.BORROWING) {
                    record.setStatus(BorrowStatus.OVERDUE);
                }
                ArchiveBox box = dataStore.boxMap.get(record.getBoxNo());
                if (box != null && box.getStatus() != BoxStatus.OVERDUE) {
                    box.setStatus(BoxStatus.OVERDUE);
                }

                OverdueInfo info = new OverdueInfo();
                info.setRecordId(record.getRecordId());
                info.setCaseNo(record.getCaseNo());
                CaseInfo caseInfo = dataStore.caseMap.get(record.getCaseNo());
                info.setCaseName(caseInfo != null ? caseInfo.getCaseName() : "");
                info.setBoxNo(record.getBoxNo());
                info.setBorrowerId(record.getBorrowerId());
                info.setBorrowerName(record.getBorrowerName());
                info.setBorrowTime(record.getBorrowTime());
                info.setDueTime(record.getDueTime());
                info.setOverdueDays(Duration.between(record.getDueTime(), now).toDays());

                ActionLog lastLog = dataStore.getLastAction(record.getRecordId());
                if (lastLog != null) {
                    info.setLastActionType(lastLog.getActionType().getDesc());
                    info.setLastActionDesc(lastLog.getActionDesc());
                    info.setLastOperator(lastLog.getOperator());
                    info.setLastActionTime(lastLog.getActionTime());
                } else {
                    info.setLastActionType("无记录");
                    info.setLastActionDesc("无操作记录");
                    info.setLastOperator("系统");
                    info.setLastActionTime(record.getBorrowTime());
                }

                result.add(info);
            }
        }

        result.sort((a, b) -> Long.compare(b.getOverdueDays(), a.getOverdueDays()));
        return result;
    }

    public List<BorrowRecord> listBorrowRecords() {
        return new ArrayList<>(dataStore.recordMap.values());
    }
}
