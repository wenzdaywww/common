package com.www.common.config.oauth2.token;

import com.www.common.config.oauth2.resource.Oauth2Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * <p>@Description token存储方式配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/18 12:21 </p>
 */
@Slf4j
@EnableConfigurationProperties(value = Oauth2Properties.class)
public class Oauth2TokenConfig {
    /** oauth2资源方认证配置属性 **/
    @Autowired
    private Oauth2Properties oauth2Properties;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:09 </p>
     * @return
     */
    public Oauth2TokenConfig(){
        log.info("注册配置token存储方式");
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
        converter.setSigningKey(oauth2Properties.getSigningKey());
        return converter;
    }
    /**
     * <p>@Description 配置token存储方式 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 12:41 </p>
     * @return org.springframework.security.oauth2.provider.token.TokenStore
     */
    @Bean
    public TokenStore tokenStore(@Qualifier("jwtTokenConverter") JwtTokenConverter jwtTokenConverter){
        //使用jwt方式存储
        return new JwtTokenStore(jwtTokenConverter);
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
