package com.www.common.config.uaa;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
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
    /** 是否开启uaa单点登录认证服务方配置，默认关闭false **/
    private Boolean enable = false;
    /** 登录的http请求，即登录页面loginPage中form的action请求，默认/login **/
    private String login = "/login";
    /** 退出登录的http请求，即form的action请求，默认/logout **/
    private String logout = "/logout";
    /** 自定义登录页面请求url，默认/uaa-login **/
    private String loginUrl = "/uaa-login";
    /** 自定义登录页面thymeleaf文件存在路径，如：resource/uaa/uaa_login.html，只需配置uaa/uaa_login，默认uaa/uaa_login **/
    private String loginPage = "uaa/uaa_login";
    /** 登录失败返回的Session的Attribute的名称，默认error **/
    private String error = "error";
    /** 自定义授权页面thymeleaf文件存在路径，如：resource/uaa/confirm.html，只需配置uaa/confirm，默认uaa/confirm **/
    private String confirmPage = "uaa/confirm";
    /** 自定义授权页面配置的资源服务方客户端的key，默认clientId **/
    private String clientId = "clientId";
    /** 自定义授权页面配置的资源服务方权限范围的key，默认scopes **/
    private String scopes = "scopes";
    /** 保存到cookie的access_token的key，默认access_token **/
    private String cookiesAccessToken = "access_token";
    /** 保存到cookie的refresh_token的key，默认refresh_token **/
    private String cookiesRefreshToken = "refresh_token";
    /** 保存到cookie的user的key，默认user **/
    private String cookiesUser = "user";
    /** 保存到cookie的user的ID的key，默认userId **/
    private String cookiesUserId = "userId";
    /** 保存到cookie的user的角色的key，默认roles **/
    private String cookiesUserRoles = "roles";
    /** jwt令牌签名，即生成token的令牌签名，默认wenzday **/
    private String signingKey = "wenzday";
    /** 用户登录的token保存到redis中的key的前缀，需冒号结尾，默认oauth2_token:user_token: **/
    private String tokenKeyPrefix = "oauth2_token:user_token:";
    /** 文件资源路径 **/
    @Value("${com.www.common.file.url-path:unknown}")
    private String urlPath;
}
