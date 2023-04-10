package com.www.common.config.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.www.common.config.filter.core.TraceIdFilter;
import com.www.common.utils.UidGeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>@Description pre过滤器:请求调用前的处理  </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/29 21:28 </p>
 */
@Slf4j
public class PreFilter extends ZuulFilter {
    /**
     * <p>@Description 设置过滤器类型
     *   pre：可以在请求被路由之前调用
     *   route：在路由请求时候被调用
     *   post：在route和error过滤器之后被调用
     *   error：处理请求时发生错误时被调用
     * </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/29 21:35 </p>
     * @return java.lang.String
     */
    @Override
    public String filterType() {
        return "pre";
    }
    /**
     * <p>@Description 设置过滤器优先级，数字越小优先级越大 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/29 21:35 </p>
     * @return int
     */
    @Override
    public int filterOrder() {
        return 0;
    }
    /**
     * <p>@Description 设置是否开启过滤器 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/29 21:35 </p>
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String traceId = UidGeneratorUtils.getTraceId();
        //生成全局日志跟踪号
        ctx.addZuulRequestHeader(TraceIdFilter.TRACE_ID, traceId);
        MDC.put(TraceIdFilter.TRACE_ID, traceId);
//        只过滤OPTIONS 请求
        if(request.getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        return false;
    }
    /**
     * <p>@Description 设置过滤器规则 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/29 21:36 </p>
     * @return java.lang.Object
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        HttpServletRequest request = ctx.getRequest();
//        log.info("---> pre的cookies={}", JSON.toJSONString(request.getCookies()));
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));//根据该字段判断是否允许该请求访问。
        response.setHeader("Access-Control-Allow-Credentials","true");//用户是否可以发送、处理 cookie；
        response.setHeader("Access-Control-Allow-Headers","authorization, content-type");//服务器允许使用的字段
        response.setHeader("Access-Control-Allow-Methods","POST,GET");//真实请求允许的方法
        //可以让用户拿到的字段。有几个字段无论设置与否都可以拿到的，包括：Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma 。
        response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host");
        //服务器收到请求时，需要分别对 Origin、Access-Control-Request-Method、Access-Control-Request-Headers 进行验证
        response.setHeader("Vary","Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
        //不再路由
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(200);
        return null;
    }
}
