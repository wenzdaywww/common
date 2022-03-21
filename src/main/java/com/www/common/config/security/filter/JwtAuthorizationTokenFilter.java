package com.www.common.config.security.filter;

import com.www.common.config.security.handler.LoginSuccessHandler;
import com.www.common.config.security.handler.SecurityRedisHandler;
import com.www.common.config.security.impl.UserDetailsServiceImpl;
import com.www.common.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
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

/**
 * <p>@Description token验证拦截器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/16 21:06 </p>
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "com.www.common.securuty",name = "enable") //是否开启Security安全
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private SecurityRedisHandler securityRedisHandler;
    /** jwt令牌签名 */
    @Value("${com.www.common.securuty.secret-key}")
    private String SECRET_KEY ;
    /**  过期时间（秒） */
    @Value("${com.www.common.securuty.expire-time-second}")
    private int EXPIRE_TIME;
    /** 设置token过期时间和密钥 **/
    @PostConstruct
    public void setSecretAndExpireTime(){
        TokenUtils.setSecretAndExpireTime(EXPIRE_TIME,SECRET_KEY);
    }

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:12 </p>
     * @return
     */
    public JwtAuthorizationTokenFilter(){
        log.info("security配置token验证拦截器");
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
}
