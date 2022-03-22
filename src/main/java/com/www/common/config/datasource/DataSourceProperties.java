package com.www.common.config.datasource;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description 多数据源配置属性 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:51 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.datasource")
public class DataSourceProperties {
    /**  是否开启多数据源配置 **/
    private Boolean enable;
    /**  是否开启druid监控平台 **/
    private Boolean monitor;
    /**  druid监控平台用户 **/
    private String monitorName;
    /**  druid监控平台密码 **/
    private String monitorPwd;
}
