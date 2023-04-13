package com.www.common.config.uaa.handler;

import com.www.common.config.uaa.UaaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description oauth2登录认证失败处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:11 </p>
 */
@Slf4j
public class Oauth2LoginFailureHandler implements AuthenticationFailureHandler  {
    @Autowired
    private UaaProperties uaaProperties;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/12 21:27 </p>
     */
    public Oauth2LoginFailureHandler(){
        log.info("启动加载>>>单点登录认证服务方自动配置>>>开启oauth2登录认证失败处理配置");
    }
    /**
     * <p>@Description 登录失败处理事件 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:12 </p>
     * @param request 请求报文
     * @param response 响应报文
     * @param exception 认证异常
     * @return void
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = "未知原因";
        if (exception instanceof LockedException) {
            msg = "账户被锁定，请联系管理员!";
        } else if (exception instanceof CredentialsExpiredException) {
            msg = "密码过期，请联系管理员!";
        } else if (exception instanceof AccountExpiredException) {
            msg = "账户过期，请联系管理员!";
        } else if (exception instanceof DisabledException) {
            msg = "账户被禁用，请联系管理员!";
        } else if ((exception instanceof BadCredentialsException ) || (exception instanceof InternalAuthenticationServiceException)) {
            msg = "用户名或者密码输入错误，请重新输入!";
        }
        log.error("登录认证服务器认证失败处理,失败原因：{}",msg);
        request.getSession().setAttribute(uaaProperties.getError(),msg);
        response.sendRedirect(uaaProperties.getLoginUrl());
    }
}
