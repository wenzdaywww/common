package com.www.common.config.uaa.authorize.core;

import com.www.common.config.uaa.UaaAutoConfiguration;
import com.www.common.config.uaa.authorize.handler.Oauth2LoginFailureHandler;
import com.www.common.config.uaa.authorize.handler.Oauth2LogoutSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>@Description 认证授权服务提供方的Security配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:10 </p>
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true) //配置基于方法的安全认证,必要
@ConditionalOnProperty( prefix = "com.www.common.uaa", name = "enable", havingValue = "true")
public class AuthorizeSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private Oauth2LogoutSuccessHandler oauth2LogoutSuccessHandler;
    @Autowired
    private Oauth2LoginFailureHandler oauth2LoginFailureHandler;

    /**
     * <p>@Description 配置密码加密方式 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/18 12:51 </p>
     * @return org.springframework.security.crypto.password.PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("启动加载：单点登录认证服务方自动配置--配置密码加密方式");
        return new BCryptPasswordEncoder();
    }
    /**
     * <p>@Description 配置认证管理器，使用密码模式必要 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/18 12:54 </p>
     * @return org.springframework.security.authentication.AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        log.info("启动加载：单点登录认证服务方自动配置--配置认证管理器");
        return super.authenticationManager();
    }
    /**
     * <p>@Description 配置用户的安全拦截策略 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/18 13:04 </p>
     * @param http
     * @return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(UaaAutoConfiguration.LOGIN_PAGE) //自定义的登录页面 **重要**
                .loginProcessingUrl("/login")  //原始的处理登录的URL,保持和uaa_login.html的form表单的action一致 ，不要修改
                .failureHandler(oauth2LoginFailureHandler)//登录失败处理逻辑
                .permitAll() //放开
                .and()
                .authorizeRequests().anyRequest().authenticated()//其他请求必须登录
                .and()
                .csrf().disable();//关闭csrf
        http.logout()//退出
                .logoutUrl("/logout")//退出路径
                .logoutSuccessHandler(oauth2LogoutSuccessHandler)//退出成功处理逻辑
                .deleteCookies("JSESSIONID");//登出之后删除cookie
    }
    /**
     * <p>@Description 忽略静态资源的拦截配置 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/10 21:43 </p>
     * @param web
     * @return
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略静态资源的拦截
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
    }
}
