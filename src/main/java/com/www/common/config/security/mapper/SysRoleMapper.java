package com.www.common.config.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.security.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>@Description 角色表Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {
}