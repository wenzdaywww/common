package com.www.common.config.oauth2.core;

import com.www.common.config.oauth2.httpsecurity.Oauth2AccessDecisionManager;
import com.www.common.config.oauth2.httpsecurity.Oauth2MetadataSource;
import com.www.common.config.oauth2.resourcesecurity.Oauth2AuthRejectHandler;
import com.www.common.config.oauth2.token.Oauth2TokenExtractor;
import com.www.common.config.oauth2.resourcesecurity.Oauth2UnauthHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
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
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable") //是否开启oauth2资源服务配置
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    /** 资源服务id **/
    @Value("${spring.application.name}")
    private String resourceId;
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
        log.info("资源服务器配置验证方式");
        resources.resourceId(resourceId) //资源ID
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
        log.info("资源服务器配置安全拦截策略");
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
    /**
     * <p>@Description 远程校验token </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/19 12:28 </p>
     * @return org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
     */
    @Deprecated
    public ResourceServerTokenServices tokenServices(){
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://localhost:8003/oauth/check_token");
        services.setClientId("blog");
        services.setClientSecret("wenzday");
        return services;
    }
}
