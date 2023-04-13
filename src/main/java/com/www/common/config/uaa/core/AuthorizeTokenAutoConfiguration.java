package com.www.common.config.uaa.core;

import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.config.oauth2.token.Oauth2TokenExtractor;
import com.www.common.config.uaa.UaaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * <p>@Description 认证服务方token存储方式配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/18 12:21 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = UaaProperties.class)
@ConditionalOnProperty( prefix = "com.www.common.uaa", name = "enable", havingValue = "true")
public class AuthorizeTokenAutoConfiguration {
    @Autowired
    private UaaProperties uaaProperties;

    /**
     * <p>@Description 注册jwt对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 12:42 </p>
     * @return
     */
    @Bean
    public JwtTokenConverter jwtTokenConverter(){
        JwtTokenConverter converter = new JwtTokenConverter(uaaProperties.getTokenKeyPrefix());
        converter.setSigningKey(uaaProperties.getSigningKey());
        return converter;
    }
    /**
     * <p>@Description 配置token存储方式 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 12:41 </p>
     * @return
     */
    @Bean
    public TokenStore tokenStore(@Qualifier("jwtTokenConverter") JwtTokenConverter jwtTokenConverter){
        log.info("启动加载>>>配置token存储方式");
        //使用jwt方式存储
        return new JwtTokenStore(jwtTokenConverter);
    }
    /**
     * <p>@Description 自定义token获取器 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 23:39 </p>
     * @return
     */
    @Bean
    public Oauth2TokenExtractor oauth2TokenExtractor(){
        return new Oauth2TokenExtractor(uaaProperties.getTokenKeyPrefix(),uaaProperties.getUrlPath());
    }
}
