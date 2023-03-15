package com.www.common.config.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.security.dto.AuthorityDTO;
import com.www.common.config.security.entity.AuthorityRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 角色访问权限信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:25 </p>
 */
@Mapper
public interface AuthorityRoleMapper extends BaseMapper<AuthorityRoleEntity> {
    /**
     * <p>@Description 查询角色请求访问权限信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 21:45 </p>
     * @return 角色访问权限信息
     */
    List<AuthorityDTO> findAllAuthorityRole();
    /**
     * <p>@Description 查询当前用户拥有的VUE路由 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 19:14 </p>
     * @param userId 用户ID
     * @return java.util.List<com.www.common.config.security.dto.AuthorityDTO>
     */
    List<AuthorityDTO> findUserAuthorityRole(@Param("userId") String userId);
    /**
     * <p>@Description 查询角色拥有的VUE路由 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 19:14 </p>
     * @param roleList 角色信息集合
     * @return java.util.List<com.www.common.config.security.dto.AuthorityDTO>
     */
    List<AuthorityDTO> findRoleAuthority(@Param("list") List<String> roleList);
}