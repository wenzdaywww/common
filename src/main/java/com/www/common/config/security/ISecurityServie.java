package com.www.common.config.security;

import com.www.common.pojo.dto.security.AuthorityDTO;
import com.www.common.pojo.dto.security.UserDetailDTO;

import java.util.List;

/**
 * <p>@Description security的信息查询接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/12 15:45 </p>
 */
public interface ISecurityServie {
    /**
     * <p>@Description 根据用户ID查询用户的登录信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/12 15:46 </p>
     * @param userId 用户ID
     * @return com.www.myblog.common.pojo.UserDetailDTO 用户的登录信息
     */
    UserDetailDTO findUserDetailById(String userId);
    /**
     * <p>@Description 查询用户拥有的角色 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/12 15:52 </p>
     * @param userId 用户ID
     * @return java.util.List<java.lang.String> 角色集合
     */
    List<String> findUserRole(String userId);
    /**
     * <p>@Description 查询所有请求权限 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/12 15:59 </p>
     * @return java.util.List<com.www.myblog.common.pojo.AuthorityDTO> 所有权限
     */
    List<AuthorityDTO> findAllAuthority();
}
