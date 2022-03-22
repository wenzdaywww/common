package com.www.common.config.aop;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description 请求响应报文AOP拦截配置属性 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 18:28 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.request")
public class RequestAopProperties {
    /** 是否开启请求响应报文AOP拦截 **/
    private Boolean enable;
    /** 请求响应字段过长替换的字符串 **/
    private String content;
    /** 请求响应字段过长时是否开启字符串替换 **/
    private Boolean replace;
    /** 请求响应字段过长时字符串长度限制 **/
    private Integer length;
}
