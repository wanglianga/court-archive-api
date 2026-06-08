package com.court.archive.store;

import com.court.archive.entity.*;
import com.court.archive.enums.ActionType;
import com.court.archive.enums.BoxStatus;
import com.court.archive.enums.BorrowStatus;
import com.court.archive.enums.CaseType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataStore {

    public final ConcurrentHashMap<String, CaseInfo> caseMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, ArchiveBox> boxMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Borrower> borrowerMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, BorrowRecord> recordMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, List<ActionLog>> actionLogMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, ApprovalStrategyTemplate> strategyTemplateMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initDemoData() {
        initStrategyTemplates();
        initCaseAndBoxData();
        initBorrowerData();
        initBorrowRecordData();
    }

    private void initStrategyTemplates() {
        ApprovalStrategyTemplate t1 = new ApprovalStrategyTemplate();
        t1.setTemplateId("STRAT-CIVIL-FIRST");
        t1.setTemplateName("民事一审卷宗借阅审批策略");
        t1.setCaseType(CaseType.CIVIL);
        t1.setCaseLevel("一审");
        t1.setPhysicalLocationRule("民事档案A区，按年份排架");
        t1.setStartNodeRole("书记员");
        t1.setRequiredApprovalRoles(Arrays.asList("书记员", "庭长"));
        t1.setCountersignNodeCount(0);
        t1.setJumpConditions(Arrays.asList("紧急案件可跳过庭长审批", "简易程序可由独任法官直接审批"));
        t1.setDescription("适用于所有一审民事案件卷宗借阅，常规需庭长审批");
        strategyTemplateMap.put(t1.getTemplateId(), t1);

        ApprovalStrategyTemplate t2 = new ApprovalStrategyTemplate();
        t2.setTemplateId("STRAT-CIVIL-SECOND");
        t2.setTemplateName("民事二审卷宗借阅审批策略");
        t2.setCaseType(CaseType.CIVIL);
        t2.setCaseLevel("二审");
        t2.setPhysicalLocationRule("民事档案A区二审专区，按案号排架");
        t2.setStartNodeRole("书记员");
        t2.setRequiredApprovalRoles(Arrays.asList("书记员", "庭长", "分管副院长"));
        t2.setCountersignNodeCount(1);
        t2.setJumpConditions(Arrays.asList("紧急案件可跳过庭长审批直接报分管副院长"));
        t2.setDescription("适用于二审民事案件卷宗借阅，需分管副院长审批");
        strategyTemplateMap.put(t2.getTemplateId(), t2);

        ApprovalStrategyTemplate t3 = new ApprovalStrategyTemplate();
        t3.setTemplateId("STRAT-CRIMINAL-FIRST");
        t3.setTemplateName("刑事一审卷宗借阅审批策略");
        t3.setCaseType(CaseType.CRIMINAL);
        t3.setCaseLevel("一审");
        t3.setPhysicalLocationRule("刑事档案B区，按年份和罪名排架");
        t3.setStartNodeRole("书记员");
        t3.setRequiredApprovalRoles(Arrays.asList("书记员", "庭长", "分管副院长"));
        t3.setCountersignNodeCount(2);
        t3.setJumpConditions(Arrays.asList("涉密案件需先经保密专员审查"));
        t3.setDescription("适用于一审刑事案件卷宗借阅，需分管副院长审批且需刑事庭和法警队会签");
        strategyTemplateMap.put(t3.getTemplateId(), t3);

        ApprovalStrategyTemplate t4 = new ApprovalStrategyTemplate();
        t4.setTemplateId("STRAT-CRIMINAL-SECOND");
        t4.setTemplateName("刑事二审卷宗借阅审批策略");
        t4.setCaseType(CaseType.CRIMINAL);
        t4.setCaseLevel("二审");
        t4.setPhysicalLocationRule("刑事档案B区二审专区，按案号排架");
        t4.setStartNodeRole("书记员");
        t4.setRequiredApprovalRoles(Arrays.asList("书记员", "庭长", "分管副院长", "审判委员会"));
        t4.setCountersignNodeCount(3);
        t4.setJumpConditions(Arrays.asList("重大案件需审委会集体讨论决定"));
        t4.setDescription("适用于二审刑事案件卷宗借阅，重大案件需审委会审批");
        strategyTemplateMap.put(t4.getTemplateId(), t4);

        ApprovalStrategyTemplate t5 = new ApprovalStrategyTemplate();
        t5.setTemplateId("STRAT-ADMIN-FIRST");
        t5.setTemplateName("行政一审卷宗借阅审批策略");
        t5.setCaseType(CaseType.ADMINISTRATIVE);
        t5.setCaseLevel("一审");
        t5.setPhysicalLocationRule("行政档案C区，按行政机关分类排架");
        t5.setStartNodeRole("书记员");
        t5.setRequiredApprovalRoles(Arrays.asList("书记员", "庭长", "分管副院长"));
        t5.setCountersignNodeCount(1);
        t5.setJumpConditions(Arrays.asList("涉及政府信息公开案件需额外审查"));
        t5.setDescription("适用于一审行政案件卷宗借阅，需分管副院长审批");
        strategyTemplateMap.put(t5.getTemplateId(), t5);

        ApprovalStrategyTemplate t6 = new ApprovalStrategyTemplate();
        t6.setTemplateId("STRAT-EXECUTION");
        t6.setTemplateName("执行案件卷宗借阅审批策略");
        t6.setCaseType(CaseType.EXECUTION);
        t6.setCaseLevel("执行");
        t6.setPhysicalLocationRule("执行档案D区，按执行阶段排架");
        t6.setStartNodeRole("执行书记员");
        t6.setRequiredApprovalRoles(Arrays.asList("执行书记员", "执行局长"));
        t6.setCountersignNodeCount(0);
        t6.setJumpConditions(Arrays.asList("已结执行案件可由执行书记员直接审批"));
        t6.setDescription("适用于执行案件卷宗借阅，常规需执行局长审批");
        strategyTemplateMap.put(t6.getTemplateId(), t6);

        ApprovalStrategyTemplate t7 = new ApprovalStrategyTemplate();
        t7.setTemplateId("STRAT-IP-FIRST");
        t7.setTemplateName("知识产权一审卷宗借阅审批策略");
        t7.setCaseType(CaseType.INTELLECTUAL);
        t7.setCaseLevel("一审");
        t7.setPhysicalLocationRule("知识产权档案E区，按权利类型排架，保密等级高");
        t7.setStartNodeRole("书记员");
        t7.setRequiredApprovalRoles(Arrays.asList("书记员", "保密专员", "庭长", "分管副院长"));
        t7.setCountersignNodeCount(1);
        t7.setJumpConditions(Arrays.asList("涉密知识产权案件需双重保密审查"));
        t7.setDescription("适用于一审知识产权案件卷宗借阅，因涉及商业秘密需保密专员审查");
        strategyTemplateMap.put(t7.getTemplateId(), t7);

        ApprovalStrategyTemplate t8 = new ApprovalStrategyTemplate();
        t8.setTemplateId("STRAT-FOREIGN");
        t8.setTemplateName("涉外案件卷宗借阅审批策略");
        t8.setCaseType(CaseType.FOREIGN);
        t8.setCaseLevel("一审");
        t8.setPhysicalLocationRule("涉外档案F区，独立库房保管，需双人双锁");
        t8.setStartNodeRole("书记员");
        t8.setRequiredApprovalRoles(Arrays.asList("书记员", "庭长", "分管副院长", "外事办公室"));
        t8.setCountersignNodeCount(2);
        t8.setJumpConditions(Arrays.asList("外交豁免案件需层报最高院审批"));
        t8.setDescription("适用于涉外案件卷宗借阅，需外事办公室会签，涉及外交豁免需最高院批准");
        strategyTemplateMap.put(t8.getTemplateId(), t8);
    }

    private void initCaseAndBoxData() {
        LocalDateTime now = LocalDateTime.now();

        CaseInfo c1 = new CaseInfo();
        c1.setCaseNo("(2024)京0101民初字第001号");
        c1.setCaseName("张三诉李四合同纠纷案");
        c1.setCaseType("民事案件");
        c1.setResponsiblePerson("王法官");
        c1.setCreateTime(now.minusMonths(3));
        caseMap.put(c1.getCaseNo(), c1);

        CaseInfo c2 = new CaseInfo();
        c2.setCaseNo("(2024)京0101刑初字第088号");
        c2.setCaseName("赵某盗窃案");
        c2.setCaseType("刑事案件");
        c2.setResponsiblePerson("李法官");
        c2.setCreateTime(now.minusMonths(2));
        caseMap.put(c2.getCaseNo(), c2);

        CaseInfo c3 = new CaseInfo();
        c3.setCaseNo("(2024)京0101行初字第015号");
        c3.setCaseName("某公司诉工商局行政复议案");
        c3.setCaseType("行政案件");
        c3.setResponsiblePerson("张法官");
        c3.setCreateTime(now.minusMonths(1));
        caseMap.put(c3.getCaseNo(), c3);

        CaseInfo c4 = new CaseInfo();
        c4.setCaseNo("(2024)京02民终字第256号");
        c4.setCaseName("王五诉赵六二审合同纠纷案");
        c4.setCaseType("民事案件");
        c4.setResponsiblePerson("刘法官");
        c4.setCreateTime(now.minusWeeks(3));
        caseMap.put(c4.getCaseNo(), c4);

        CaseInfo c5 = new CaseInfo();
        c5.setCaseNo("(2024)京0101执字第500号");
        c5.setCaseName("某公司执行案");
        c5.setCaseType("执行案件");
        c5.setResponsiblePerson("陈执行员");
        c5.setCreateTime(now.minusWeeks(2));
        caseMap.put(c5.getCaseNo(), c5);

        CaseInfo c6 = new CaseInfo();
        c6.setCaseNo("(2024)京0101刑终字第099号");
        c6.setCaseName("钱某重大责任事故二审案");
        c6.setCaseType("刑事案件");
        c6.setResponsiblePerson("周法官");
        c6.setCreateTime(now.minusWeeks(1));
        caseMap.put(c6.getCaseNo(), c6);

        CaseInfo c7 = new CaseInfo();
        c7.setCaseNo("(2024)京0101民初字第123-130号");
        c7.setCaseName("系列商品房预售合同纠纷案");
        c7.setCaseType("民事案件");
        c7.setResponsiblePerson("吴法官");
        c7.setCreateTime(now.minusDays(5));
        caseMap.put(c7.getCaseNo(), c7);

        CaseInfo c8 = new CaseInfo();
        c8.setCaseNo("(2024)京0101知民初字第066号");
        c8.setCaseName("某科技公司专利侵权案");
        c8.setCaseType("知识产权案件");
        c8.setResponsiblePerson("郑法官");
        c8.setCreateTime(now.minusDays(3));
        caseMap.put(c8.getCaseNo(), c8);

        CaseInfo c9 = new CaseInfo();
        c9.setCaseNo("(2024)京0101外民初字第008号");
        c9.setCaseName("某外资企业合资纠纷案");
        c9.setCaseType("涉外案件");
        c9.setResponsiblePerson("冯法官");
        c9.setCreateTime(now.minusDays(1));
        caseMap.put(c9.getCaseNo(), c9);

        ArchiveBox b1 = new ArchiveBox();
        b1.setBoxNo("BOX-CIV-001-A");
        b1.setCaseNo(c1.getCaseNo());
        b1.setBoxName("合同纠纷案-正卷第一册");
        b1.setStatus(BoxStatus.IN_STOCK);
        b1.setLocation("A区-01-03");
        b1.setPhysicalLocation("民事档案A区-第1排-第3层");
        b1.setShelfNo("A-01");
        b1.setFloorNo("F3");
        b1.setCurrentResponsible(c1.getResponsiblePerson());
        b1.setStrategyTemplateId("STRAT-CIVIL-FIRST");
        boxMap.put(b1.getBoxNo(), b1);

        ArchiveBox b2 = new ArchiveBox();
        b2.setBoxNo("BOX-CIV-001-B");
        b2.setCaseNo(c1.getCaseNo());
        b2.setBoxName("合同纠纷案-副卷");
        b2.setStatus(BoxStatus.IN_STOCK);
        b2.setLocation("A区-01-04");
        b2.setPhysicalLocation("民事档案A区-第1排-第4层");
        b2.setShelfNo("A-01");
        b2.setFloorNo("F3");
        b2.setCurrentResponsible(c1.getResponsiblePerson());
        b2.setStrategyTemplateId("STRAT-CIVIL-FIRST");
        boxMap.put(b2.getBoxNo(), b2);

        ArchiveBox b3 = new ArchiveBox();
        b3.setBoxNo("BOX-CRI-088-A");
        b3.setCaseNo(c2.getCaseNo());
        b3.setBoxName("盗窃案-正卷");
        b3.setStatus(BoxStatus.BORROWED);
        b3.setLocation("借出");
        b3.setPhysicalLocation("刑事档案B区-第2排-第1层");
        b3.setShelfNo("B-02");
        b3.setFloorNo("F3");
        b3.setCurrentResponsible("书记员小刘");
        b3.setStrategyTemplateId("STRAT-CRIMINAL-FIRST");
        boxMap.put(b3.getBoxNo(), b3);

        ArchiveBox b4 = new ArchiveBox();
        b4.setBoxNo("BOX-ADM-015-A");
        b4.setCaseNo(c3.getCaseNo());
        b4.setBoxName("行政复议案-正卷");
        b4.setStatus(BoxStatus.OVERDUE);
        b4.setLocation("逾期未还");
        b4.setPhysicalLocation("行政档案C区-第1排-第2层");
        b4.setShelfNo("C-01");
        b4.setFloorNo("F4");
        b4.setCurrentResponsible("逾期借阅人");
        b4.setStrategyTemplateId("STRAT-ADMIN-FIRST");
        boxMap.put(b4.getBoxNo(), b4);

        ArchiveBox b5 = new ArchiveBox();
        b5.setBoxNo("BOX-CIV-256-A");
        b5.setCaseNo(c4.getCaseNo());
        b5.setBoxName("二审合同纠纷案-正卷");
        b5.setStatus(BoxStatus.IN_STOCK);
        b5.setLocation("A区-03-02");
        b5.setPhysicalLocation("民事档案A区二审专区-第3排-第2层");
        b5.setShelfNo("A-03");
        b5.setFloorNo("F3");
        b5.setCurrentResponsible(c4.getResponsiblePerson());
        b5.setStrategyTemplateId("STRAT-CIVIL-SECOND");
        boxMap.put(b5.getBoxNo(), b5);

        ArchiveBox b6 = new ArchiveBox();
        b6.setBoxNo("BOX-EXE-500-A");
        b6.setCaseNo(c5.getCaseNo());
        b6.setBoxName("执行案-正卷");
        b6.setStatus(BoxStatus.IN_STOCK);
        b6.setLocation("D区-01-05");
        b6.setPhysicalLocation("执行档案D区-第1排-第5层");
        b6.setShelfNo("D-01");
        b6.setFloorNo("F2");
        b6.setCurrentResponsible(c5.getResponsiblePerson());
        b6.setStrategyTemplateId("STRAT-EXECUTION");
        boxMap.put(b6.getBoxNo(), b6);

        ArchiveBox b7 = new ArchiveBox();
        b7.setBoxNo("BOX-CRI-099-A");
        b7.setCaseNo(c6.getCaseNo());
        b7.setBoxName("重大责任事故二审案-正卷");
        b7.setStatus(BoxStatus.IN_STOCK);
        b7.setLocation("B区-03-01");
        b7.setPhysicalLocation("刑事档案B区二审专区-第3排-第1层");
        b7.setShelfNo("B-03");
        b7.setFloorNo("F3");
        b7.setCurrentResponsible(c6.getResponsiblePerson());
        b7.setStrategyTemplateId("STRAT-CRIMINAL-SECOND");
        boxMap.put(b7.getBoxNo(), b7);

        ArchiveBox b8 = new ArchiveBox();
        b8.setBoxNo("BOX-CIV-123-A");
        b8.setCaseNo(c7.getCaseNo());
        b8.setBoxName("系列商品房预售合同纠纷案-第1册");
        b8.setStatus(BoxStatus.IN_STOCK);
        b8.setLocation("A区-02-06");
        b8.setPhysicalLocation("民事档案A区-第2排-第6层");
        b8.setShelfNo("A-02");
        b8.setFloorNo("F3");
        b8.setCurrentResponsible(c7.getResponsiblePerson());
        b8.setStrategyTemplateId("STRAT-CIVIL-FIRST");
        boxMap.put(b8.getBoxNo(), b8);

        ArchiveBox b9 = new ArchiveBox();
        b9.setBoxNo("BOX-IP-066-A");
        b9.setCaseNo(c8.getCaseNo());
        b9.setBoxName("专利侵权案-正卷（涉密）");
        b9.setStatus(BoxStatus.IN_STOCK);
        b9.setLocation("E区-01-01");
        b9.setPhysicalLocation("知识产权档案E区保密库房-第1排-第1层");
        b9.setShelfNo("E-01");
        b9.setFloorNo("B1");
        b9.setCurrentResponsible(c8.getResponsiblePerson());
        b9.setStrategyTemplateId("STRAT-IP-FIRST");
        boxMap.put(b9.getBoxNo(), b9);

        ArchiveBox b10 = new ArchiveBox();
        b10.setBoxNo("BOX-FOR-008-A");
        b10.setCaseNo(c9.getCaseNo());
        b10.setBoxName("涉外合资纠纷案-正卷");
        b10.setStatus(BoxStatus.IN_STOCK);
        b10.setLocation("F区-01-01");
        b10.setPhysicalLocation("涉外档案F区独立库房-双人双锁专柜");
        b10.setShelfNo("F-01");
        b10.setFloorNo("B1");
        b10.setCurrentResponsible(c9.getResponsiblePerson());
        b10.setStrategyTemplateId("STRAT-FOREIGN");
        boxMap.put(b10.getBoxNo(), b10);
    }

    private void initBorrowerData() {
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
    }

    private void initBorrowRecordData() {
        LocalDateTime now = LocalDateTime.now();

        BorrowRecord record1 = new BorrowRecord();
        record1.setRecordId("REC-2024-0001");
        record1.setCaseNo("(2024)京0101刑初字第088号");
        record1.setBoxNo("BOX-CRI-088-A");
        record1.setBorrowerId("U002");
        record1.setBorrowerName("孙法官");
        record1.setStatus(BorrowStatus.BORROWING);
        record1.setBorrowTime(now.minusDays(5));
        record1.setDueTime(now.minusDays(1));
        record1.setApprover("王庭长");
        recordMap.put(record1.getRecordId(), record1);

        addActionLog(record1.getCaseNo(), record1.getBoxNo(), record1.getRecordId(),
                ActionType.BORROW_REQUEST, "孙法官申请借阅卷宗", "孙法官", record1.getBorrowTime());
        addActionLog(record1.getCaseNo(), record1.getBoxNo(), record1.getRecordId(),
                ActionType.APPROVE_BORROW, "王庭长批准借阅申请", "王庭长", record1.getBorrowTime().plusMinutes(30));
        addActionLog(record1.getCaseNo(), record1.getBoxNo(), record1.getRecordId(),
                ActionType.PICK_UP, "孙法官取走卷宗盒", "档案室管理员", record1.getBorrowTime().plusHours(2));

        BorrowRecord record2 = new BorrowRecord();
        record2.setRecordId("REC-2024-0002");
        record2.setCaseNo("(2024)京0101行初字第015号");
        record2.setBoxNo("BOX-ADM-015-A");
        record2.setBorrowerId("U001");
        record2.setBorrowerName("钱律师");
        record2.setStatus(BorrowStatus.OVERDUE);
        record2.setBorrowTime(now.minusDays(20));
        record2.setDueTime(now.minusDays(5));
        record2.setApprover("李庭长");
        recordMap.put(record2.getRecordId(), record2);

        addActionLog(record2.getCaseNo(), record2.getBoxNo(), record2.getRecordId(),
                ActionType.BORROW_REQUEST, "钱律师申请借阅卷宗", "钱律师", record2.getBorrowTime());
        addActionLog(record2.getCaseNo(), record2.getBoxNo(), record2.getRecordId(),
                ActionType.APPROVE_BORROW, "李庭长批准借阅申请", "李庭长", record2.getBorrowTime().plusMinutes(15));
        addActionLog(record2.getCaseNo(), record2.getBoxNo(), record2.getRecordId(),
                ActionType.PICK_UP, "钱律师取走卷宗盒", "档案室管理员", record2.getBorrowTime().plusHours(1));
        addActionLog(record2.getCaseNo(), record2.getBoxNo(), record2.getRecordId(),
                ActionType.OVERDUE_REMIND, "系统发送逾期提醒", "系统", now.minusDays(4));
        addActionLog(record2.getCaseNo(), record2.getBoxNo(), record2.getRecordId(),
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

    public ApprovalStrategyTemplate findStrategyTemplate(CaseType caseType, String caseLevel) {
        if (caseType == null) return null;
        for (ApprovalStrategyTemplate template : strategyTemplateMap.values()) {
            if (template.getCaseType() == caseType) {
                if (caseLevel == null || caseLevel.equals(template.getCaseLevel())) {
                    return template;
                }
            }
        }
        for (ApprovalStrategyTemplate template : strategyTemplateMap.values()) {
            if (template.getCaseType() == caseType) {
                return template;
            }
        }
        return null;
    }
}
