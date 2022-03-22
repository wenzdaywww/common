package com.www.common.config.datasource.sources.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.www.common.config.datasource.sources.IReadDataSoure;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>@Description 读权限数据源2实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/30 21:56 </p>
 */
@Slf4j
public class ReadTwoDataSource extends DruidDataSource implements IReadDataSoure {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/30 23:05 </p>
     * @return
     */
    public ReadTwoDataSource(){
        super();
        log.info("加载读权限数据源2");
    }
}
