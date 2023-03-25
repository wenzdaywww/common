package com.www.common.config.security.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.data.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description 认证失败时的异常处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/15 20:39 </p>
 */
@Slf4j
public class SecurityAuthRejectHandler implements AuthenticationEntryPoint {

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:17 </p>
     * @return
     */
    public SecurityAuthRejectHandler(){
        log.info("启动加载：Security认证自动配置类：注册security配置认证失败时的异常处理");
    }
    /**
     * <p>@Description 认证失败时的异常处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/17 20:30 </p>
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @return void
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("5、访问无认证失败");
        Result<String> responseDTO = new Result<>("无权限访问");
        httpServletResponse.setStatus(HttpStatus.SC_FORBIDDEN);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseDTO));
    }
}
