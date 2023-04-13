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
    /** 是否开启数据字典读取，默认关闭false **/
    private Boolean readEnable = false;
    /** 定时重新读取数据字典的时间格式，默认每天0点执行 **/
    private String readScheduled = "0 0 * * * ?";
    /** 是否开启数据字典写入redis，默认关闭false **/
    private Boolean writeEnable = false;
    /** 定时重新写入redis数据字典的时间格式，默认每天0点执行 **/
    private String writeScheduled = "0 0 * * * ?";
    /** 数据字典的分布式锁key，不为空则使用分布式锁将数据字典写入redis **/
    private String codeRedisLock = null;
    /** redis中数据字典的key **/
    private String codeRedisKey = null;
}
