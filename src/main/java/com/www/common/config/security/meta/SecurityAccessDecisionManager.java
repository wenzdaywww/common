package com.www.common.config.security.meta;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>@Description 访问决策管理器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/24 19:22 </p>
 */
@Slf4j
public class SecurityAccessDecisionManager implements AccessDecisionManager {

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:13 </p>
     * @return
     */
    public SecurityAccessDecisionManager(){
        log.info("启动加载：Security认证自动配置类：注册security配置访问决策管理器");
    }
    /**
     * <p>@Description url访问校验 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 22:01 </p>
     * @param authentication
     * @param o
     * @param collection
     * @return void
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        log.info("4、访问权限角色验证");
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute ca = iterator.next();
            String needRole = ca.getAttribute();//当前请求需要的权限
            //当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    log.info("4.1、访问权限角色验证-权限通过");
                    return;
                }
            }
        }
        log.info("4.1、访问权限角色验证-无权限访问");
        throw new AccessDeniedException("无权限访问");
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
