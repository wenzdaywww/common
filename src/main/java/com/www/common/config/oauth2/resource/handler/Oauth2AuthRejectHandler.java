package com.www.common.config.oauth2.resource.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.config.oauth2.dto.TokenInfoDTO;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.data.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description 资源服务方认证失败时的异常处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/15 20:39 </p>
 */
@Slf4j
public class Oauth2AuthRejectHandler implements AuthenticationEntryPoint {
    @Autowired
    private JwtTokenConverter jwtTokenConverter;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:44 </p>
     * @return
     */
    public Oauth2AuthRejectHandler(){
        log.info("启动加载>>>Oauth2资源服务方自动配置>>>资源服务方认证失败异常处理");
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
        TokenInfoDTO tokenDTO = jwtTokenConverter.decodeToken(httpServletRequest);
        log.error("4、请求认证失败，认证信息：{}，失败原因：{}",JSON.toJSONString(tokenDTO),e.getMessage());
        Result<String> responseDTO = new Result<>("认证失败");
        httpServletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseDTO));
    }
}
