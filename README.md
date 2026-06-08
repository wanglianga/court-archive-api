# 法院卷宗借阅归还API

基于 Spring Boot 实现的法院卷宗借阅归还管理系统 API。

## 原始需求

> 在法院卷宗借阅归还API里，案号目录是用户第一步操作，卷宗盒决定流程走向，借阅人处理例外，审批期限用于验收闭环。法院卷宗借阅归还API涉及归还核验时显示当前责任人，涉及逾期提醒时显示最近一次动作。演示时只需要证明案号目录能进流程、借阅人能拦住问题、逾期提醒能回到结果。要求使用SpringBoot实现。

## 项目简介

本系统实现法院卷宗借阅归还的完整流程管理，核心业务节点包括：

- **案号目录**：用户操作第一步入口，查询案件及其卷宗盒列表，根据卷宗盒状态给出流程走向提示
- **卷宗盒**：根据卷宗盒状态（在库/已借出/逾期/遗失等）决定具体流程走向
- **借阅人**：处理例外情况，黑名单借阅人将被系统拦截
- **审批期限**：借阅申请需在审批期限内完成审批，借阅超期触发逾期机制
- **归还核验**：核验时显示卷宗当前责任人信息
- **逾期提醒**：逾期记录中展示最近一次操作动作，便于回溯

## 技术栈

- Spring Boot 2.7.18
- Java 8
- Maven
- 内存存储（ConcurrentHashMap，启动即含演示数据，无需数据库）

## 启动方式

### 前置要求

- JDK 1.8+
- Maven 3.6+
- （可选）Docker 20.10+ 及 Docker Compose 2.0+

---

### 方式一：Docker 一键启动（推荐）

#### 1. 构建并启动

```bash
docker compose up --build
```

如需后台运行：

```bash
docker compose up --build -d
```

#### 2. 停止并清理

```bash
docker compose down
```

访问地址：http://localhost:8080

---

### 方式二：本地 Maven 启动

#### 1. 安装依赖并打包

```bash
mvn clean package -DskipTests
```

#### 2. 启动服务

```bash
mvn spring-boot:run
```

或

```bash
java -jar target/court-archive-api-1.0.0.jar
```

访问地址：http://localhost:8080

---

## API 接口说明

### 1. 案号目录（流程入口，第一步操作）

获取所有案号列表：

```bash
curl http://localhost:8080/api/cases
```

查询指定案号目录（进入卷宗借阅流程）：

```bash
curl "http://localhost:8080/api/cases/(2024)京民初字第001号"
```

返回内容包含：案件信息、卷宗盒列表、基于卷宗盒状态的流程走向提示。

### 2. 借阅申请（含借阅人拦截）

正常借阅（合法借阅人 U001）：

```bash
curl -X POST http://localhost:8080/api/borrow \
  -H "Content-Type: application/json" \
  -d '{
    "caseNo": "(2024)京民初字第001号",
    "boxNo": "BOX-001-A",
    "borrowerId": "U001",
    "borrowDays": 7
  }'
```

借阅人例外拦截（黑名单借阅人 U999）：

```bash
curl -X POST http://localhost:8080/api/borrow \
  -H "Content-Type: application/json" \
  -d '{
    "caseNo": "(2024)京民初字第001号",
    "boxNo": "BOX-001-A",
    "borrowerId": "U999",
    "borrowDays": 7
  }'
```

返回 403，提示借阅人处于黑名单并给出拦截原因。

### 3. 归还核验（显示当前责任人）

```bash
curl -X POST http://localhost:8080/api/return \
  -H "Content-Type: application/json" \
  -d '{
    "recordId": "REC-2024-0001"
  }'
```

返回内容包含当前责任人信息及核验结果。

### 4. 逾期提醒（显示最近一次动作）

```bash
curl http://localhost:8080/api/overdue
```

返回每条逾期记录及最近一次操作类型、描述、操作人、时间。

### 5. 借阅记录列表

```bash
curl http://localhost:8080/api/records
```

---

## 演示数据说明

系统启动时自动注入以下演示数据：

| 案号 | 卷宗盒 | 状态 |
|------|--------|------|
| (2024)京民初字第001号 | BOX-001-A / BOX-001-B | 在库（可正常借阅） |
| (2024)京刑初字第088号 | BOX-088-A | 已借出 |
| (2024)京行初字第015号 | BOX-015-A | 逾期未还 |

| 借阅人ID | 姓名 | 状态 |
|----------|------|------|
| U001 | 钱律师 | 正常 |
| U002 | 孙法官 | 正常 |
| U999 | 黑名单用户 | 黑名单（拦截用） |

---

## 目录结构

```
.
├── src/main/java/com/court/archive/
│   ├── CourtArchiveApplication.java   # 启动类
│   ├── common/                        # 通用返回、全局异常
│   ├── controller/                    # REST API
│   ├── dto/                           # 请求/响应DTO
│   ├── entity/                        # 数据实体
│   ├── enums/                         # 枚举定义
│   ├── service/                       # 业务逻辑
│   └── store/                         # 内存数据存储
├── src/main/resources/application.yml
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── pom.xml
└── README.md
```
