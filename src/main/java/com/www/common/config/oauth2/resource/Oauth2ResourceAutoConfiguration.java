package com.www.common.config.oauth2.resource;

import com.www.common.config.oauth2.resource.handler.Oauth2AuthRejectHandler;
import com.www.common.config.oauth2.resource.handler.Oauth2UnauthHandler;
import com.www.common.config.oauth2.resource.meta.Oauth2AccessDecisionManager;
import com.www.common.config.oauth2.resource.meta.Oauth2MetadataSource;
import com.www.common.config.oauth2.token.JwtTokenConverter;
import com.www.common.config.oauth2.token.Oauth2TokenExtractor;
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
 * <p>@Description oauth2资源服务自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 22:30 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = Oauth2Properties.class)
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable", havingValue = "true") //是否开启oauth2资源服务配置
public class Oauth2ResourceAutoConfiguration{
    @Autowired
    private Oauth2Properties oauth2Properties;

    /**
     * <p>@Description 注册jwt对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 12:42 </p>
     * @return
     */
    @Bean
    public JwtTokenConverter jwtTokenConverter(){
        JwtTokenConverter converter = new JwtTokenConverter(oauth2Properties.getTokenKeyPrefix());
        converter.setSigningKey(oauth2Properties.getSigningKey());
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
        return new Oauth2TokenExtractor(oauth2Properties.getTokenKeyPrefix(),oauth2Properties.getUrlPath());
    }
    /**
     * <p>@Description 注册资源服务器访问决策管理器对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:29 </p>
     * @return
     */
    @Bean
    public Oauth2AccessDecisionManager oauth2AccessDecisionManager(){
        return new Oauth2AccessDecisionManager();
    }
    /**
     * <p>@Description 注册资源服务器安全元数据源配置对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:43 </p>
     * @return
     */
    @Bean
    public Oauth2MetadataSource oauth2MetadataSource(){
        return new Oauth2MetadataSource();
    }
    /**
     * <p>@Description 资源服务器认证失败时的异常处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:46 </p>
     * @return
     */
    @Bean
    public Oauth2AuthRejectHandler Oauth2AuthRejectHandler(){
        return new Oauth2AuthRejectHandler();
    }
    /**
     * <p>@Description 拒绝访问异常处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:46 </p>
     * @return
     */
    @Bean
    public Oauth2UnauthHandler oauth2UnauthHandler(){
        return new Oauth2UnauthHandler();
    }
}
