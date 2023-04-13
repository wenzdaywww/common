package com.www.common.config.uaa.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.config.oauth2.dto.TokenInfoDTO;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.config.oauth2.util.RedisTokenHandler;
import com.www.common.config.uaa.UaaProperties;
import com.www.common.data.response.Result;
import com.www.common.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description oauth2退出成功的处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/15 20:51 </p>
 */
@Slf4j
public class Oauth2LogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private UaaProperties uaaProperties;
    @Autowired
    private JwtTokenConverter jwtTokenConverter;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/12 21:27 </p>
     */
    public Oauth2LogoutSuccessHandler(){
        log.info("启动加载>>>单点登录认证服务方自动配置>>>开启oauth2退出成功的处理配置");
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
        log.info("认证服务器退出成功");
        //获取token信息
        TokenInfoDTO tokenInfoDTO = jwtTokenConverter.decodeToken(httpServletRequest);
        //删除用户登录的token到redis中
        RedisTokenHandler.deleteUserIdToken(tokenInfoDTO,uaaProperties.getTokenKeyPrefix());
        Result<String> response = new Result<>("退出成功");
        //清除token
        TokenUtils.clearResponseToken(httpServletResponse, uaaProperties.getCookiesAccessToken(),uaaProperties.getCookiesRefreshToken(),uaaProperties.getCookiesUser());
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(response));
    }
}
