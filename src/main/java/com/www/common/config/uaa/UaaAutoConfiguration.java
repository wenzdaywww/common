package com.www.common.config.uaa;

import com.www.common.config.uaa.authorize.handler.Oauth2LoginFailureHandler;
import com.www.common.config.uaa.authorize.handler.Oauth2LogoutSuccessHandler;
import com.www.common.config.uaa.authorize.handler.UserServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>@Description 单点登录认证服务方自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/23 09:15 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = UaaProperties.class)
@ConditionalOnProperty( prefix = "com.www.common.uaa", name = "enable", havingValue = "true")
public class UaaAutoConfiguration implements WebMvcConfigurer {
    /** 登录页面请求路径 **/
    public static final String LOGIN_PAGE = "/uaa-login";
    /**
     * <p>@Description 设置视图控制器 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:00 </p>
     * @param registry 视图控制器
     * @return void
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        log.info("启动加载：单点登录认证服务方自动配置--加载视图控制器");
        registry.addViewController("/uaa-login").setViewName("uaa_login");//自定义登录页面跳转
    }
    /**
     * <p>@Description 注册oauth2登录认证失败处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/10 21:50 </p>
     * @return
     */
    @Bean
    public Oauth2LoginFailureHandler oauth2LoginFailureHandler(){
        log.info("启动加载：单点登录认证服务方自动配置--开启oauth2登录认证失败处理配置");
        return new Oauth2LoginFailureHandler();
    }
    /**
     * <p>@Description 注册退出成功的处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/10 21:50 </p>
     * @return
     */
    @Bean
    public Oauth2LogoutSuccessHandler oauth2LogoutSuccessHandler(){
        log.info("启动加载：单点登录认证服务方自动配置--开启oauth2退出成功的处理配置");
        return new Oauth2LogoutSuccessHandler();
    }
    /**
     * <p>@Description 注册oauth2用户详细信息服务类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/10 21:50 </p>
     * @return
     */
    @Bean
    public UserServiceHandler userServiceHandler(){
        log.info("启动加载：单点登录认证服务方自动配置--开启oauth2用户详细信息服务配置");
        return new UserServiceHandler();
    }
}
