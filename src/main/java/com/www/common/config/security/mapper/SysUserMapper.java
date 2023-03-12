package com.www.common.config.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.security.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>@Description 用户信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
}