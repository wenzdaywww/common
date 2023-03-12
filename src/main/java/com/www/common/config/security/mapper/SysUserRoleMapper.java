package com.www.common.config.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.security.entity.SysRoleEntity;
import com.www.common.config.security.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 用户角色Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:25 </p>
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleEntity> {
    /**
     * <p>@Description 查询用户拥有的角色信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 21:45 </p>
     * @param userId 用户ID
     * @return java.util.List<com.www.myblog.base.data.entity.SysRoleEntity> 角色信息
     */
    List<SysRoleEntity> findUserRole(@Param("userId") String userId);
}