package com.www.common.config.security.impl;

import com.www.common.config.security.ISecurityServie;
import com.www.common.pojo.dto.security.UserDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description 用户详细信息服务类，用于实现spring security的登录认证 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:12 </p>
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "com.www.common.securuty",name = "enable") //是否开启Security安全
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private ISecurityServie securityUserServie;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:20 </p>
     * @return
     */
    public UserDetailsServiceImpl(){
        log.info("security配置用户详细信息服务类");
    }
    /**
     * <p>@Description 加载用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:12 </p>
     * @param userId 用户ID
     * @return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("2、登录加载{}用户信息",userId);
        UserDetailDTO userDTO = securityUserServie.findUserDetailById(userId);
        if (userDTO == null) {
            return null;
        }
        //查询用户的角色
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roleList = securityUserServie.findUserRole(userId);
        if(CollectionUtils.isNotEmpty(roleList)){
            for (String roleCode : roleList){
                authorities.add(new SimpleGrantedAuthority(roleCode));
            }
        }
        //密码必须加密，否则无效
        User user = new User(userDTO.getUserId(), userDTO.getPassword(), userDTO.isEnabled(),
                userDTO.isAccountNonExpired(),userDTO.isCredentialsNonExpired(), userDTO.isAccountNonLocked(),authorities);
        return user;
    }
}
