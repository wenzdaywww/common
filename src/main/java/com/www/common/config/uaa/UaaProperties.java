package com.www.common.config.uaa;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description uaa单点登录认证服务方配置属性类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/23 09:16 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.uaa")
public class UaaProperties {
    /** 是否开启uaa单点登录认证服务方配置 **/
    private Boolean enable = false;
}
