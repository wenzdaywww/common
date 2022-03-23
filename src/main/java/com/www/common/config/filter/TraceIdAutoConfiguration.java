package com.www.common.config.filter;

import com.www.common.config.filter.core.TraceIdFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description 配置日志全局跟踪号过滤器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:10 </p>
 */
@Slf4j
@Configuration
public class TraceIdAutoConfiguration {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:44 </p>
     */
    public TraceIdAutoConfiguration(){
        log.info("加载 -> 日志全局跟踪号配置");
    }
    /**
     * <p>@Description 配置日志全局跟踪号过滤器 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:11 </p>
     * @return com.www.common.config.filter.TraceIdFilter
     */
    @Bean
    public TraceIdFilter traceIdFilter(){
        return new TraceIdFilter();
    }
}
