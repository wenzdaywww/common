package com.www.common.pojo.dto.token;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Set;

/**
 * <p>@Description 令牌信息DTO </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/19 23:46 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class TokenDTO {
    /** 用户ID **/
    private String userId;
    /** token令牌 **/
    private String accessToken;
    /** 刷洗后的token **/
    private String refreshToken;
    /** token类型 **/
    private String tokenType;
    /** 令牌有效时间 **/
    private int expiresSeconds;
    /** 授权范围 **/
    private Set<String> scope;
}
