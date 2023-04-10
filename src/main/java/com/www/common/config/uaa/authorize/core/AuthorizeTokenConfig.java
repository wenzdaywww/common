package com.www.common.config.uaa.authorize.core;

import com.www.common.config.oauth2.resource.Oauth2Properties;
import com.www.common.config.oauth2.token.Oauth2TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description 认证服务方token存储方式配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/18 12:21 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = Oauth2Properties.class)
@ConditionalOnProperty( prefix = "com.www.common.uaa", name = "enable", havingValue = "true")
public class AuthorizeTokenConfig extends Oauth2TokenConfig {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/10 22:18 </p>
     */
    public AuthorizeTokenConfig(){
        log.info("启动加载：单点登录认证服务方自动配置--token存储方式配置");
    }
}
