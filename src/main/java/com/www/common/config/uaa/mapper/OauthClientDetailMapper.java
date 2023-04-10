package com.www.common.config.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.uaa.entity.OauthClientDetailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>@Description 客户端信息表Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Mapper
public interface OauthClientDetailMapper extends BaseMapper<OauthClientDetailEntity> {
}