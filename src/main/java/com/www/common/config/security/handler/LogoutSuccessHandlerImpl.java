package com.www.common.config.security.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.data.response.Response;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>@Description 退出成功的处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/15 20:51 </p>
 */
@Slf4j
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private SecurityRedisHandler securityRedisHandler;


    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:16 </p>
     * @return
     */
    public LogoutSuccessHandlerImpl(){
        log.info("启动加载：Security认证自动配置类：注册security配置退出成功的处理");
    }
    /**
     * <p>@Description 退出成功处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/17 20:31 </p>
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication
     * @return void
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("6、security退出成功");
        Cookie cookie = WebUtils.getCookie(httpServletRequest,LoginSuccessHandler.COOKIE_TOKEN);
        String token = cookie != null ? cookie.getValue() : "";
        Map<String,Object> map = TokenUtils.validateTokenAndGetClaims(token);
        //获取token，删除redis中的token
        if(map != null && map.size() > 0) {
            String userId = String.valueOf(map.get(TokenUtils.USERID));
            securityRedisHandler.deleteToken(userId);
        }
        Response<String> responseDTO = new Response<>(ResponseEnum.SUCCESS,"退出成功");
        TokenUtils.clearResponseToken(httpServletResponse,LoginSuccessHandler.COOKIE_TOKEN);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseDTO));
    }
}
