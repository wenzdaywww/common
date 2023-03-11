package com.www.common.config.security.meta;

import com.www.common.config.security.MySecurityProperties;
import com.www.common.config.security.handler.LoginSuccessHandler;
import com.www.common.config.security.handler.SecurityRedisHandler;
import com.www.common.config.security.impl.UserDetailsServiceImpl;
import com.www.common.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * <p>@Description token验证拦截器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/16 21:06 </p>
 */
@Slf4j
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private SecurityRedisHandler securityRedisHandler;
    @Autowired
    private MySecurityProperties mySecurityProperties;


    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:12 </p>
     * @return
     */
    public JwtAuthorizationTokenFilter(){
        log.info("启动加载：Security认证自动配置类：注册security配置token验证拦截器");
    }
    /**
     * <p>@Description 设置token过期时间和密钥 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:57 </p>
     * @return void
     */
    @PostConstruct
    public void setSecretAndExpireTime(){
        TokenUtils.setSecretAndExpireTime(mySecurityProperties.getExpireTimeSecond(),mySecurityProperties.getSecretKey());
    }
    /**
     * <p>@Description token验证 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/17 19:24 </p>
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @return void
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = WebUtils.getCookie(httpServletRequest,LoginSuccessHandler.COOKIE_TOKEN);
        String token = cookie != null ? cookie.getValue() : "";
        log.info("1、security访问token验证，token={}",token);
        Map<String,Object> map = TokenUtils.validateTokenAndGetClaims(token);
        if(map != null && map.size() > 0){
            String userId = String.valueOf(map.get(TokenUtils.USERID));
            //判断redis中的token是否存在且token值相等，存在则说明token有效
            if(securityRedisHandler.hasToken(userId)
                    && StringUtils.equals((String)map.get(TokenUtils.AUTHORIZATION), securityRedisHandler.getToken(userId))
                    && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                if(userDetails != null){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
    /**
     * <p>@Description 从当前请求头中获取当前登录的用户ID </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/22 16:48 </p>
     * @return java.lang.String 用户ID
     */
    public String getUserId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null){
            HttpServletRequest request = attributes.getRequest();
            Cookie cookie = WebUtils.getCookie(request, LoginSuccessHandler.COOKIE_TOKEN);
            String token = Optional.ofNullable(cookie).map(c -> c.getValue()).orElse("");
            Map<String,Object> usrMap = TokenUtils.validateTokenAndGetClaims(token);
            return Optional.ofNullable(usrMap).map(m -> String.valueOf(m.get(TokenUtils.USERID))).orElse(null);
        }
        return null;
    }
}
