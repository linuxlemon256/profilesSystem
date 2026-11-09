# 内容审核部署说明

两个版本各自连接的 profiles 库都需要执行下面的 MySQL 命令；没有新增 SQL 文件，也不会自动建表或写库。先建表并添加基础词语，再发布程序，避免审核未就绪导致文本提交暂停。下面的初始词语仅用于起步，需管理员持续按本站规则增删，不是完整词库。

```sql
USE profiles;
CREATE TABLE IF NOT EXISTS sensitive_word (
 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 word VARCHAR(128) NOT NULL,
 normalized VARCHAR(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 UNIQUE KEY uk_sensitive_word_normalized (normalized)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT IGNORE INTO sensitive_word(word, normalized) VALUES
 ('傻逼','傻逼'),('煞笔','煞笔'),('操你妈','操你妈'),('草泥马','草泥马'),
 ('去死吧','去死吧'),('代开发票','代开发票'),('办假证','办假证'),
 ('出售银行卡','出售银行卡'),('出售身份证','出售身份证'),
 ('买卖人口','买卖人口'),('出售毒品','出售毒品'),('出售枪支','出售枪支');
```

管理员打开管理面板“内容审核 · 敏感词库”，可每行一词批量添加、搜索及逐条删除。单次最多200行，词库最多20000条。每个词2至64个有效文字/数字。删除最后一个词会暂停文本提交。新增词不应直接使用SQL写入，以免规范化去重字段不一致。

统一审核位于 RequestBodyAdvice，在 Controller 执行和写入数据库之前检查 JSON DTO 中 content/message/reason/name/username/description/title/recoveryReason 及嵌套对象。覆盖私聊、学习组聊天、组名称说明、反馈、加入申请、注册申请理由、管理员回复和通知；内部版的模型消息也检查。登录、密码、身份码、邮箱及词库维护不参与词匹配。

匹配先做两轮HTML实体解码、Unicode NFKC、大小写折叠，并只保留文字数字，因此全角、空格、标点、常见零宽/组合字符插入无法绕过。使用字典树匹配，不把管理员输入当作正则或SQL执行。文本字段最大20000字符，复杂请求限制深度与节点数。词库仅管理员会话可访问；页面使用textContent显示词语，避免词条作为HTML执行。

本机新增删除立即清缓存，多实例最多5秒刷新一次。数据库异常或空词库时提交返回503，不使用过期词库放行；超过长度或命中时返回400，响应不泄露命中词或完整用户消息。

限制：不是语义模型，谐音、拼音、繁简、形似文字、图片、拆成多条消息或上下文暗语不能保证识别；不检查历史内容、数据库直接写入或未来绕过HTTP的入口。短词可能误伤学习资料或反馈引文，请审慎维护。需对未来新增的文本字段扩展字段清单，不应把敏感词审核作为身份权限校验、XSS防护或人工举报机制的替代。

验收：两个关联账户分别私聊和组聊发送普通文本与词条的空格、全角、零宽变体；确认拒绝内容未入库；普通账户访问词库管理应返回401，管理员增删后立即生效；断开词库数据库应拒绝发送并给出暂不可用提示。
