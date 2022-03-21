package com.www.common.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <p>@Description Restful API调用配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:20 </p>
 */
@Slf4j
@Configuration
@ConditionalOnClass(RestTemplate.class)
public class RestConfig {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * <p>@Description 注入RestTemplate </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:20 </p>
     * @return org.springframework.web.client.RestTemplate
     */
    @Bean
    @LoadBalanced //此注解开启负载均衡
    public RestTemplate restTemplate(){
        log.info("配置RestTemplate负载均衡");
        // 创建 RestTemplate 实例， 我这里使用的OkHttp
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate;
    }
    /**
     * <p>@Description 添加全局负载均衡策略 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/7 15:04 </p>
     * @return com.netflix.loadbalancer.IRule
     */
    @Bean
    public IRule getRule(){
        log.info("配置全局负载均衡策略");
        return new RandomRule();
    }
}
