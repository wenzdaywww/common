package com.www.common.config.aop;

import com.www.common.config.aop.core.RequestAopConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description 请求响应报文AOP拦截自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 18:59 </p>
 */
@Configuration
@EnableConfigurationProperties(value = RequestAopProperties.class)
@ConditionalOnProperty( prefix = "com.www.common.request", name = "enable", havingValue = "true")
public class RequestAopAutoConfiguration {
    @Autowired
    private RequestAopProperties requestAopProperties;
    /**
     * <p>@Description 注册请求响应报文AOP拦截配置对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 19:54 </p>
     * @return com.www.common.config.aop.core.RequestAop
     */
    @Bean
    public RequestAopConfig requestAopConfig(){
        return new RequestAopConfig(requestAopProperties);
    }
}
