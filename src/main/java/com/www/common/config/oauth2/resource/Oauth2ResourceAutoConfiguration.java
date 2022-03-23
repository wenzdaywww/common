package com.www.common.config.oauth2.resource;

import com.www.common.config.oauth2.resource.handler.Oauth2AuthRejectHandler;
import com.www.common.config.oauth2.resource.handler.Oauth2UnauthHandler;
import com.www.common.config.oauth2.resource.meta.Oauth2AccessDecisionManager;
import com.www.common.config.oauth2.resource.meta.Oauth2MetadataSource;
import com.www.common.config.oauth2.token.Oauth2TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description oauth2资源服务自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 22:30 </p>
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable") //是否开启oauth2资源服务配置
public class Oauth2ResourceAutoConfiguration extends Oauth2TokenConfig {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:47 </p>
     */
    public Oauth2ResourceAutoConfiguration(){
        log.info("启动加载：Oauth2资源服务方自动配置类");
    }
    /**
     * <p>@Description 注册资源服务器访问决策管理器对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:29 </p>
     * @return com.www.common.config.oauth2.httpsecurity.Oauth2AccessDecisionManager
     */
    @Bean
    public Oauth2AccessDecisionManager oauth2AccessDecisionManager(){
        return new Oauth2AccessDecisionManager();
    }
    /**
     * <p>@Description 注册资源服务器安全元数据源配置对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:43 </p>
     * @return com.www.common.config.oauth2.resource.meta.Oauth2MetadataSource
     */
    @Bean
    public Oauth2MetadataSource oauth2MetadataSource(){
        return new Oauth2MetadataSource();
    }
    /**
     * <p>@Description 资源服务器认证失败时的异常处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:46 </p>
     * @return com.www.common.config.oauth2.resource.handler.Oauth2AuthRejectHandler
     */
    @Bean
    public Oauth2AuthRejectHandler Oauth2AuthRejectHandler(){
        return new Oauth2AuthRejectHandler();
    }
    /**
     * <p>@Description 拒绝访问异常处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:46 </p>
     * @return com.www.common.config.oauth2.resource.handler.Oauth2AuthRejectHandler
     */
    @Bean
    public Oauth2UnauthHandler oauth2UnauthHandler(){
        return new Oauth2UnauthHandler();
    }
}
