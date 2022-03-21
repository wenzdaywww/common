package com.www.common.pojo.dto.token;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

/**
 * <p>@Description 令牌解析后的信息DTO </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/19 23:46 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class TokenInfoDTO {
    /** 客户端ID **/
    private String client_id;
    /** 用户名 **/
    private String user_name;
    /** 授权范围 **/
    private ArrayList<String> scope;
    /** resouceId **/
    private ArrayList<String> aud;
    /** 令牌剩余有效时间 **/
    private long exp;
    /** 权限信息 **/
    private ArrayList<String> authorities;
    /** 标识符值 **/
    private String jti;
}
