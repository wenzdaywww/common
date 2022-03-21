package com.www.common.config.oauth2.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <p>@Description 资源服务方的Security配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:10 </p>
 */
@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable") //是否开启oauth2资源服务配置
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true) //配置基于方法的安全认证,必要
public class ResourceSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * <p>@Description 配置资源的安全拦截策略 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/18 13:04 </p>
     * @param http
     * @return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("资源服务器配置Security安全拦截策略");
        http.csrf().disable();//关闭csrf跨域检查
        //此处只配置所有请求必须认证通过，哪些请求不需要认证的需要在ResourceServieConfig.configure(HttpSecurity http)中配置
        http.authorizeRequests().antMatchers("/**").authenticated();
        http.authorizeRequests().anyRequest().permitAll(); //其他请求
    }
}
