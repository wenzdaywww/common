package com.www.common.config.request;

import com.www.common.config.request.core.RequestPrintConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description 请求响应报文打印自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 18:59 </p>
 */
@Configuration
@EnableConfigurationProperties(value = RequestPrintProperties.class)
@ConditionalOnProperty( prefix = "com.www.common.request", name = "enable", havingValue = "true")
public class RequestPrintAutoConfiguration {
    /**
     * <p>@Description 注册请求响应报文打印配置对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 19:54 </p>
     * @return RequestPrintConfig
     */
    @Bean
    public RequestPrintConfig requestAopConfig(){
        return new RequestPrintConfig();
    }
}
