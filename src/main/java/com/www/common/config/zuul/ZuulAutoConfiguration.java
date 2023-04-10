package com.www.common.config.zuul;

import com.netflix.zuul.ZuulFilter;
import com.www.common.config.zuul.filter.PostFilter;
import com.www.common.config.zuul.filter.PreFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description zuul自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/29 21:33 </p>
 */
@Slf4j
@Configuration
@ConditionalOnClass(value = ZuulFilter.class)
public class ZuulAutoConfiguration {
    /**
     * <p>@Description 添加pre过滤器 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/29 21:40 </p>
     * @return com.www.myblog.zuul.config.PreFilter
     */
    @Bean
    public PreFilter preFilter(){
        log.info("启动加载：zuul自动配置pre过滤器");
        return new PreFilter();
    }
    /**
     * <p>@Description 添加post过滤器 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/29 21:40 </p>
     * @return com.www.myblog.zuul.config.PostFilter
     */
    @Bean
    public PostFilter postFilter(){
        log.info("启动加载：zuul自动配置post过滤器");
        return new PostFilter();
    }
}
