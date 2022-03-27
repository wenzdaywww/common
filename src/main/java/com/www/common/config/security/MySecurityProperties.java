package com.www.common.config.security;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description Security认证配置属性 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/23 09:16 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.securuty")
public class MySecurityProperties {
    /** 是否开启Security认证 **/
    private Boolean enable = false;
    /** jwt令牌签名 */
    private String secretKey = "wenzday";
    /**  过期时间（秒） */
    private int expireTimeSecond = 259200;
    /** cookie免登录有效天数 **/
    private int cookieDay = 3;
    /** 使用redis保存用户的token的key前缀 **/
    private String userPrefix;
}
