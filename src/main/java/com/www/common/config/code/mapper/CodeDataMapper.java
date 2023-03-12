package com.www.common.config.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.common.config.code.dto.CodeDTO;
import com.www.common.config.code.entity.CodeDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>@Description 数据字典信息表Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Mapper
public interface CodeDataMapper extends BaseMapper<CodeDataEntity> {
    /**
     * <p>@Description 查询所有有效的code数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 16:14 </p>
     * @return java.util.List<com.www.common.config.code.dto.CodeDTO>
     */
    List<CodeDTO> findAllCodeData();
}