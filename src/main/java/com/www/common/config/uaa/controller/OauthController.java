package com.www.common.config.uaa.controller;

import com.alibaba.fastjson.JSONObject;
import com.www.common.config.oauth2.dto.TokenDTO;
import com.www.common.config.oauth2.dto.TokenInfoDTO;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.config.oauth2.util.RedisTokenHandler;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>@Description 重写oauth请求 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/19 23:36 </p>
 */
@Slf4j
@RestController
public class OauthController {
    /** 保存到cookie的access_token的key **/
    public static final String COOKIES_ACCESS_TOKEN = "access_token";
    /** 保存到cookie的refresh_token的key **/
    public static final String COOKIES_REFRESH_TOKEN = "refresh_token";
    /** 保存到cookie的user的key **/
    public static final String COOKIES_USER = "user";
    /** 保存到cookie的user的ID的key **/
    public static final String COOKIES_USER_ID = "userId";
    /** 保存到cookie的user的角色的key **/
    public static final String COOKIES_USER_ROLES = "roles";
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private JwtTokenConverter jwtTokenConverter;

    /**
     * <p>@Description 重写oauth/token的get请求 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 23:44 </p>
     * @param principal
     * @param parameters
     * @return com.www.authorise.data.dto.Response<java.util.Map>
     */
    @GetMapping("oauth/token")
    public Result<TokenDTO> tokenGet(HttpServletResponse response, Principal principal, @RequestParam Map<String, String> parameters) throws Exception {
        return tokenPost(response,principal,parameters);
    }
    /**
     * <p>@Description 重写oauth/token的post请求 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 23:44 </p>
     * @param principal
     * @param parameters
     * @return com.www.authorise.data.dto.Response<java.util.Map>
     */
    @PostMapping("oauth/token")
    public Result<TokenDTO> tokenPost(HttpServletResponse response, Principal principal, @RequestParam Map<String, String> parameters) throws Exception  {
        Result<TokenDTO> result = new Result<>();
        ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.postAccessToken(principal, parameters);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(accessToken.getBody().getValue());
        tokenDTO.setRefreshToken(accessToken.getBody().getRefreshToken() != null ?
                accessToken.getBody().getRefreshToken().getValue() : null);
        tokenDTO.setTokenType(accessToken.getBody().getTokenType());
        tokenDTO.setExpiresSeconds(accessToken.getBody().getExpiresIn());
        tokenDTO.setScope(accessToken.getBody().getScope());
        //解析token信息
        TokenInfoDTO tokenInfoDTO = jwtTokenConverter.decodeToken(tokenDTO.getAccessToken());
        tokenDTO.setUserId(tokenInfoDTO != null ? tokenInfoDTO.getUser_name() : null);
        //将token保存到cookie中
        Cookie tokenCookie = new Cookie(COOKIES_ACCESS_TOKEN,tokenDTO.getAccessToken());
        tokenCookie.setMaxAge(tokenDTO.getExpiresSeconds());
        tokenCookie.setPath(CharConstant.LEFT_SLASH);
        response.addCookie(tokenCookie);
        // 有刷新令牌则也保存到cookie中
        if(tokenDTO.getRefreshToken() != null){
            Cookie refreshCookie = new Cookie(COOKIES_REFRESH_TOKEN,tokenDTO.getRefreshToken());
            refreshCookie.setPath(CharConstant.LEFT_SLASH);
            response.addCookie(refreshCookie);
        }
        //将用户ID和角色信息保存到cookies中
        if(tokenInfoDTO != null){
            Map<String,Object> userMap = new HashMap<>();
            userMap.put(COOKIES_USER_ID,tokenInfoDTO.getUser_name());
            userMap.put(COOKIES_USER_ROLES,tokenInfoDTO.getAuthorities());
            Cookie userCookie = new Cookie(COOKIES_USER, URLEncoder.encode(JSONObject.toJSONString(userMap),"UTF-8"));
            userCookie.setMaxAge(tokenDTO.getExpiresSeconds());
            userCookie.setPath(CharConstant.LEFT_SLASH);
            response.addCookie(userCookie);
        }
        result.setData(tokenDTO);
        //保存用户登录的token到redis中
        RedisTokenHandler.setUserIdToken(tokenInfoDTO,tokenDTO.getAccessToken(),tokenDTO.getExpiresSeconds());
        return result;
    }
}
