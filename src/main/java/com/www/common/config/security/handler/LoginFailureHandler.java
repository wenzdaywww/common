package com.www.common.config.security.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.data.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description security登录认证失败处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:11 </p>
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler  {

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:14 </p>
     * @return
     */
    public LoginFailureHandler(){
        log.info("启动加载：Security认证自动配置类：注册security配置登录认证失败处理");
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
        log.info("3、security登录认证失败处理");
        String msg = "登录认证失败";
        if(exception == null){
            msg = "未知错误!";
        }else if (exception instanceof LockedException) {
            msg = "账户被锁定，请联系管理员!";
        } else if (exception instanceof CredentialsExpiredException) {
            msg = "密码过期，请联系管理员!";
        } else if (exception instanceof AccountExpiredException) {
            msg = "账户过期，请联系管理员!";
        } else if (exception instanceof DisabledException) {
            msg = "账户被禁用，请联系管理员!";
        } else if (exception instanceof BadCredentialsException) {
            msg = "用户名或者密码输入错误，请重新输入!";
        }
        Result<String> responseDTO = new Result<>(msg);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(responseDTO));
    }
}
