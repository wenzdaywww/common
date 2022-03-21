package com.www.common.config.oauth2.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * <p>@Description token存储方式配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/18 12:21 </p>
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable") //是否开启oauth2资源服务配置
public class Oauth2TokenConfig {
    /** jwt令牌签名 **/
    @Value("${com.www.common.oauth2.signing-key}")
    private String signingKey;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:09 </p>
     * @return
     */
    public Oauth2TokenConfig(){
        log.info("配置token存储方式");
    }
    /**
     * <p>@Description 配置token存储方式 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 12:41 </p>
     * @return org.springframework.security.oauth2.provider.token.TokenStore
     */
    @Bean
    public TokenStore tokenStore(){
        //使用jwt方式存储
        return new JwtTokenStore(jwtTokenConverter());
    }
    /**
     * <p>@Description 注册jwt对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 12:42 </p>
     * @return org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     */
    @Bean
    public JwtTokenConverter jwtTokenConverter(){
        JwtTokenConverter converter = new JwtTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }
    /**
     * <p>@Description 自定义token获取器 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 23:39 </p>
     * @return com.www.common.config.oauth2.resuorce.resourcesecurity.Oauth2Extractor
     */
    @Bean
    public Oauth2TokenExtractor oauth2TokenExtractor(){
        return new Oauth2TokenExtractor();
    }
}
