package com.www.common.config.oauth2.resource.meta;

import com.www.common.config.oauth2.dto.ScopeDTO;
import com.www.common.config.oauth2.resource.Oauth2Properties;
import com.www.common.config.redis.RedisOperation;
import com.www.common.data.constant.CharConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
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
public class Oauth2MetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private Oauth2Properties oauth2Properties;
    /** 路径匹配器 **/
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:44 </p>
     */
    public Oauth2MetadataSource(){
        log.info("启动加载>>>Oauth2资源服务方自动配置>>>资源服务方配置URL可访问的范围");
    }
    /**
     * <p>@Description 访问权限角色配置 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 21:38 </p>
     * @param o
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        log.debug("2、资源服务器配置URL可访问的scope范围");
        String requestURL = ((FilterInvocation)o).getRequestUrl();
        //从redis中查询当前资源服务器的请求路径允许的scope范围
        List<ScopeDTO> urlScopeList = (List<ScopeDTO>) RedisOperation.listGet(oauth2Properties.getUrlScopePrefix() + CharConstant.COLON + oauth2Properties.getResourceId());
        //如果有数据，则添加对应的scope范围，如果没数据，则不限制scope
        if(CollectionUtils.isNotEmpty(urlScopeList)){
            List<String> roleList = new ArrayList<>();
            for (ScopeDTO dto : urlScopeList){
                if(antPathMatcher.match(dto.getUrl(),requestURL) && StringUtils.isNotBlank(dto.getScope())){
                    roleList.addAll(Arrays.asList(dto.getScope().split(CharConstant.COMMA)));
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
