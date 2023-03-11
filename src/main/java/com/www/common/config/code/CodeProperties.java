package com.www.common.config.code;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description 数据字典配置属性类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 20:37 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.code")
public class CodeProperties {
    /** 是否开启数据字典读取 **/
    private Boolean readEnable = false;
    /** 定时重新读取数据字典的时间格式 **/
    private String readScheduled = "0 0 * * * ?";
    /** redis中数据字典的key **/
    private String codeDataKey = null;
}
