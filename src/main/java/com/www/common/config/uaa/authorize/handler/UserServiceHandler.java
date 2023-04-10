package com.www.common.config.uaa.authorize.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.www.common.config.security.dto.UserDetailDTO;
import com.www.common.config.uaa.entity.SysUserEntity;
import com.www.common.config.uaa.mapper.SysRoleMapper;
import com.www.common.config.uaa.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description 用户详细信息服务类，用于实现spring security的登录认证 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:12 </p>
 */
@Slf4j
public class UserServiceHandler implements UserDetailsService {
    /** 用户信息有效标志值 **/
    private static String VALID =  "1";
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * <p>@Description 加载用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:12 </p>
     * @param userId 用户ID
     * @return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("验证{}用户信息",userId);
        if(StringUtils.isBlank(userId)){
            return null;
        }
        QueryWrapper<SysUserEntity> userWrapper = new QueryWrapper<>();
        userWrapper.lambda().eq(SysUserEntity::getUserId,userId);
        SysUserEntity userEntity = sysUserMapper.selectOne(userWrapper);
        if(userEntity == null){
            return null;
        }
        UserDetailDTO userDTO = new UserDetailDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setPassword(userEntity.getPassword());
        //用户是否可用判断
        userDTO.setEnabled(StringUtils.equals(userEntity.getStateCd(), VALID));
        //是否未过期判断
        userDTO.setAccountNonExpired(StringUtils.equals(userEntity.getNotExpired(), VALID));
        //账号是否未锁定判断
        userDTO.setCredentialsNonExpired(StringUtils.equals(userEntity.getCredentialsNotExpired(), VALID));
        //证书（密码）是否未过期判断
        userDTO.setAccountNonLocked(StringUtils.equals(userEntity.getNotLocked(), VALID));
        //查询用户的角色
        List<String> roleList = sysRoleMapper.findUserRole(userId);
        List<GrantedAuthority> authorities = new ArrayList<>();
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
