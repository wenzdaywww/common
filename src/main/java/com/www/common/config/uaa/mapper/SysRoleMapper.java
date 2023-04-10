package com.www.common.config.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.uaa.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 角色表Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {
    /**
     * <p>@Description 查询用户拥有的角色信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 21:45 </p>
     * @param userId 用户ID
     * @return java.util.List<String> 角色信息
     */
    List<String> findUserRole(@Param("userId") String userId);
}