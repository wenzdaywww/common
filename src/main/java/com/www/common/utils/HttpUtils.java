package com.www.common.utils;

import com.www.common.config.filter.TraceIdFilter;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.UNKNOWN;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * <p>@Description http工具类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/7 23:00 </p>
 */
public class HttpUtils {
    private static final String IP_UTILS_FLAG = ",";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IP1 = "127.0.0.1";
    /**
     * <p>@Description 获取当前http请求 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/7 23:00 </p>
     * @return javax.servlet.http.HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes == null){
            return null;
        }
        return attributes.getRequest();
    }
    /**
     * <p>@Description 获取http请求头的全局跟踪号 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/7 23:03 </p>
     * @return java.lang.String 全局跟踪号
     */
    public static String getTraceId(){
        HttpServletRequest request = getRequest();
        if (request == null){
            return null;
        }
        return request.getHeader(TraceIdFilter.TRACE_ID);
    }
    /**
     * <p>@Description 获取请求客户端的ip地址 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/8 20:42 </p>
     * @return java.lang.String
     */
    public static String getIp(){
        HttpServletRequest request = getRequest();
        if (request == null){
            return null;
        }
        String ip = null;
        //以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
        ip = request.getHeader("X-Original-Forwarded-For");
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        //获取nginx等代理的ip
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        //使用代理，则获取第一个IP地址
        if (!StringUtils.isEmpty(ip) && ip.indexOf(IP_UTILS_FLAG) > 0) {
            ip = ip.substring(0, ip.indexOf(IP_UTILS_FLAG));
        }
        return ip;
    }
}
