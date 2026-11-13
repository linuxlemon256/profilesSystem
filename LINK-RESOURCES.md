# 链接资源管理与数据库迁移

两个版本均需在部署前，在各自使用的 profiles 库执行下面的 MySQL 命令。没有自动操作数据库，也未生成 SQL 文件。先 SHOW COLUMNS FROM learning_resource 检查，已有字段不要重复添加。

```sql
USE profiles;
ALTER TABLE learning_resource
    ADD COLUMN safety_status VARCHAR(16) NOT NULL DEFAULT 'UNVERIFIED',
    ADD COLUMN review_note VARCHAR(1000) NULL,
    ADD COLUMN reviewed_at DATETIME NULL;
```

原有记录默认待核验，UNVERIFIED=待核验、VERIFIED=已核验、RISK=存在风险。同一链接可在不同专业下分别建立记录，等级、说明和开关分别维护。

新增默认草稿，管理员选择发布状态。安全状态由管理员人工核验后设置；系统只校验地址格式，不向目标服务器发起请求。HTTPS 不等于内容安全，已核验不保证网站以后不变化。修改地址会重置核验信息；风险记录必须写原因并自动关闭，公共查询额外排除风险记录。通用内容开关也不能直接开放风险资源。

编辑时类别必选，专业与路线可选。后端验证归属；筛选框显示该类别路线，选中的具体专业与路线不相符时请调整。删除只移除本站资源记录，不删除外部内容或物理文件。

安全字段目前不随学习内容包导出。合并导入时，已有资源的核验信息会重置为待核验；完整替换后的新记录也是待核验。导入后请重新审核链接。不会把来源环境的核验结论直接视为可信。

上线顺序：执行字段迁移 → 构建并重启对应服务 → 管理面板维护链接。页面读取失败时先检查字段是否已迁移。不要在未迁移数据库的环境直接部署这版资源查询代码。
