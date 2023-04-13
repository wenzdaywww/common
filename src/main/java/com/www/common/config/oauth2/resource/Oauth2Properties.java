package com.www.common.config.oauth2.resource;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description oauth2资源方认证配置属性 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 22:19 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.oauth2")
public class Oauth2Properties {
    /** 是否开启oauth2资源方认证配置，默认关闭false **/
    private Boolean enable = false;
    /** jwt令牌签名，即认证服务方应用用户token的令牌签名，默认wenzday **/
    private String signingKey = "wenzday";
    /** 用户登录的token保存到redis中的key的前缀，结尾不含冒号，需与uaa包的认证服务方应用保持一致，默认oauth2_token:user_token: **/
    private String tokenKeyPrefix = "oauth2_token:user_token";
    /** 资源服务ID的url的scope的redis的key前缀,结尾不含冒号，格式如：oauth2:resource_id:url_scope **/
    private String urlScopePrefix;
    /** 资源服务id，必须配置 **/
    @Value("${spring.application.name}")
    private String resourceId;
    /** 文件资源路径 **/
    @Value("${com.www.common.file.url-path:unknown}")
    private String urlPath;
}
