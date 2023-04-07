package com.www.common.config.transaction;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description 全局事物管理属性配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/4/7 22:29 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.transaction")
public class TransactionProperties {
    /** 是否开启全局事物管理 **/
    private Boolean enable = false;
    /** 全局事物管理AOP拦截路径 **/
    private String aopPointcut = "execution(* com.www..*.service..*.*(..))";
}
