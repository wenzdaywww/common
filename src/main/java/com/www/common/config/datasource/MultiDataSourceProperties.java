package com.www.common.config.datasource;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description 多数据源配置属性类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:51 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.datasource")
public class MultiDataSourceProperties {
    /**  是否开启多数据源配置，默认关闭false **/
    private Boolean enable = false;
    /**  是否开启druid监控平台，默认关闭false **/
    private Boolean monitor = false;
    /**  druid监控平台用户，默认admin **/
    private String monitorName = "admin";
    /**  druid监控平台密码，默认www362412 **/
    private String monitorPwd = "www362412";
}
