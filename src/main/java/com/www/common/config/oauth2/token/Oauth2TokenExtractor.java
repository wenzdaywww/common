package com.www.common.config.oauth2.token;

import com.www.common.config.oauth2.dto.TokenInfoDTO;
import com.www.common.config.oauth2.util.RedisTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>@Description 自定义token提取器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/26 16:14 </p>
 */
@Slf4j
public class Oauth2TokenExtractor extends BearerTokenExtractor {
    /** 保存到cookie的access_token的key **/
    public static final String COOKIES_ACCESS_TOKEN = "access_token";
    @Autowired
    private JwtTokenConverter jwtTokenConverter;
    /** 路径匹配器 **/
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    /** 用户登录的token保存到redis中的key的前缀 **/
    private String redisKeyPrefix;
    /** 文件资源路径 **/
    private String urlPath;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 22:39 </p>
     * @param redisKeyPrefix 用户登录的token保存到redis中的key的前缀
     * @param urlPath 文件资源路径
     */
    public Oauth2TokenExtractor(String redisKeyPrefix,String urlPath){
        log.info("启动加载>>>自定义token提取器配置");
        this.redisKeyPrefix = redisKeyPrefix;
        this.urlPath = urlPath;
    }
    /**
     * <p>@Description 设置token获取方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/26 16:32 </p>
     * @param request 请求
     * @return
     */
    @Override
    public Authentication extract(HttpServletRequest request) {
        String uri = request.getRequestURI(); //当前uri
        // 判断是否是图片资源，是则不获取token
        if(StringUtils.isNotBlank(urlPath) && antPathMatcher.match(urlPath,uri)){
            log.debug("当前请求{} 为图片资源，不获取token",uri);
            return null;
        }
        String tokenValue = this.getToken(request); //获取token
        if(StringUtils.isBlank(tokenValue)){
            log.info("1、获取请求{} 中的token单点登录验证不通过，请求中的token不存在",uri);
            return null;
        }
        TokenInfoDTO tokenInfoDTO = jwtTokenConverter.decodeToken(tokenValue);
        if(RedisTokenHandler.isInvalidToken(tokenInfoDTO,tokenValue,redisKeyPrefix)){
            log.info("1、获取请求{} 中的token单点登录验证不通过，请求中的token已失效",uri);
            return null;
        }
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
        log.debug("1、获取请求{} 中的token单点登录验证通过",uri);
        return authentication;
    }
    /**
     * <p>@Description 获取token </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 21:17 </p>
     * @param request 请求
     * @return java.lang.String
     */
    public String getToken(HttpServletRequest request){
        //token获取优先级
        //1、从请求头header中获取Authorization参数值，参数值必须是【Bearer 】开头
        //2、从请求参数中获取access_token的参数值
        String tokenValue = super.extractToken(request);
        //3、从cookie中获取access_token
        if(StringUtils.isBlank(tokenValue)){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (int i = 0; i < cookies.length; i++){
                    if (StringUtils.equals(COOKIES_ACCESS_TOKEN,cookies[i].getName()) && StringUtils.isNotBlank(cookies[i].getValue())){
                        return cookies[i].getValue();
                    }
                }
            }
        }
        return null;
    }
}
