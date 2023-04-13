-- 数据字典信息
CREATE TABLE IF NOT EXISTS CODE_DATA (
     CODE_TYPE               VARCHAR(100)                            COMMENT 'code类型',
     CODE_NAME               VARCHAR(256)                            COMMENT 'code类型名称',
     CODE_KEY                VARCHAR(100)                            COMMENT 'code的key',
     CODE_VALUE              VARCHAR(100)                            COMMENT '码值',
     VALUE_NAME              VARCHAR(256)                            COMMENT '码值名称',
     IS_VALID                CHAR(1)         DEFAULT '1'             COMMENT '是否有效，1有效，0无效'
);
ALTER TABLE     CODE_DATA           COMMENT '数据字典信息';
CREATE INDEX    INDEX_CODE_TYPE     ON  CODE_DATA (CODE_TYPE);
CREATE INDEX    INDEX_CODE_KEY      ON  CODE_DATA (CODE_KEY);
CREATE INDEX    INDEX_CODE_VALUE    ON  CODE_DATA (CODE_VALUE);
CREATE INDEX    INDEX_IS_VALID      ON  CODE_DATA (IS_VALID);