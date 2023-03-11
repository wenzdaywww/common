package com.www.common.config.security.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 请求权限DTO </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/12 15:57 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class AuthorityDTO {
    private static final long serialVersionUID = 1L;
    /** 请求路径 **/
    private String url;
    /** 授权角色 **/
    private String role;
}
