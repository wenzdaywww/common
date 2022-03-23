package com.www.common.config.datasource.sources.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.www.common.config.datasource.sources.IWriteDataSoure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

/**
 * <p>@Description 写权限数据源实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/30 21:56 </p>
 */
@Slf4j
@Primary//优先使用master
@ConfigurationProperties(prefix = "com.www.common.datasource.write")
public class WriteDataSource extends DruidDataSource implements IWriteDataSoure {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/30 23:05 </p>
     * @return
     */
    public WriteDataSource(){
        super();
        log.info("启动加载：多数据源自动配置类：加载写权限数据源");
    }
}
