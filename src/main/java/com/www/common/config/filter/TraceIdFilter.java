package com.www.common.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description 配置日志全局跟踪号过滤器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/31 20:26 </p>
 */
@Slf4j
@Order(-1000)
@Component
public class TraceIdFilter extends OncePerRequestFilter {
    /** 日志跟踪号的key **/
    public static final String TRACE_ID = "traceId";

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:04 </p>
     * @return
     */
    public TraceIdFilter(){
        log.info("开启日志全局跟踪号过滤器");
    }
    /**
     * <p>@Description 过滤器设置 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/31 21:52 </p>
     * @param httpServletRequest 请求参数
     * @param httpServletResponse 响应参数
     * @param filterChain 过滤器
     * @return void
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        MDC.put(TRACE_ID, httpServletRequest.getHeader(TRACE_ID));
        filterChain.doFilter(httpServletRequest,httpServletResponse);
        MDC.remove(TRACE_ID);
    }
}
