package com.www.common.config.security.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>@Description 用户信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class UserDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 用户ID
    */
    private String userId;
    /**
    * 密码
    */
    private String password;
    /**
    * 用户是否可用
    */
    private boolean enabled;
    /**
    * 是否未过期
    */
    private boolean accountNonExpired;
    /**
    * 账号是否未锁定
    */
    private boolean credentialsNonExpired;
    /**
    * 证书（密码）是否未过期
    */
    private boolean accountNonLocked;
}