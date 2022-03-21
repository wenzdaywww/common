package com.www.common.pojo.dto.redis;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>@Description 博客标签信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 23:07 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class BlogTagDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 博客标签主键
    */
    private Long btId;
    /**
    * 博客ID
    */
    private Long blogId;
    /**
     * 用户ID
     */
    private String userId;
    /**
    * 标签ID
    */
    private Long tagId;
    /**
     * 标签名称
     */
    private String tagName;
}