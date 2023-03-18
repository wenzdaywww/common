package com.www.common.utils;

import com.www.common.data.constant.CharConstant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>@Description token生成解析工具 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/16 20:43 </p>
 */
@Slf4j
public class TokenUtils {
    /** token键值 */
    public static final String TOKEN = "token";
    /** token-type键值 */
    public static final String TOKEN_TYPE = "token-type";
    /** 超时键值 */
    public static final String EXPIRE_TIME = "expire-time";
    /** 用户名键值 */
    public static final String USERID = "userId";
    /** 请求头名称 */
    public static final String AUTHORIZATION = "Authorization";
    /** 过期时间 */
    private static Integer EXPIRATION_TIME ;
    /** 私钥 */
    private static String SECRET ;
    /** token前缀 */
    private static final String TOKEN_PREFIX = "Bearer";

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * <p>@Description 设置token过期时间和密钥 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/16 20:49 </p>
     * @param expirationTime 过期时间
     * @param secret 密钥
     */
    public static void setSecretAndExpireTime(int expirationTime, String secret){
        EXPIRATION_TIME = expirationTime;
        SECRET = secret;
    }
    /**
     * <p>@Description  获取过期时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/12 15:34 </p>
     * @return java.lang.String
     */
    public static Integer getExpirationTime() {
        return EXPIRATION_TIME;
    }
    /**
     * <p>@Description 生成token </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/16 20:52 </p>
     * @param claims
     * @return java.util.Map<java.lang.String, java.lang.String>
     */
    public static Map<String, String> generateToken(Map<String, Object> claims) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR, EXPIRATION_TIME);
        Date d = c.getTime();
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(d)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        Map<String, String> map = new HashMap<String, String>();
        map.put(TOKEN,jwt);
        map.put(TOKEN_TYPE, TOKEN_PREFIX);
        map.put(EXPIRE_TIME,format.format(d));
        return map;
    }
    /**
     * <p>@Description 解析token </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/16 20:53 </p>
     * @param token 令牌
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    public static Map<String, Object> validateTokenAndGetClaims(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> body;
        try {
            body = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX + " ",""))
                    .getBody();
            body.put(AUTHORIZATION,token.replace(TOKEN_PREFIX + " ",""));
        }catch (ExpiredJwtException e){
            log.error("token过期");
            body = null;
        }catch (Exception e){
            log.error("token异常",e);
            body = null;
        }
        return body;
    }
    /**
     * <p>@Description 清除响应报文cookie中的token信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/19 22:34 </p>
     * @param response 响应报文
     * @param tokenKey token的key值
     * @return void
     */
    public static void clearResponseToken(HttpServletResponse response,String... tokenKey){
        for (String key : tokenKey){
            Cookie cookie = new Cookie(key,null);
            cookie.setPath(CharConstant.LEFT_SLASH);
            response.addCookie(cookie);
        }
    }
}
