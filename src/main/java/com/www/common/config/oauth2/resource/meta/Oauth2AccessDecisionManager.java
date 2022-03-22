package com.www.common.config.oauth2.resource.meta;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>@Description 资源服务器访问决策管理器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/24 19:22 </p>
 */
@Slf4j
public class Oauth2AccessDecisionManager implements AccessDecisionManager {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:06 </p>
     * @return
     */
    public Oauth2AccessDecisionManager(){
        log.info("资源服务器配置访问决策管理器");
    }
    /**
     * <p>@Description url访问验证 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 22:01 </p>
     * @param authentication
     * @param o
     * @param collection
     * @return void
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        if(authentication != null && authentication instanceof OAuth2Authentication){
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            Iterator<ConfigAttribute> iterator = collection.iterator();
            while (iterator.hasNext()) {
                ConfigAttribute ca = iterator.next();
                String needScope = ca.getAttribute();//当前请求需要的scope权限
                //当前角色拥有scope范围
                Collection<String> scopeList = oAuth2Authentication.getOAuth2Request().getScope();
                if(CollectionUtils.isNotEmpty(scopeList)){
                    for (String scope : scopeList){
                        if(StringUtils.equals(needScope,scope)){
                            log.debug("3、当前URL访问scope范围验证-访问范围权限验证通过");
                            return;
                        }
                    }
                }
            }
            //用户拥有的角色统一在方法上使用@PreAuthorize注解校验
            log.info("3、当前URL访问scope范围验证-无访问范围权限");
            throw new AccessDeniedException("无访问范围权限");
        }else if(authentication != null && authentication instanceof AnonymousAuthenticationToken){
            log.info("3、当前URL访问scope范围验证-匿名无访问范围权限");
            throw new AccessDeniedException("匿名无访问范围权限");
        }
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
