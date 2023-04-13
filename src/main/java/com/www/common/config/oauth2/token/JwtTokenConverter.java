package com.www.common.config.oauth2.token;

import com.alibaba.fastjson.JSON;
import com.www.common.config.oauth2.dto.TokenInfoDTO;
import com.www.common.config.oauth2.util.RedisTokenHandler;
import com.www.common.data.constant.CharConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>@Description 自定义jwt的token转换器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/24 21:00 </p>
 */
@Slf4j
public class JwtTokenConverter extends JwtAccessTokenConverter {
    /** 令牌前缀 **/
    public static String TOKEN_PREFIX = "Bearer ";
    @Autowired
    private Oauth2TokenExtractor oauth2TokenExtractor;
    /** 用户登录的token保存到redis中的key的前缀 **/
    private String redisKeyPrefix;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:50 </p>
     * @param redisKeyPrefix 用户登录的token保存到redis中的key的前缀
     */
    public JwtTokenConverter(String redisKeyPrefix){
        log.info("启动加载>>>自定义jwt的token转换器配置");
        this.redisKeyPrefix = redisKeyPrefix;
    }
    /**
     * <p>@Description token解析 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/24 21:02 </p>
     * @param token 令牌
     * @return token信息
     */
    public TokenInfoDTO decodeToken(String token){
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> map = super.decode(token.replace(TOKEN_PREFIX, CharConstant.EMPTY));
        TokenInfoDTO tokenInfoDTO = map != null ? JSON.parseObject(JSON.toJSONString(map),TokenInfoDTO.class) : null;
        return tokenInfoDTO;
    }
    /**
     * <p>@Description token解析 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/24 21:02 </p>
     * @param request 请求信息
     * @return token信息
     */
    public TokenInfoDTO decodeToken(HttpServletRequest request){
        if(request == null){
            return null;
        }
        String token = oauth2TokenExtractor.getToken(request);
        return decodeToken(token);
    }
    /**
     * <p>@Description 从当前请求头中获取当前登录的用户ID </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/22 16:48 </p>
     * @param request 请求信息
     * @return java.lang.String 用户ID
     */
    public String getUserId(HttpServletRequest request){
        String token = oauth2TokenExtractor.getToken(request);
        TokenInfoDTO tokenDTO = this.decodeToken(token);
        //token无效则返回空
        if(RedisTokenHandler.isInvalidToken(tokenDTO,token,redisKeyPrefix)){
            return null;
        }
        return tokenDTO != null ? tokenDTO.getUser_name() : null;
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
            return this.getUserId(request);
        }
        return null;
    }
}
