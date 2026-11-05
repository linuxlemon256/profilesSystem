# ProfilesSystem

**简体中文** · [English](README.md) · [开发版 →](../ProfilesSystem-dev/README.zh-CN.md)

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 4.1.2](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=spring&logoColor=white)
![MySQL 8](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)
![ORM: MyBatis](https://img.shields.io/badge/ORM-MyBatis-DC382D)
[![License: Non-Commercial](https://img.shields.io/badge/License-Non--Commercial-red)](./LICENSE)
![Version](https://img.shields.io/badge/version-v0.4.0-informational)
![Status](https://img.shields.io/badge/status-personal%20project-lightgrey)
![部署环境: 内网](https://img.shields.io/badge/%E9%83%A8%E7%BD%B2%E7%8E%AF%E5%A2%83-%E5%86%85%E7%BD%91-0b7285)

linuxlemon256 的个人主页，以及配套的专业学习系统：专业目录、学习路线与资源、学习进度、账号审核、通知与社区反馈，外加一套管理员后台。

---

ProfilesSystem 是一个**单人维护、面向小范围用户**的站点。公开页面可以浏览专业与学习路线；注册用户要等管理员审核通过后才能登录；内部页面按用户等级开放。后端是 Spring Boot 单体（MVC + MyBatis），数据库是 MySQL，前端是 31 个原生 HTML 页面，没有 npm、没有构建步骤。

## 与开发版的关系

本仓库是**公众版**，功能范围与 `CHANGELOG.md` 一致。同工作区里的 [ProfilesSystem-dev](../ProfilesSystem-dev/README.zh-CN.md) 是**开发版**，在公众版基础上多了学习组、好友私信和大模型问答（含额度控制），其中部分模块还没写进 CHANGELOG。

| | 公众版（本仓库） | 开发版 |
| --- | --- | --- |
| 学习组 / 好友私信 | — | `/api/groups`、`/api/social` |
| 大模型问答 | — | `/api/llm`（OpenAI 兼容 / Ollama，按等级限额） |
| 页面 | 31 个 | 34 个（多 `groups.html`、`communication.html`、`LLMindex.html`） |
| 数据表 | 11 张 | 17 张（多 6 张） |
| 版本号 | v0.4.0 | v0.4.0，但实验模块未计入版本 |

> 两个目录在同一工作区时相对链接可以直接打开；如果它们将来拆成两个仓库，把上面的链接换成对应仓库地址即可。

## 功能

**站点与账户**

- 个人主页 `index.html`、项目展示 `projectDemo.html`（等级 2 可见）、账户页 `account.html`。
- 注册：用户名唯一、邮箱唯一、密码至少 6 位，密码用 BCrypt 存储；注册后 `isin = 0`，等待审核。
- 审核通过：`isin = 1`，等级从 0 自动升到 1，并生成 10 位身份码（去掉了 `0/O`、`1/I` 等易混字符）。
- 三种登录方式：账户 + 身份码、账户 + 密码、邮箱 + 密码；三种都要求账户已审核通过。
- 找回：邮箱 + 身份码重置密码（立即生效）；身份码丢失可提交申请，管理员审批后重新生成；`/recovery.html` 提供页面。

**学习系统**

- 专业目录：`major_category` → `major` 两级，数据库驱动，支持按类目 slug 查询。
- 学习路线：`learning_route`，只返回 `status = 'PUBLISHED'` 且 `enabled = 1` 的路线。
- 学习资源：`learning_resource`，只返回 `status = 'PUBLISHED'`、资源与所属类别已开放、`required_level <= 用户等级` 的记录；支持按类目、专业、关键词搜索。
- 页面：`learnIndex.html`、`learnMajorCategory.html`、`moreMajors.html`、`learncomputer.html`、`learnmechanical.html`、`learnautomation.html`、`resourceDetail.html`，以及给新专业用的标准模板 `learnXXX.html`（`X` 是待填内容）。
- 学习进度：每条路线用独立 `routeKey` 存在浏览器；登录后通过 `GET/POST /api/progress` 把整段 JSON 同步到 `user_progress`（上限 1 MB）。

**运营与后台**

- 管理后台 `managerPanel.html`：用户审核、学习资源增删改、内容迁移（JSON 内容包导出/导入，导入失败整体回滚）、分层学习内容开放设置、通知群发、社区反馈回复、找回申请审批、功能开关。
- 通知：站内 `user_notification`，用户只读自己的 100 条，可标记已读；管理员可按用户名发送（≤ 2000 字）。
- 社区反馈：`feedback.html` / `joinUs.html` 提交 `community_request`，用户与管理员在同一 `community_message` 线程里对话。
- 功能开关：`feature_toggle` 控制 `notifications`、`community`、`storage` 三个模块；开关关闭时页面跳 `featureDisabled.html`，接口返回 403 和自定义原因。

## 技术栈

| 类别 | 选型 |
| --- | --- |
| 语言与运行时 | Java 17 |
| 框架 | Spring Boot 4.1.2（Spring WebMVC、JDBC、MyBatis 4.1.0 starter） |
| 数据库 | MySQL 8（HikariCP 连接池，最大 10 个连接） |
| 持久层 | MyBatis 注解 SQL（`@Select` / `@Insert` / `@Update` / `@Delete`） |
| 密码 | `spring-security-crypto` 的 BCryptPasswordEncoder（没有引入完整的 Spring Security） |
| 鉴权 | `HttpSession` + `HandlerInterceptor`（`AuthInterceptor`） |
| 前端 | 原生 HTML / CSS / JavaScript，单文件页面，无构建 |

> `pom.xml` 里还引入了 Redis、RabbitMQ、Batch 和 WebSocket 的 starter，但当前代码没有对应的实现或使用，可以按需删掉。`mybatis.mapper-locations` 指向 `classpath:mapper/*.xml`，仓库里实际没有 XML，SQL 全部写在注解里。

## 目录结构

```
ProfilesSystem/
├─ pom.xml
├─ mvnw / mvnw.cmd
├─ CHANGELOG.md / VERSION / LICENSE
├─ src/main/java/com/example/profilessystem/
│  ├─ ProfilesSystemApplication.java
│  ├─ auth/            # SessionKeys、AuthInterceptor（登录 / 等级 / 功能开关）
│  ├─ config/          # AuthConfig（BCrypt）、WebConfig（注册拦截器）
│  ├─ controller/      # 13 个控制器
│  ├─ server/          # 业务服务
│  ├─ mapper/          # 注解 SQL
│  ├─ entity/          # 实体与请求体
│  └─ result/          # 统一返回结构 Result
├─ src/main/resources/
│  ├─ application.yaml
│  ├─ application-dev.yaml
│  └─ static/          # 31 个页面 + img.png
└─ src/test/java/.../ProfilesSystemApplicationTests.java
```

## 快速开始

### 前置条件

| 依赖 | 说明 |
| --- | --- |
| JDK 17+ | `java -version` 确认 |
| MySQL 8 | 默认库名 `profiles`，账号 `profiles` |
| Maven | 用仓库自带的 `mvnw`，不用预装 |

Redis 与 RabbitMQ 的配置目前不参与运行，可以不启动。

### 1. 建库与建表

```sql
CREATE DATABASE profiles
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE USER 'profiles'@'localhost' IDENTIFIED BY '换成你的密码';
GRANT ALL PRIVILEGES ON profiles.* TO 'profiles'@'localhost';
```

> 仓库**没有建表脚本，也没有种子数据**。表结构由 `mapper` 包里的注解 SQL 决定，第一次运行前需要自己建表。至少要建下面这些表，否则启动后接口会报错。

### 2. 配置数据库

`src/main/resources/application.yaml` 默认激活 `dev`：

```yaml
server:
  port: 8080
spring:
  profiles:
    active: dev
```

`application-dev.yaml` 里的连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/profiles?...
    username: profiles
    password: ${DB_PASSWORD:123456}
```

把密码写进环境变量（推荐）：

```powershell
$env:DB_PASSWORD = "换成你的密码"
```

### 3. 启动

```powershell
.\mvnw.cmd spring-boot:run
```

打开 <http://localhost:8080/index.html>。学习页面（`learnIndex.html` 等）无需登录；账户相关页面会跳转到 `login.html`。

### 4. 初始化管理员

管理员表为空时才可以创建第一个管理员。打开 <http://localhost:8080/managerLogin.html> 走初始化流程，或者直接调用：

```bash
curl -X POST http://localhost:8080/api/manager/bootstrap \
  -H 'Content-Type: application/json' \
  -d '{"managername":"admin","password":"至少六位"}'
```

初始化成功后该接口永久失效；管理员密码同样是 BCrypt 存储。

### 5. 打开功能开关

三个功能开关默认没有任何行，等于**关闭**：

```sql
INSERT INTO feature_toggle(feature_key, enabled, reason) VALUES
  ('notifications', 1, ''),
  ('community',     1, ''),
  ('storage',       1, '');
```

开关关闭时，对应页面会跳 `featureDisabled.html?feature=xxx`，接口返回 `403 {"code":403,"message":"该功能暂未开放","reason":"..."}`。

### 6. 导入学习内容

专业目录数据不在仓库里。可以用管理后台的「数据内容迁移」导入 JSON 内容包，也可以直接往 `major_category`、`major`、`learning_route`、`learning_resource` 里写数据，再用 `enabled` 和「分层开放」控制可见性。

### 7. 运行测试

```powershell
.\mvnw.cmd test
```

目前只有一个 `contextLoads` 冒烟测试，会加载完整上下文，所以运行前需要有可连接的 MySQL。

## 内网部署

本仓库按**内网运行**设计：同一局域网里的浏览器访问内网服务器（或本机），不依赖公网入口。

- **访问方式**：代码没有设置 `server.address`，Spring Boot 默认监听 `0.0.0.0:8080`，同网段浏览器打开 `http://<服务器IP>:8080/index.html` 即可。
- **不依赖外网**：页面的 CSS/JS 全部内联，图标是 data URI，唯一的本地图片是 `img.png`，没有 CDN、外部字体或统计脚本，断网也能正常打开。页面里的 GitHub、Bilibili、教育部目录和备案链接只是外跳，内网点开需要外网，不影响站内功能。
- **端口放行**：应用 8080；MySQL 3306 只对应用服务器开放。用防火墙或安全组把来源限制在可信网段。
- **登录与会话**：登录态保存在 `JSESSIONID` Cookie 里，内网 HTTP 下可正常使用；项目没有 HTTPS 和 CSRF Token，只适合可信内网。
- **时区**：JDBC 连接串固定 `Asia/Shanghai`，服务器与数据库时间保持一致。
- **备份**：定期导出 `profiles` 库；会话存在内存中，重启后需要重新登录。
- **不要直接映射到公网**：确需公网访问时，先补上 HTTPS、CSRF 防护、登录限流和默认密码修改等措施，详见[已知限制](#已知限制)。

## 配置项

| 配置 | 默认值 | 说明 |
| --- | --- | --- |
| `server.port` | `8080` | 服务端口 |
| `spring.profiles.active` | `dev` | 必须激活 `dev`，否则没有数据源配置 |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/profiles?...` | 已带 `utf8` 与 `Asia/Shanghai` |
| `spring.datasource.username` | `profiles` | 数据库账号 |
| `spring.datasource.password` | `${DB_PASSWORD:123456}` | 建议用环境变量覆盖 |
| `spring.datasource.hikari.*` | 最大 10、最小 2 | 连接池 |
| `spring.data.redis.*` | `localhost:6379` | 预留，代码未使用 |
| `spring.rabbitmq.*` | `localhost:5672` | 预留，代码未使用 |
| `spring.batch.job.enabled` | `false` | Batch 未使用 |
| `mybatis.configuration.log-impl` | `StdOutImpl` | 开发期打印 SQL |

## 数据表

| 表 | 关键列 | 用途 |
| --- | --- | --- |
| `user` | `username`、`password`、`code`、`isin`、`level`、`email`、`reason`、`recovery_*` | 用户与审核、身份码、找回申请 |
| `manager` | `id`、`managername`、`password`、`level` | 管理员 |
| `major_category` | `slug`、`name`、`field_name`、`icon`、`featured`、`enabled`、`sort_order` | 专业大类 |
| `major` | `category_id`、`code`、`name`、`program_type`、`enabled` | 具体专业 |
| `learning_route` | `category_id`、`major_id`、`status`、`enabled` | 学习路线 |
| `learning_resource` | `category_id`、`required_level`、`status`、`enabled`、`target_url`、`storage_key` | 学习资源 |
| `user_progress` | `username`、`data` | 学习进度 JSON，按用户一行 |
| `user_notification` | `username`、`sender_name`、`content`、`read_status` | 站内通知 |
| `feature_toggle` | `feature_key`、`enabled`、`reason` | 模块开关 |
| `community_request` | `username`、`request_type`、`status`、`contact` | 社区反馈工单 |
| `community_message` | `request_id`、`sender_type`、`content` | 工单对话 |

字段类型没有脚本可参考，按注解 SQL 里的列名建表即可；`user.reason` 在代码里按 255 字符校验，`user_progress.data` 上限 1 MB。

## 权限模型

**身份**

- 用户会话：`session.loginUser`；管理员会话：`session.loginManager`，两者互不覆盖。
- 用户等级：`0` 游客 / `1` 关联人员 / `2` 内部人员。`/storage.html` 与 `/projectDemo.html` 要求等级 ≥ 2，其他受保护页面要求 ≥ 1。
- 审核状态 `isin`：`0` 待审核、`1` 通过、`2` 拒绝。`isin = 1` 才允许登录。

**拦截器保护的路径**

| 范围 | 规则 |
| --- | --- |
| 管理员 | `/api/manager/**`、`/managerPanel.html`、`/api/recovery/admin/**`、`/api/community/admin/**`、`/api/notifications/admin/send`、`/api/features/**`、`/api/user/get/all`、`/api/user/insert`、`/api/user/approve/**` 等 |
| 需要登录 | `/api/user/**`（登录/注册接口除外）、`/api/progress`、`/api/community/messages`、`/api/community/{id}/messages`、`/api/notifications` |
| 公开 | `/api/learning/**`、`/api/recovery/{invite,password,account}`、`/api/community/request`、`/api/ping` |

未登录访问接口返回 `401 {"code":401,"message":"请先登录"}`；等级不足返回 `403 {"code":403,"message":"当前等级无权访问"}`；页面请求会被重定向到 `login.html`、`managerLogin.html`、`accessDenied.html` 或 `featureDisabled.html`。

## API 一览

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/ping` | 健康检查 |
| `POST` | `/api/user/register` | 注册（用户名、邮箱、密码、申请理由） |
| `POST` | `/api/user/login` | 账户 + 身份码登录 |
| `POST` | `/api/user/login/password` | 账户 + 密码登录 |
| `POST` | `/api/user/login/email` | 邮箱 + 密码登录 |
| `POST` | `/api/user/logout` | 退出并销毁会话 |
| `GET` | `/api/user/me` | 当前用户资料 |
| `GET` | `/api/user/level` | 当前用户等级 |
| `GET` | `/api/learning/categories` | 专业大类列表 |
| `GET` | `/api/learning/categories/{slug}` | 大类下的专业与路线 |
| `GET` | `/api/learning/resources` | 资源列表（`category`、`major`、`q` 过滤） |
| `GET` | `/api/learning/resources/{id}` | 资源详情（按等级校验） |
| `GET/POST` | `/api/progress` | 读取 / 保存当前用户学习进度 |
| `GET` | `/api/notifications` | 我的通知 |
| `POST` | `/api/notifications/{id}/read` | 标记已读 |
| `POST` | `/api/community/request` | 提交反馈 / 加入申请 |
| `GET` | `/api/community/messages` | 我的工单与消息 |
| `POST` | `/api/community/{id}/messages` | 追加消息 |
| `POST` | `/api/recovery/invite` | 身份码找回申请 |
| `POST` | `/api/recovery/password` | 用身份码重置密码 |
| `POST` | `/api/recovery/account` | 按邮箱查询账户名 |
| `GET/POST` | `/api/manager/*` | 管理员登录、初始化、查询 |
| `GET/POST/PUT/DELETE` | `/api/manager/learning/resources[/{id}]` | 学习资源管理 |
| `GET` | `/api/manager/learning-access` | 分层开放树 |
| `PUT` | `/api/manager/learning-access/{contentType}/{id}` | 开放 / 关闭某条内容 |
| `GET` | `/api/manager/content-transfer/overview` | 可迁移内容数量 |
| `GET` | `/api/manager/content-transfer/export` | 导出 JSON 内容包 |
| `POST` | `/api/manager/content-transfer/import` | 导入内容包（事务，失败整体回滚） |
| `GET` | `/api/features` | 功能开关列表 |
| `POST` | `/api/features/{feature}` | 保存开关（管理员） |

## 前端页面

| 分组 | 页面 |
| --- | --- |
| 公开 | `index.html`、`changelog.html`、`learnIndex.html`、`learnMajorCategory.html`、`moreMajors.html`、`learncomputer*`、`learnmechanical*`、`learnautomation*`、`resourceDetail.html`、`resourceDemo.html`、`linkResource.html`、`majorDemo.html`、`routeDemo.html`、`learnXXX.html` |
| 学习空间 | `learnServer.html`（需登录） |
| 账户 | `login.html`、`register.html`、`recovery.html`、`account.html` |
| 功能 | `notification.html`、`feedback.html`、`joinUs.html`、`storage.html`（等级 2）、`projectDemo.html`（等级 2） |
| 后台 | `managerLogin.html`、`managerPanel.html` |
| 系统提示 | `accessDenied.html`、`featureDisabled.html` |

`storage.html` 是云盘界面，需要等级 2；当前没有后端文件接口，属于纯前端页面。

## 已知限制

- **没有建表脚本和种子数据**：目录、路线、资源都要自己导入。
- 功能开关没有默认值：不插入 `feature_toggle` 行等于全部关闭。
- Redis、RabbitMQ、Batch、WebSocket 只在 `pom.xml` 里，代码中未使用。
- 云盘页没有上传/下载接口，`user.storage` 列目前没有实际用途。
- 会话登录，没有 CSRF Token、没有登录限流；找回接口无需登录，`/api/recovery/account` 可按邮箱查到账户名。
- 单实例部署，没有 Docker、没有 CI；`contextLoads` 测试依赖真实 MySQL。
- 前端全部是手写 HTML，没有国际化。

## 许可证

本项目采用 [ProfilesSystem Non-Commercial License](./LICENSE)：允许个人、教育、研究等非商业用途；**禁止任何商业用途**，包括销售、付费产品/服务、付费托管等。需要商用请联系版权持有人。

## 变更记录

- 版本号见 [VERSION](./VERSION)，当前 `v0.4.0`。
- 每次改动同步更新 [CHANGELOG.md](./CHANGELOG.md) 与站内 `changelog.html`。
