package com.www.common.config.oauth2.resource;

import com.www.common.config.oauth2.resource.handler.Oauth2AuthRejectHandler;
import com.www.common.config.oauth2.resource.handler.Oauth2UnauthHandler;
import com.www.common.config.oauth2.resource.meta.Oauth2AccessDecisionManager;
import com.www.common.config.oauth2.resource.meta.Oauth2MetadataSource;
import com.www.common.config.oauth2.token.Oauth2TokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * <p>@Description 资源服务方的认证配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/18 21:24 </p>
 */
@Slf4j
@Configuration
@EnableResourceServer
@EnableConfigurationProperties(value = Oauth2Properties.class)
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable", havingValue = "true") //是否开启oauth2资源服务配置
public class Oauth2ResourceServerAutoConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private Oauth2Properties oauth2Properties;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private Oauth2AuthRejectHandler oauth2AuthRejectHandler;
    @Autowired
    private Oauth2UnauthHandler oauth2UnauthHandler;
    @Autowired
    private Oauth2MetadataSource oauth2MetadataSource;
    @Autowired
    private Oauth2AccessDecisionManager oauth2AccessDecisionManager;
    @Autowired
    private Oauth2TokenExtractor oauth2TokenExtractor;

    /**
     * <p>@Description 配置资源服务方验证方式 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 16:25 </p>
     * @param resources
     * @return void
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        log.info("启动加载>>>Oauth2资源服务方的认证配置>>>资源服务方配置验证方式");
        resources.resourceId(oauth2Properties.getResourceId()) //资源ID
                // .tokenServices(tokenServices()) //远程校验token时需要
                .tokenStore(tokenStore) //jwt校验token
                .stateless(true);
        //先认证失败在拒绝方法
        resources.authenticationEntryPoint(oauth2AuthRejectHandler);//认证失败时的异常处理
        resources.accessDeniedHandler(oauth2UnauthHandler);//拒绝访问异常处理
        resources.tokenExtractor(oauth2TokenExtractor);//自定义token获取器
    }
    /**
     * <p>@Description 配置用户的安全拦截策略 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/18 13:04 </p>
     * @param http
     * @return void
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("启动加载>>>Oauth2资源服务方的认证配置>>>资源服务器配置安全拦截策略");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//关闭session策略
        //固定写法的配置scope范围
//        http.authorizeRequests()
//            .antMatchers("/**").access("#oauth2.hasAnyScope('base:read','base:read')");
        //动态配置权限范围
        http.authorizeRequests().anyRequest().authenticated()
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
               @Override
               public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                  o.setSecurityMetadataSource(oauth2MetadataSource);//访问权限配置
                  o.setAccessDecisionManager(oauth2AccessDecisionManager);//访问权限验证
                  return o;
               }
            });
    }
}
