package com.www.common.config.feign;

import com.www.common.config.filter.TraceIdFilter;
import com.www.common.config.oauth2.token.Oauth2TokenExtractor;
import com.www.common.pojo.constant.CharConstant;
import com.www.common.utils.HttpUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>@Description feign配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/20 22:00 </p>
 */
@Slf4j
@Configuration
@ConditionalOnClass(RequestTemplate.class)
public class FeignConfig{
    /** feign转发的cookie的key值 **/
    private static final String COOKIE = "cookie";
    /**
     * <p>@Description 重新配置feign请求头，解决feign调用请求头丢失问题 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/20 22:16 </p>
     * @return feign.RequestInterceptor
     */
    @Bean
    public RequestInterceptor requestInterceptor(){
        log.info("配置Feign请求头转发");
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
