package com.www.common.config.oauth2.resource.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.pojo.dto.response.ResponseDTO;
import com.www.common.pojo.dto.token.TokenInfoDTO;
import com.www.common.pojo.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description 拒绝访问异常处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/24 22:25 </p>
 */
@Slf4j
public class Oauth2UnauthHandler implements AccessDeniedHandler {
    private JwtTokenConverter jwtTokenConverter;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:44 </p>
     * @param jwtTokenConverter 自定义jwt的token转换器
     * @return
     */
    public Oauth2UnauthHandler(JwtTokenConverter jwtTokenConverter){
        this.jwtTokenConverter = jwtTokenConverter;
        log.info("资源服务器配置拒绝访问异常处理");
    }
    /**
     * <p>@Description 拒绝访问异常处理  </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 22:27 </p>
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @return void
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        TokenInfoDTO tokenDTO = jwtTokenConverter.decodeToken(httpServletRequest);
        log.error("4、请求的角色拒绝访问，角色权限信息：{}，拒绝原因：{}",JSON.toJSONString(tokenDTO),e.getMessage());
        ResponseDTO<String> responseDTO = new ResponseDTO<>(ResponseEnum.UNAUTHORIZED,e.getMessage());
        httpServletResponse.setStatus(ResponseEnum.UNAUTHORIZED.getCode());
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseDTO));
    }
}
