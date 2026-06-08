package com.court.archive.store;

import com.court.archive.entity.*;
import com.court.archive.enums.ActionType;
import com.court.archive.enums.BoxStatus;
import com.court.archive.enums.BorrowStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataStore {

    public final ConcurrentHashMap<String, CaseInfo> caseMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, ArchiveBox> boxMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Borrower> borrowerMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, BorrowRecord> recordMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, List<ActionLog>> actionLogMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initDemoData() {
        LocalDateTime now = LocalDateTime.now();

        CaseInfo c1 = new CaseInfo();
        c1.setCaseNo("(2024)京民初字第001号");
        c1.setCaseName("张三诉李四合同纠纷案");
        c1.setCaseType("民事案件");
        c1.setResponsiblePerson("王法官");
        c1.setCreateTime(now.minusMonths(3));
        caseMap.put(c1.getCaseNo(), c1);

        CaseInfo c2 = new CaseInfo();
        c2.setCaseNo("(2024)京刑初字第088号");
        c2.setCaseName("赵某盗窃案");
        c2.setCaseType("刑事案件");
        c2.setResponsiblePerson("李法官");
        c2.setCreateTime(now.minusMonths(2));
        caseMap.put(c2.getCaseNo(), c2);

        CaseInfo c3 = new CaseInfo();
        c3.setCaseNo("(2024)京行初字第015号");
        c3.setCaseName("某公司诉工商局行政复议案");
        c3.setCaseType("行政案件");
        c3.setResponsiblePerson("张法官");
        c3.setCreateTime(now.minusMonths(1));
        caseMap.put(c3.getCaseNo(), c3);

        ArchiveBox b1 = new ArchiveBox();
        b1.setBoxNo("BOX-001-A");
        b1.setCaseNo(c1.getCaseNo());
        b1.setBoxName("合同纠纷案-正卷第一册");
        b1.setStatus(BoxStatus.IN_STOCK);
        b1.setLocation("A区-01-03");
        b1.setCurrentResponsible(c1.getResponsiblePerson());
        boxMap.put(b1.getBoxNo(), b1);

        ArchiveBox b2 = new ArchiveBox();
        b2.setBoxNo("BOX-001-B");
        b2.setCaseNo(c1.getCaseNo());
        b2.setBoxName("合同纠纷案-副卷");
        b2.setStatus(BoxStatus.IN_STOCK);
        b2.setLocation("A区-01-04");
        b2.setCurrentResponsible(c1.getResponsiblePerson());
        boxMap.put(b2.getBoxNo(), b2);

        ArchiveBox b3 = new ArchiveBox();
        b3.setBoxNo("BOX-088-A");
        b3.setCaseNo(c2.getCaseNo());
        b3.setBoxName("盗窃案-正卷");
        b3.setStatus(BoxStatus.BORROWED);
        b3.setLocation("借出");
        b3.setCurrentResponsible("书记员小刘");
        boxMap.put(b3.getBoxNo(), b3);

        ArchiveBox b4 = new ArchiveBox();
        b4.setBoxNo("BOX-015-A");
        b4.setCaseNo(c3.getCaseNo());
        b4.setBoxName("行政复议案-正卷");
        b4.setStatus(BoxStatus.OVERDUE);
        b4.setLocation("逾期未还");
        b4.setCurrentResponsible("逾期借阅人");
        boxMap.put(b4.getBoxNo(), b4);

        Borrower br1 = new Borrower();
        br1.setBorrowerId("U001");
        br1.setBorrowerName("钱律师");
        br1.setDepartment("正大律师事务所");
        br1.setPhone("13800000001");
        br1.setBlacklisted(false);
        borrowerMap.put(br1.getBorrowerId(), br1);

        Borrower br2 = new Borrower();
        br2.setBorrowerId("U002");
        br2.setBorrowerName("孙法官");
        br2.setDepartment("民事审判庭");
        br2.setPhone("13800000002");
        br2.setBlacklisted(false);
        borrowerMap.put(br2.getBorrowerId(), br2);

        Borrower br3 = new Borrower();
        br3.setBorrowerId("U999");
        br3.setBorrowerName("黑名单用户");
        br3.setDepartment("无");
        br3.setPhone("13800000999");
        br3.setBlacklisted(true);
        br3.setBlacklistReason("多次逾期未还卷宗，情节严重");
        borrowerMap.put(br3.getBorrowerId(), br3);

        BorrowRecord record1 = new BorrowRecord();
        record1.setRecordId("REC-2024-0001");
        record1.setCaseNo(c2.getCaseNo());
        record1.setBoxNo(b3.getBoxNo());
        record1.setBorrowerId("U002");
        record1.setBorrowerName("孙法官");
        record1.setStatus(BorrowStatus.BORROWING);
        record1.setBorrowTime(now.minusDays(5));
        record1.setDueTime(now.minusDays(1));
        record1.setApprover("王庭长");
        recordMap.put(record1.getRecordId(), record1);

        addActionLog(record1.getCaseNo(), b3.getBoxNo(), record1.getRecordId(),
                ActionType.BORROW_REQUEST, "孙法官申请借阅卷宗", "孙法官", record1.getBorrowTime());
        addActionLog(record1.getCaseNo(), b3.getBoxNo(), record1.getRecordId(),
                ActionType.APPROVE_BORROW, "王庭长批准借阅申请", "王庭长", record1.getBorrowTime().plusMinutes(30));
        addActionLog(record1.getCaseNo(), b3.getBoxNo(), record1.getRecordId(),
                ActionType.PICK_UP, "孙法官取走卷宗盒", "档案室管理员", record1.getBorrowTime().plusHours(2));

        BorrowRecord record2 = new BorrowRecord();
        record2.setRecordId("REC-2024-0002");
        record2.setCaseNo(c3.getCaseNo());
        record2.setBoxNo(b4.getBoxNo());
        record2.setBorrowerId("U001");
        record2.setBorrowerName("钱律师");
        record2.setStatus(BorrowStatus.OVERDUE);
        record2.setBorrowTime(now.minusDays(20));
        record2.setDueTime(now.minusDays(5));
        record2.setApprover("李庭长");
        recordMap.put(record2.getRecordId(), record2);

        addActionLog(record2.getCaseNo(), b4.getBoxNo(), record2.getRecordId(),
                ActionType.BORROW_REQUEST, "钱律师申请借阅卷宗", "钱律师", record2.getBorrowTime());
        addActionLog(record2.getCaseNo(), b4.getBoxNo(), record2.getRecordId(),
                ActionType.APPROVE_BORROW, "李庭长批准借阅申请", "李庭长", record2.getBorrowTime().plusMinutes(15));
        addActionLog(record2.getCaseNo(), b4.getBoxNo(), record2.getRecordId(),
                ActionType.PICK_UP, "钱律师取走卷宗盒", "档案室管理员", record2.getBorrowTime().plusHours(1));
        addActionLog(record2.getCaseNo(), b4.getBoxNo(), record2.getRecordId(),
                ActionType.OVERDUE_REMIND, "系统发送逾期提醒", "系统", now.minusDays(4));
        addActionLog(record2.getCaseNo(), b4.getBoxNo(), record2.getRecordId(),
                ActionType.OVERDUE_REMIND, "电话催还钱律师", "档案室管理员", now.minusDays(2));
    }

    public void addActionLog(String caseNo, String boxNo, String recordId,
                             ActionType actionType, String actionDesc, String operator, LocalDateTime time) {
        ActionLog log = new ActionLog();
        log.setLogId("LOG-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000));
        log.setCaseNo(caseNo);
        log.setBoxNo(boxNo);
        log.setRecordId(recordId);
        log.setActionType(actionType);
        log.setActionDesc(actionDesc);
        log.setOperator(operator);
        log.setActionTime(time != null ? time : LocalDateTime.now());
        String key = recordId != null ? recordId : (caseNo + "-" + boxNo);
        actionLogMap.computeIfAbsent(key, k -> new ArrayList<>()).add(log);
    }

    public ActionLog getLastAction(String recordId) {
        List<ActionLog> logs = actionLogMap.get(recordId);
        if (logs == null || logs.isEmpty()) {
            return null;
        }
        return logs.stream()
                .max((a, b) -> a.getActionTime().compareTo(b.getActionTime()))
                .orElse(null);
    }
}
