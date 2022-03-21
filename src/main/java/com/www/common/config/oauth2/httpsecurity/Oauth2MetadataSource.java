package com.www.common.config.oauth2.httpsecurity;

import com.www.common.config.oauth2.IOauth2Service;
import com.www.common.pojo.dto.security.ScopeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>@Description 资源服务器安全元数据源配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/24 18:22 </p>
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "com.www.common.oauth2",name = "enable") //是否开启oauth2资源服务配置
public class Oauth2MetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private IOauth2Service oauth2Service;
    /** 路径匹配器 **/
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:06 </p>
     * @return
     */
    public Oauth2MetadataSource(){
        log.info("资源服务器配置安全元数据源");
    }
    /**
     * <p>@Description 访问权限角色配置 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 21:38 </p>
     * @param o
     * @return java.util.Collection<org.springframework.security.access.ConfigAttribute>
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        log.debug("2、资源服务器配置URL可访问的scope范围");
        String requestURL = ((FilterInvocation)o).getRequestUrl();
        //查询当前资源服务器的请求路径允许的scope范围
        List<ScopeDTO> urlScopeList = oauth2Service.findUrlScope();
        //如果有数据，则添加对应的scope范围，如果没数据，则不限制scope
        if(CollectionUtils.isNotEmpty(urlScopeList)){
            List<String> roleList = new ArrayList<>();
            for (ScopeDTO dto : urlScopeList){
                if(antPathMatcher.match(dto.getUrl(),requestURL) && StringUtils.isNotBlank(dto.getScope())){
                    roleList.addAll(Arrays.asList(dto.getScope().split(",")));
                }
            }
            String[] roleArr = roleList.toArray(new String[roleList.size()]);
            return SecurityConfig.createList(roleArr);
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
