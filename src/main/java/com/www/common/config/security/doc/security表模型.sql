-- 1、角色信息
CREATE TABLE IF NOT EXISTS SYS_ROLE (
    ROLE_ID                   INT           PRIMARY KEY AUTO_INCREMENT            COMMENT '角色主键ID',
    ROLE_CODE                 VARCHAR(100)                                        COMMENT '角色编码，ROLE_开头',
    ROLE_NAME                 VARCHAR(256)                                        COMMENT '角色名称',
    UPDATE_TIME               DATETIME      DEFAULT NOW()                         COMMENT '更新时间',
    CREATE_TIME               DATETIME      DEFAULT NOW()                         COMMENT '创建时间'
);
ALTER TABLE     SYS_ROLE            COMMENT '角色信息';
CREATE INDEX    INDEX_ROLE_CODE     ON  SYS_ROLE (ROLE_CODE);
-- 2、用户信息
CREATE TABLE IF NOT EXISTS SYS_USER (
    SU_ID                     INT             PRIMARY KEY AUTO_INCREMENT          COMMENT '用户主键ID',
    USER_ID                   VARCHAR(40)     NOT NULL UNIQUE                     COMMENT '用户名',
    USER_NAME                 VARCHAR(100)                                        COMMENT '用户昵称',
    PASSWORD                  VARCHAR(200)    NOT NULL                            COMMENT '密码',
    PHONE_NUM                 VARCHAR(11)                                         COMMENT '手机号',
    BIRTHDAY                  DATE                                                COMMENT '生日',
    SEX                       CHAR(1)                                             COMMENT '性别(code=Sex)',
    PHOTO                     VARCHAR(256)                                        COMMENT '头像',
    EMAIL                     VARCHAR(100)                                        COMMENT '邮箱',
    BRIEF                     VARCHAR(256)                                        COMMENT '个人简介',
    STATE_CD                  CHAR(1)         NOT NULL DEFAULT '1'                COMMENT '用户状态(code=UserState)：1有效，2注销，3封号',
    NOT_EXPIRED               CHAR(1)         NOT NULL DEFAULT '1'                COMMENT '是否未过期(code=YesOrNo)：默认为1未过期，0过期',
    NOT_LOCKED                CHAR(1)         NOT NULL DEFAULT '1'                COMMENT '账号是否未锁定(code=YesOrNo)：默认为1未锁定，0锁定',
    CREDENTIALS_NOT_EXPIRED   CHAR(1)         NOT NULL DEFAULT '1'                COMMENT '证书（密码）是否未过期(code=YesOrNo)：默认为1未过期，0过期',
    UPDATE_TIME               DATETIME        DEFAULT NOW()                       COMMENT '更新时间',
    CREATE_TIME               DATETIME        DEFAULT NOW()                       COMMENT '创建时间'
);
ALTER TABLE     SYS_USER            COMMENT '用户信息';
ALTER TABLE     SYS_USER            AUTO_INCREMENT=10000;
CREATE INDEX    INDEX_USER_ID       ON  SYS_USER (USER_ID);
CREATE INDEX    INDEX_USER_NAME     ON  SYS_USER (USER_NAME);
CREATE INDEX    INDEX_STATE_CD      ON  SYS_USER (STATE_CD);
-- 4、用户角色信息
CREATE TABLE IF NOT EXISTS SYS_USER_ROLE (
    SUR_ID                    INT          PRIMARY KEY AUTO_INCREMENT             COMMENT '用户角色主键ID',
    USER_ID                   VARCHAR(40)                                         COMMENT '用户名',
    ROLE_ID                   INT                                                 COMMENT '角色ID',
    UPDATE_TIME               DATETIME     DEFAULT NOW()                          COMMENT '更新时间',
    CREATE_TIME               DATETIME     DEFAULT NOW()                          COMMENT '创建时间'
);
ALTER TABLE     SYS_USER_ROLE       COMMENT '用户角色信息';
CREATE INDEX    INDEX_ROLE_ID       ON  SYS_USER_ROLE (ROLE_ID);
-- 5、角色访问权限信息
CREATE TABLE IF NOT EXISTS AUTHORITY_ROLE (
    AR_ID                     INT             PRIMARY KEY AUTO_INCREMENT          COMMENT '角色权限主键ID',
    URL                       VARCHAR(40)                                         COMMENT '访问路径',
    ROLE_ID                   INT                                                 COMMENT '角色ID',
    URL_TYPE                  CHAR(1)         DEFAULT '1'                         COMMENT 'URL类型：1=http请求路径，2=VUE的路由',
    IS_VALID                  CHAR(1)         DEFAULT '1'                         COMMENT '是否有效：1有效，0无效',
    UPDATE_TIME               DATETIME        DEFAULT NOW()                       COMMENT '更新时间',
    CREATE_TIME               DATETIME        DEFAULT NOW()                       COMMENT '创建时间'
);
ALTER TABLE     AUTHORITY_ROLE       COMMENT '角色访问权限信息';
CREATE INDEX    INDEX_ROLE_ID        ON  AUTHORITY_ROLE (ROLE_ID);















