package com.www.common.pojo.enums;

/**
 * <p>@Description 响应码枚举值 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 10:47 </p>
 */
public enum ResponseEnum {
    /** 成功 **/
    SUCCESS(200, "请求成功"),
    /** 未找到 **/
    NOT_FOUND(404, "未找到资源"),
    /** 禁止访问 **/
    FORBIDDEN(403, "无权访问"),
    /** 失败 **/
    FAIL(500, "请求失败"),
    /** 未经授权 **/
    UNAUTHORIZED(401, "未经授权"),
    /**  未知异常 **/
    UNDEFINE(-1, "未知异常");
    /** 响应码 **/
    private Integer code;
    /** 响应信息 **/
    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
