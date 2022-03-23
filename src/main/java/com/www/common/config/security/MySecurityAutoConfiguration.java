package com.www.common.config.security;

import com.www.common.config.security.handler.LoginFailureHandler;
import com.www.common.config.security.handler.LoginSuccessHandler;
import com.www.common.config.security.handler.LogoutSuccessHandlerImpl;
import com.www.common.config.security.handler.SecurityAuthRejectHandler;
import com.www.common.config.security.handler.SecurityRedisHandler;
import com.www.common.config.security.handler.SecurityUnauthHandler;
import com.www.common.config.security.handler.SessionExpiredHandler;
import com.www.common.config.security.impl.UserDetailsServiceImpl;
import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.config.security.meta.SecurityAccessDecisionManager;
import com.www.common.config.security.meta.SecurityMetadataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description Security认证自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/23 09:15 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = MySecurityProperties.class)
@ConditionalOnProperty( prefix = "com.www.common.securuty", name = "enable", havingValue = "true")
public class MySecurityAutoConfiguration {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:52 </p>
     */
    public MySecurityAutoConfiguration(){
        log.info("加载 -> Security认证自动配置类");
    }
    /**
     * <p>@Description 注册security登录认证失败处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:24 </p>
     * @return com.www.common.config.security.handler.LoginFailureHandler
     */
    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }
    /**
     * <p>@Description 注册security登录认证成功处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:38 </p>
     * @return com.www.common.config.security.handler.LoginSuccessHandler
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }
    /**
     * <p>@Description 注册退出成功的处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:38 </p>
     * @return com.www.common.config.security.handler.LogoutSuccessHandlerImpl
     */
    @Bean
    public LogoutSuccessHandlerImpl logoutSuccessHandlerImpl(){
        return new LogoutSuccessHandlerImpl();
    }
    /**
     * <p>@Description 注册认证失败时的异常处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:38 </p>
     * @return com.www.common.config.security.handler.SecurityAuthRejectHandler
     */
    @Bean
    public SecurityAuthRejectHandler securityAuthRejectHandler(){
        return new SecurityAuthRejectHandler();
    }
    /**
     * <p>@Description 注册Security的redis操作类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:55 </p>
     * @return com.www.common.config.security.handler.SecurityRedisHandler
     */
    @Bean
    public SecurityRedisHandler securityRedisHandler(){
        return new SecurityRedisHandler();
    }
    /**
     * <p>@Description 注册拒绝访问异常处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:37 </p>
     * @return com.www.common.config.security.handler.SecurityUnauthHandler
     */
    @Bean
    public SecurityUnauthHandler securityUnauthHandler(){
        return new SecurityUnauthHandler();
    }
    /**
     * <p>@Description 注册会话过期处理对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:37 </p>
     * @return com.www.common.config.security.handler.SessionExpiredHandler
     */
    @Bean
    public SessionExpiredHandler sessionExpiredHandler(){
        return new SessionExpiredHandler();
    }
    /**
     * <p>@Description 注册用户详细信息服务类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:37 </p>
     * @return com.www.common.config.security.impl.UserDetailsServiceImpl
     */
    @Bean
    public UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl();
    }
    /**
     * <p>@Description 注册token验证拦截器对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:37 </p>
     * @return com.www.common.config.security.meta.JwtAuthorizationTokenFilter
     */
    @Bean
    public JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter(){
        return new JwtAuthorizationTokenFilter();
    }
    /**
     * <p>@Description 注册访问决策管理器对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:37 </p>
     * @return com.www.common.config.security.meta.SecurityAccessDecisionManager
     */
    @Bean
    public SecurityAccessDecisionManager securityAccessDecisionManager(){
        return new SecurityAccessDecisionManager();
    }
    /**
     * <p>@Description 注册安全元数据源配置对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 09:39 </p>
     * @return com.www.common.config.security.meta.SecurityMetadataSource
     */
    @Bean
    public SecurityMetadataSource securityMetadataSource(){
        return new SecurityMetadataSource();
    }
}
