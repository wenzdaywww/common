package com.www.common.config.oauth2.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 请求的scope范围 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/12 15:57 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class ScopeDTO {
    private static final long serialVersionUID = 1L;
    /** 资源服务id **/
    private String resourceId;
    /** 请求路径 **/
    private String url;
    /** scope范围 **/
    private String scope;
}
