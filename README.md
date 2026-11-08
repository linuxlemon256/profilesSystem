# ProfilesSystem

[简体中文](README.zh-CN.md) · **English** · [Dev edition →](../ProfilesSystem-dev/README.md)

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 4.1.2](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=spring&logoColor=white)
![MySQL 8](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)
![ORM: MyBatis](https://img.shields.io/badge/ORM-MyBatis-DC382D)
[![License: Non-Commercial](https://img.shields.io/badge/License-Non--Commercial-red)](./LICENSE)
![Version](https://img.shields.io/badge/version-v0.4.0-informational)
![Status](https://img.shields.io/badge/status-personal%20project-lightgrey)
![Deployment: Intranet](https://img.shields.io/badge/deployment-intranet-0b7285)

linuxlemon256's personal homepage plus a companion learning system: majors, learning routes and resources, progress tracking, account review, notifications and community feedback, with an admin console on top.

---

ProfilesSystem is a **single-maintainer site built for a small group of users**. Public pages let anyone browse majors and learning routes; registered users can only sign in after an administrator approves them; internal pages are gated by user level. The backend is a Spring Boot monolith (MVC + MyBatis) on MySQL, the frontend is 31 hand-written HTML pages with no npm and no build step.

## Relationship to the dev edition

This repository is the **public edition**, matching what `CHANGELOG.md` describes. [ProfilesSystem-dev](../ProfilesSystem-dev/README.md) in the same workspace is the **dev edition**: it adds study groups, friend messaging, and an LLM Q&A module with token quotas. Some of those modules are not in the CHANGELOG yet.

| | Public edition (this repo) | Dev edition |
| --- | --- | --- |
| Study groups / friend messaging | — | `/api/groups`, `/api/social` |
| LLM Q&A | — | `/api/llm` (OpenAI-compatible / Ollama, quota per level) |
| Pages | 31 | 34 (adds `groups.html`, `communication.html`, `LLMindex.html`) |
| Tables | 11 | 17 (6 more) |
| Version | v0.4.0 | v0.4.0, experimental modules not versioned |

> The relative links work when both directories live in the same workspace; if they ever become two separate repositories, point the links at the corresponding repo URLs.

## Features

**Site and accounts**

- Personal homepage `index.html`, project showcase `projectDemo.html` (level 2 only), account page `account.html`.
- Registration: unique username and email, password of at least 6 characters stored with BCrypt; after registration `isin = 0` and the account waits for review.
- On approval: `isin = 1`, the level is raised from 0 to 1 automatically, and a 10-character identity code is generated (the alphabet drops look-alikes such as `0/O` and `1/I`).
- Three ways to sign in: username + identity code, username + password, email + password. All three require an approved account.
- Recovery: email + identity code resets the password immediately; a lost identity code can be requested and an administrator approval regenerates it. The UI lives in `/recovery.html`.

**Learning system**

- Major catalog: `major_category` → `major`, database-driven, queryable by category slug.
- Learning routes: `learning_route`, only routes with `status = 'PUBLISHED'` and `enabled = 1` are returned.
- Learning resources: `learning_resource`, only rows that are `status = 'PUBLISHED'`, whose resource and category are enabled, and whose `required_level <= user level`; filterable by category, major and keyword.
- Pages: `learnIndex.html`, `learnMajorCategory.html`, `moreMajors.html`, `learncomputer.html`, `learnmechanical.html`, `learnautomation.html`, `resourceDetail.html`, plus the `learnXXX.html` template for new majors (`X` marks the content to fill in).
- Progress: each route keeps its own `routeKey` in the browser; once signed in, the whole JSON blob syncs to `user_progress` through `GET/POST /api/progress` (1 MB cap).

**Operations and admin**

- Admin console `managerPanel.html`: user review, resource CRUD, content transfer (JSON package export/import, rolled back on failure), layered access settings, notifications, community replies, recovery approvals and feature toggles.
- Notifications: `user_notification` rows; users read their own latest 100 and can mark them read; an admin can target a username with a message of at most 2000 characters.
- Community feedback: `feedback.html` / `joinUs.html` create a `community_request`; the user and the admin talk in one `community_message` thread.
- Feature toggles: `feature_toggle` controls the `notifications`, `community` and `storage` modules. When a switch is off, pages redirect to `featureDisabled.html` and APIs answer 403 with the custom reason.

## Tech stack

| Area | Choice |
| --- | --- |
| Language and runtime | Java 17 |
| Framework | Spring Boot 4.1.2 (Spring WebMVC, JDBC, MyBatis 4.1.0 starter) |
| Database | MySQL 8 (HikariCP, max 10 connections) |
| Persistence | MyBatis annotation SQL (`@Select` / `@Insert` / `@Update` / `@Delete`) |
| Passwords | `BCryptPasswordEncoder` from `spring-security-crypto` (no full Spring Security) |
| Auth | `HttpSession` + a `HandlerInterceptor` (`AuthInterceptor`) |
| Frontend | Vanilla HTML / CSS / JavaScript, single-file pages, no build |

> `pom.xml` also pulls in the Redis, RabbitMQ, Batch and WebSocket starters, but no code uses them yet, so they can be removed. `mybatis.mapper-locations` points at `classpath:mapper/*.xml`, yet the repository contains no XML files — every statement is an annotation.

## Project layout

```
ProfilesSystem/
├─ pom.xml
├─ mvnw / mvnw.cmd
├─ CHANGELOG.md / VERSION / LICENSE
├─ src/main/java/com/example/profilessystem/
│  ├─ ProfilesSystemApplication.java
│  ├─ auth/            # SessionKeys, AuthInterceptor (login / level / feature gates)
│  ├─ config/          # AuthConfig (BCrypt), WebConfig (registers the interceptor)
│  ├─ controller/      # 13 controllers
│  ├─ server/          # business services
│  ├─ mapper/          # annotation SQL
│  ├─ entity/          # entities and request bodies
│  └─ result/          # shared Result envelope
├─ src/main/resources/
│  ├─ application.yaml
│  ├─ application-dev.yaml
│  └─ static/          # 31 pages + img.png
└─ src/test/java/.../ProfilesSystemApplicationTests.java
```

## Quick start

### Prerequisites

| Requirement | Notes |
| --- | --- |
| JDK 17+ | Check with `java -version` |
| MySQL 8 | Default database `profiles`, account `profiles` |
| Maven | Use the bundled `mvnw`; no installation needed |

Redis and RabbitMQ are configured but not used at runtime, so they can stay down.

### 1. Create the database and tables

```sql
CREATE DATABASE profiles
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE USER 'profiles'@'localhost' IDENTIFIED BY 'replace-with-your-password';
GRANT ALL PRIVILEGES ON profiles.* TO 'profiles'@'localhost';
```

> The repository ships **no schema script and no seed data**. The shape of each table is defined by the annotation SQL in the `mapper` package, so the tables must exist before the first run. At minimum you need every table listed under [Data tables](#data-tables), otherwise the APIs fail once called.

### 2. Configure the database

`src/main/resources/application.yaml` activates `dev` by default:

```yaml
server:
  port: 8080
spring:
  profiles:
    active: dev
```

Connection settings live in `application-dev.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/profiles?...
    username: profiles
    password: ${DB_PASSWORD:123456}
```

Prefer an environment variable for the password:

```powershell
$env:DB_PASSWORD = "replace-with-your-password"
```

### 3. Run

```powershell
.\mvnw.cmd spring-boot:run
```

Open <http://localhost:8080/index.html>. Learning pages (`learnIndex.html`, ...) need no sign-in; account pages redirect to `login.html`.

### 4. Bootstrap the first admin

The first admin can only be created while the `manager` table is empty. Use <http://localhost:8080/managerLogin.html>, or call the API directly:

```bash
curl -X POST http://localhost:8080/api/manager/bootstrap \
  -H 'Content-Type: application/json' \
  -d '{"managername":"admin","password":"at-least-6-chars"}'
```

Once an admin exists the endpoint is permanently disabled; admin passwords are BCrypt-hashed too.

### 5. Turn on the feature switches

The three switches have no default rows, which means **off**:

```sql
INSERT INTO feature_toggle(feature_key, enabled, reason) VALUES
  ('notifications', 1, ''),
  ('community',     1, ''),
  ('storage',       1, '');
```

While a switch is off, its pages redirect to `featureDisabled.html?feature=xxx` and its APIs answer `403 {"code":403,"message":"该功能暂未开放","reason":"..."}`.

### 6. Import the learning content

The catalog data is not in the repository. Either import a JSON content package through the admin console's content transfer, or write rows into `major_category`, `major`, `learning_route` and `learning_resource` yourself, then use `enabled` and layered access to control visibility.

### 7. Run the tests

```powershell
.\mvnw.cmd test
```

The only test is a `contextLoads` smoke test that starts the full context, so a reachable MySQL is required.

## Intranet deployment

This project is designed to run **on an intranet**: browsers inside the same LAN talk to an intranet server (or your own machine), with no public entry point involved.

- **How to reach it**: the code sets no `server.address`, so Spring Boot listens on `0.0.0.0:8080`; open `http://<server-ip>:8080/index.html` from any machine on the same network.
- **No internet dependency**: all CSS/JS is inline, icons are data URIs, the only local image is `img.png`, and there are no CDNs, web fonts or analytics scripts, so the site opens offline. The GitHub, Bilibili, MOE catalog and filing links are outbound links only; they need internet, and nothing in the site depends on them.
- **Ports**: 8080 for the app; expose MySQL 3306 only to the app server, and restrict sources with a firewall or security group.
- **Sessions**: the login state lives in a `JSESSIONID` cookie and works over plain HTTP inside the LAN; there is no HTTPS and no CSRF token, so treat it as trusted-intranet only.
- **Time zone**: the JDBC URL pins `Asia/Shanghai`; keep the server and database clocks in sync.
- **Backups**: export the `profiles` schema regularly; sessions are in-memory, so a restart logs everyone out.
- **Never map it straight to the public internet**: if you ever need public access, add HTTPS, CSRF protection, login rate limiting and a changed default password first — see [Known limitations](#known-limitations).

## Configuration

| Property | Default | Notes |
| --- | --- | --- |
| `server.port` | `8080` | HTTP port |
| `spring.profiles.active` | `dev` | Must stay `dev`, otherwise no datasource is configured |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/profiles?...` | UTF-8 and `Asia/Shanghai` already set |
| `spring.datasource.username` | `profiles` | Database account |
| `spring.datasource.password` | `${DB_PASSWORD:123456}` | Override with an environment variable |
| `spring.datasource.hikari.*` | max 10, min 2 | Connection pool |
| `spring.data.redis.*` | `localhost:6379` | Reserved, unused |
| `spring.rabbitmq.*` | `localhost:5672` | Reserved, unused |
| `spring.batch.job.enabled` | `false` | Batch is unused |
| `mybatis.configuration.log-impl` | `StdOutImpl` | Prints SQL during development |

## Data tables

| Table | Key columns | Purpose |
| --- | --- | --- |
| `user` | `username`, `password`, `code`, `isin`, `level`, `email`, `reason`, `recovery_*` | Accounts, review state, identity codes, recovery requests |
| `manager` | `id`, `managername`, `password`, `level` | Administrators |
| `major_category` | `slug`, `name`, `field_name`, `icon`, `featured`, `enabled`, `sort_order` | Major categories |
| `major` | `category_id`, `code`, `name`, `program_type`, `enabled` | Majors inside a category |
| `learning_route` | `category_id`, `major_id`, `status`, `enabled` | Learning routes |
| `learning_resource` | `category_id`, `required_level`, `status`, `enabled`, `target_url`, `storage_key` | Learning resources |
| `user_progress` | `username`, `data` | One progress JSON row per user |
| `user_notification` | `username`, `sender_name`, `content`, `read_status` | In-site notifications |
| `feature_toggle` | `feature_key`, `enabled`, `reason` | Module switches |
| `community_request` | `username`, `request_type`, `status`, `contact` | Feedback / join-request tickets |
| `community_message` | `request_id`, `sender_type`, `content` | Messages inside a ticket |

No script defines the column types; create them from the column names in the annotation SQL. The code enforces 255 characters for `user.reason` and a 1 MB cap for `user_progress.data`.

## Access model

**Identities**

- User session: `session.loginUser`; admin session: `session.loginManager`. They do not overwrite each other.
- User levels: `0` guest / `1` associated / `2` internal. `/storage.html` and `/projectDemo.html` require level ≥ 2; the other protected pages require ≥ 1.
- Review state `isin`: `0` pending, `1` approved, `2` rejected. Only `isin = 1` accounts can sign in.

**Paths covered by the interceptor**

| Scope | Rules |
| --- | --- |
| Admin only | `/api/manager/**`, `/managerPanel.html`, `/api/recovery/admin/**`, `/api/community/admin/**`, `/api/notifications/admin/send`, `/api/features/**`, `/api/user/get/all`, `/api/user/insert`, `/api/user/approve/**`, and related |
| Sign-in required | `/api/user/**` (except login/register), `/api/progress`, `/api/community/messages`, `/api/community/{id}/messages`, `/api/notifications` |
| Public | `/api/learning/**`, `/api/recovery/{invite,password,account}`, `/api/community/request`, `/api/ping` |

Unauthenticated API calls return `401 {"code":401,"message":"请先登录"}`; insufficient level returns `403 {"code":403,"message":"当前等级无权访问"}`. Page requests are redirected to `login.html`, `managerLogin.html`, `accessDenied.html` or `featureDisabled.html`.

## API overview

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/ping` | Health check |
| `POST` | `/api/user/register` | Register (username, email, password, reason) |
| `POST` | `/api/user/login` | Username + identity code |
| `POST` | `/api/user/login/password` | Username + password |
| `POST` | `/api/user/login/email` | Email + password |
| `POST` | `/api/user/logout` | Invalidate the session |
| `GET` | `/api/user/me` | Current profile |
| `GET` | `/api/user/level` | Current level |
| `GET` | `/api/learning/categories` | Major categories |
| `GET` | `/api/learning/categories/{slug}` | Majors and routes in a category |
| `GET` | `/api/learning/resources` | Resource list (`category`, `major`, `q` filters) |
| `GET` | `/api/learning/resources/{id}` | Resource detail (level-checked) |
| `GET/POST` | `/api/progress` | Read / save the current user's progress |
| `GET` | `/api/notifications` | My notifications |
| `POST` | `/api/notifications/{id}/read` | Mark as read |
| `POST` | `/api/community/request` | Submit feedback / join request |
| `GET` | `/api/community/messages` | My tickets and messages |
| `POST` | `/api/community/{id}/messages` | Append a message |
| `POST` | `/api/recovery/invite` | Request a new identity code |
| `POST` | `/api/recovery/password` | Reset the password with an identity code |
| `POST` | `/api/recovery/account` | Look up a username by email |
| `GET/POST` | `/api/manager/*` | Admin login, bootstrap, lookups |
| `GET/POST/PUT/DELETE` | `/api/manager/learning/resources[/{id}]` | Resource management |
| `GET` | `/api/manager/learning-access` | Layered access tree |
| `PUT` | `/api/manager/learning-access/{contentType}/{id}` | Open / close one item |
| `GET` | `/api/manager/content-transfer/overview` | Count transferable content |
| `GET` | `/api/manager/content-transfer/export` | Export a JSON package |
| `POST` | `/api/manager/content-transfer/import` | Import a package (transactional, rolls back) |
| `GET` | `/api/features` | Feature switch list |
| `POST` | `/api/features/{feature}` | Save a switch (admin) |

## Pages

| Group | Pages |
| --- | --- |
| Public | `index.html`, `changelog.html`, `learnIndex.html`, `learnMajorCategory.html`, `moreMajors.html`, `learncomputer*`, `learnmechanical*`, `learnautomation*`, `resourceDetail.html`, `resourceDemo.html`, `linkResource.html`, `majorDemo.html`, `routeDemo.html`, `learnXXX.html` |
| Learning space | `learnServer.html` (sign-in required) |
| Account | `login.html`, `register.html`, `recovery.html`, `account.html` |
| Features | `notification.html`, `feedback.html`, `joinUs.html`, `storage.html` (level 2), `projectDemo.html` (level 2) |
| Admin | `managerLogin.html`, `managerPanel.html` |
| System prompts | `accessDenied.html`, `featureDisabled.html` |

`storage.html` is a cloud-drive UI that requires level 2; there is no file backend behind it, so it is a pure frontend page.

## Known limitations

- **No schema script and no seed data**: the catalog, routes and resources must be imported by hand.
- Feature switches have no defaults: without rows in `feature_toggle` every gated module is off.
- Redis, RabbitMQ, Batch and WebSocket appear only in `pom.xml`; no code uses them.
- The cloud-drive page has no upload/download API; the `user.storage` column currently has no purpose.
- Session-based auth with no CSRF token and no login rate limiting; the recovery endpoints are public and `/api/recovery/account` reveals a username from an email address.
- Single instance, no Docker, no CI; the `contextLoads` test depends on a real MySQL.
- All frontend pages are hand-written HTML with no i18n.

## License

Released under the [ProfilesSystem Non-Commercial License](./LICENSE): personal, educational and research use is allowed; **commercial use of any kind is prohibited**, including resale, paid products or services, and paid hosting. Contact the copyright holder for a commercial license.

## Changelog

- Version: see [VERSION](./VERSION), currently `v0.4.0`.
- Every change updates both [CHANGELOG.md](./CHANGELOG.md) and the in-site `changelog.html`.
