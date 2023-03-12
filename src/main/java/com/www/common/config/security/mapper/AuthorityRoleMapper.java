package com.www.common.config.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.security.dto.AuthorityDTO;
import com.www.common.config.security.entity.AuthorityRoleEntity;
import org.apache.ibatis.annotations.Mapper;

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
     * <p>@Description 查询角色访问权限信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/24 21:45 </p>
     * @return 角色访问权限信息
     */
    List<AuthorityDTO> findAllAuthorityRole();
}