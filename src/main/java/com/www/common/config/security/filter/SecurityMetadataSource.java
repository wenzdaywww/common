package com.www.common.config.security.filter;

import com.www.common.config.security.ISecurityServie;
import com.www.common.pojo.dto.security.AuthorityDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>@Description 安全元数据源配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/24 18:22 </p>
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "com.www.common.securuty",name = "enable") //是否开启Security安全
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Resource
    private ISecurityServie securityUserServie;
    /** 路径匹配 **/
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * <p>@Description  </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:14 </p>
     * @return
     */
    public SecurityMetadataSource(){
        log.info("security配置安全元数据源");
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
        log.info("3、访问权限角色配置");
        String requestURL = ((FilterInvocation)o).getRequestUrl();
        List<AuthorityDTO> roleMenuList = securityUserServie.findAllAuthority();
        if(CollectionUtils.isNotEmpty(roleMenuList)){
            List<String> roleList = new ArrayList<>();
            for (AuthorityDTO dto : roleMenuList){
                if(antPathMatcher.match(dto.getUrl(),requestURL) && StringUtils.isNotBlank(dto.getRole())){
                    roleList.add(dto.getRole());
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
