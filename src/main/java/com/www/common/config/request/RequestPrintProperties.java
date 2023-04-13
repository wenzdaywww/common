package com.www.common.config.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description 请求响应报文打印自动配置属性类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 18:28 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.request")
public class RequestPrintProperties {
    /** 是否开启请求响应报文打印，默认关闭false **/
    private Boolean enable = false;
    /** 请求响应字段过长替换的字符串，默认为<longText> **/
    private String content = "<longText>";
    /** 请求响应字段过长时是否开启字符串替换，默认开启true **/
    private Boolean replace = true;
    /** 请求响应字段过长时字符串长度限制，默认长度256 **/
    private Integer length = 256;
}
