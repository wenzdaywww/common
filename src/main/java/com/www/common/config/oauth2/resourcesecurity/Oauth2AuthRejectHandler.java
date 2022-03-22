package com.www.common.config.oauth2.resourcesecurity;

import com.alibaba.fastjson.JSON;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.pojo.dto.response.ResponseDTO;
import com.www.common.pojo.dto.token.TokenInfoDTO;
import com.www.common.pojo.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

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
@Component
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable") //是否开启oauth2资源服务配置
public class Oauth2AuthRejectHandler implements AuthenticationEntryPoint {
    @Autowired
    private JwtTokenConverter jwtTokenConverter;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:07 </p>
     * @return
     */
    public Oauth2AuthRejectHandler(){
        log.info("资源服务器配置认证失败时的异常处理");
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
