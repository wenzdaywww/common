package com.www.common.config.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.common.config.redis.RedisOperation;
import com.www.common.config.security.MySecurityProperties;
import com.www.common.config.security.dto.AuthorityDTO;
import com.www.common.config.security.entity.SysRoleEntity;
import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.config.security.mapper.AuthorityRoleMapper;
import com.www.common.config.security.mapper.SysUserMapper;
import com.www.common.config.security.mapper.SysUserRoleMapper;
import com.www.common.config.security.dto.UserDetailDTO;
import com.www.common.data.constant.CharConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>@Description 用户详细信息服务类，用于实现spring security的登录认证 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:12 </p>
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    /**  标志1 **/
    private static String FLAG_1 = "1";
    @Autowired
    private MySecurityProperties mySecurityProperties;
    @Autowired
    private AuthorityRoleMapper authorityRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:20 </p>
     * @return
     */
    public UserDetailsServiceImpl(){
        log.info("启动加载：Security认证自动配置类：注册security配置用户详细信息服务类");
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
        UserDetailDTO userDTO = this.findUserDetailById(userId);
        if (userDTO == null) {
            return null;
        }
        //查询用户的角色
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roleList = this.findUserRole(userId);
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
    /**
     * <p>@Description 根据用户ID查询用户的登录信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/12 15:46 </p>
     * @param userId 用户ID
     * @return com.www.myblog.common.pojo.UserDetailDTO 用户的登录信息
     */
    public UserDetailDTO findUserDetailById(String userId){
        if(StringUtils.isBlank(userId)){
            return null;
        }
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserEntity::getUserId,userId);
        SysUserEntity userEntity = sysUserMapper.selectOne(wrapper);
        UserDetailDTO userDetailDTO = Optional.ofNullable(userEntity).map(entity -> {
            UserDetailDTO detailDTO = new UserDetailDTO();
            detailDTO.setUserId(entity.getUserId());
            detailDTO.setPassword(entity.getPassword());
            detailDTO.setEnabled(StringUtils.equals(entity.getStateCd(), FLAG_1));
            detailDTO.setAccountNonExpired(StringUtils.equals(entity.getNotExpired(), FLAG_1));
            detailDTO.setCredentialsNonExpired(StringUtils.equals(entity.getCredentialsNotExpired(), FLAG_1));
            detailDTO.setAccountNonLocked(StringUtils.equals(entity.getNotLocked(), FLAG_1));
            return detailDTO;
        }).orElse(null);
        return userDetailDTO;
    };
    /**
     * <p>@Description 查询用户拥有的角色 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/12 15:52 </p>
     * @param userId 用户ID
     * @return java.util.List<java.lang.String> 角色集合
     */
    public List<String> findUserRole(String userId){
        if(StringUtils.isBlank(userId)){
            return null;
        }
        //用户的角色信息的redis的key，不为空则缓存到redis中
        if(StringUtils.isNotBlank(mySecurityProperties.getUserPrefix())){
            //用户的角色信息的redis的key
            String userRoleKey = mySecurityProperties.getUserPrefix() + CharConstant.COLON + userId;
            //从redis中获取
            if(RedisOperation.hasKey(userRoleKey)){
                List<String> roleList = (List<String>)RedisOperation.listGet(userRoleKey);
                return roleList;
            }else {
                List<String> roleList = this.findUserRoleList(userId);
                if(CollectionUtils.isNotEmpty(roleList)){
                    roleList.forEach(dto -> {
                        RedisOperation.listSet(userRoleKey,dto);
                    });
                    RedisOperation.keyExpire(userRoleKey,mySecurityProperties.getUserExpireHour(), TimeUnit.HOURS);//设置超时
                }
                return roleList;
            }
        }else {
            return this.findUserRoleList(userId);
        }
    };
    /**
     * <p>@Description 查询用户拥有的角色 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 22:00 </p>
     * @param userId 用户ID
     * @return java.util.List<java.lang.String>
     */
    private List<String> findUserRoleList(String userId){
        List<SysRoleEntity> sysRoleList = sysUserRoleMapper.findUserRole(userId);
        List<String> roleList = Optional.ofNullable(sysRoleList)
                .filter(e -> CollectionUtils.isNotEmpty(sysRoleList)).map(list -> {
                    List<String> tempList = new ArrayList<>();
                    list.forEach(e -> {
                        tempList.add(e.getRoleCode());
                    });
                    return tempList;
                }).orElse(null);
        return roleList;
    }
    /**
     * <p>@Description 查询所有请求权限 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/12 15:59 </p>
     * @return java.util.List<com.www.myblog.common.pojo.AuthorityDTO> 所有权限
     */
    public List<AuthorityDTO> findAllAuthority(){
        //角色访问权限信息的redis的key，不为空则缓存到redis中
        if(StringUtils.isNotBlank(mySecurityProperties.getAuthRedisKey())){
            //从redis中获取
            if(RedisOperation.hasKey(mySecurityProperties.getAuthRedisKey())){
                List<AuthorityDTO> authorityList = (List<AuthorityDTO>)RedisOperation.listGet(mySecurityProperties.getAuthRedisKey());
                return authorityList;
            }else {
                List<AuthorityDTO> authorityList = authorityRoleMapper.findAllAuthorityRole();
                if(CollectionUtils.isNotEmpty(authorityList)){
                    authorityList.forEach(dto -> {
                        RedisOperation.listSet(mySecurityProperties.getAuthRedisKey(),dto);
                    });
                    RedisOperation.keyExpire(mySecurityProperties.getAuthRedisKey(),mySecurityProperties.getAuthExpireHour(), TimeUnit.HOURS);//设置超时
                }
                return authorityList;
            }
        }else {
            List<AuthorityDTO> authorityList = authorityRoleMapper.findAllAuthorityRole();
            return authorityList;
        }
    };
}
