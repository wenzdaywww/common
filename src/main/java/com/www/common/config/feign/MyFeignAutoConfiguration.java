package com.www.common.config.feign;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.www.common.config.filter.core.TraceIdFilter;
import com.www.common.config.oauth2.token.Oauth2TokenExtractor;
import com.www.common.pojo.constant.CharConstant;
import com.www.common.utils.HttpUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>@Description 自定义Feign配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:04 </p>
 */
@Slf4j
@Configuration
@ConditionalOnClass(RequestInterceptor.class)
public class MyFeignAutoConfiguration {
    /** feign转发的cookie的key值 **/
    private static final String COOKIE = "cookie";
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    /**
     * <p>@Description 注入RestTemplate </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:20 </p>
     * @return org.springframework.web.client.RestTemplate
     */
    @Bean
    @LoadBalanced //此注解开启负载均衡
    public RestTemplate restTemplate(){
        log.info("启动加载：自定义Rest配置类：配置RestTemplate负载均衡");
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
        log.info("启动加载：自定义Rest配置类：配置全局负载均衡策略");
        return new RandomRule();
    }
    /**
     * <p>@Description 注册自定义hystrix策略 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 22:08 </p>
     * @return com.www.common.config.feign.HystrixStrategyHandler
     */
    @Bean
    public HystrixStrategyHandler hystrixStrategyHandler(){
        return new HystrixStrategyHandler();
    }
    /**
     * <p>@Description 重新配置feign请求头，解决feign调用请求头丢失问题 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/20 22:16 </p>
     * @return feign.RequestInterceptor
     */
    @Bean
    public RequestInterceptor requestInterceptor(){
        log.info("启动加载：自定义Feign配置类：配置Feign请求头转发");
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 开启hystrix后RequestContextHolder.getRequestAttributes()=null,需要自定义hystrix策略
                HttpServletRequest request = HttpUtils.getRequest();
                if(request == null){
                    log.error("请求HttpServletRequest为空");
                    return;
                }
                //转发日志全局跟踪号
                requestTemplate.header(TraceIdFilter.TRACE_ID, HttpUtils.getTraceId());
                Cookie[] cookies = request.getCookies();
                if(cookies != null){
                    for (int i = 0; i < cookies.length; i++){
                        if (StringUtils.equals(Oauth2TokenExtractor.COOKIES_ACCESS_TOKEN,cookies[i].getName()) && StringUtils.isNotBlank(cookies[i].getValue())){
                            //转发token
                            requestTemplate.header(COOKIE, cookies[i].getName() + CharConstant.EQUAL + cookies[i].getValue());
                            break;
                        }
                    }
                }
            }
        };
    }
}
