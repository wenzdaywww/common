package com.www.common.config.oauth2.constant;

/**
 * <p>@Description 角色权限常量值 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/22 17:52 </p>
 */
public class AuthorityContant {
    /** 一般用户角色 **/
    public static final String USER = "hasAnyAuthority('user')";
    /** 管理员角色 **/
    public static final String ADMIN = "hasAnyAuthority('admin')";
    /** 角色 **/
    public static final String ALL = "hasAnyAuthority('admin','user')";

}
