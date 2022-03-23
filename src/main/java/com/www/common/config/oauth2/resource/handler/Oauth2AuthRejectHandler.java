package com.www.common.config.oauth2.resource.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.pojo.dto.response.ResponseDTO;
import com.www.common.pojo.dto.token.TokenInfoDTO;
import com.www.common.pojo.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description 资源服务器认证失败时的异常处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/15 20:39 </p>
 */
@Slf4j
public class Oauth2AuthRejectHandler implements AuthenticationEntryPoint {
    private JwtTokenConverter jwtTokenConverter;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:44 </p>
     * @param jwtTokenConverter 自定义jwt的token转换器
     * @return
     */
    public Oauth2AuthRejectHandler(JwtTokenConverter jwtTokenConverter){
        this.jwtTokenConverter = jwtTokenConverter;
        log.info("注册资源服务器配置认证失败时的异常处理");
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
        ResponseDTO<String> responseDTO = new ResponseDTO<>(ResponseEnum.UNAUTHORIZED,"认证失败");
        httpServletResponse.setStatus(ResponseEnum.UNAUTHORIZED.getCode());
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseDTO));
    }
}
