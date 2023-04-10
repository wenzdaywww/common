package com.www.common.config.uaa.authorize.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.config.oauth2.dto.TokenInfoDTO;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.config.oauth2.util.RedisTokenHandler;
import com.www.common.config.uaa.controller.OauthController;
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
    /** 自定义jwt的token转换器 **/
    @Autowired
    private JwtTokenConverter jwtTokenConverter;

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
        RedisTokenHandler.deleteUserIdToken(tokenInfoDTO);
        Result<String> response = new Result<>("退出成功");
        //清除token
        TokenUtils.clearResponseToken(httpServletResponse, OauthController.COOKIES_ACCESS_TOKEN,OauthController.COOKIES_REFRESH_TOKEN,OauthController.COOKIES_USER);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(response));
    }
}
