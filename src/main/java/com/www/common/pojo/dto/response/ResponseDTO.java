package com.www.common.pojo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>@Description 响应报文类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:21 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 请求全局跟踪号 **/
    private String traceId;
    /** 响应码 **/
    private Integer code;
    /** 当前页数 **/
    private Integer pageNum;
    /** 页面条数 **/
    private Long pageSize;
    /** 总数 **/
    private Long totalNum;
    /** 响应消息 **/
    private String msg;
    /** 响应数据 **/
    private T data;

    /**
     * <p>@Description 响应报文构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:22 </p>
     * @param code 响应码
     * @param data 数据
     */
    public ResponseDTO(RespEnum code, T data) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }
    /**
     * <p>@Description 响应报文构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:22 </p>
     * @param code 响应码
     */
    public ResponseDTO(RespEnum code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }
    /**
     * <p>@Description 设置响应码值及响应信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/2 21:14 </p>
     * @param code 码值
     * @param data 响应信息
     * @return void
     */
    public void setResponse(RespEnum code, T data){
        this.setResponse(code,code.getMsg(),data);
    }
    /**
     * <p>@Description 设置响应码值及响应信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/2 21:14 </p>
     * @param data 响应信息
     * @return void
     */
    public void setResponse(T data){
        this.setResponse(RespEnum.SUCCESS,RespEnum.SUCCESS.getMsg(),data);
    }
    /**
     * <p>@Description 设置成功响应信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/2 21:14 </p>
     * @param code 响应码
     * @param msg 响应信息
     * @param data 响应信息
     * @return void
     */
    public void setResponse(RespEnum code, String msg, T data){
        this.code = code.getCode();
        this.msg = msg == null ? code.getMsg() : msg;
        this.data = data;
    }
    /**
     * <p>@Description 获取成功返回的数据,只要code=200才有值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/23 16:09 </p>
     * @param response 返回对象
     * @return T 返回数据
     */
    public static <T> T getBackData(ResponseDTO<T> response){
        if(response == null){
            return null;
        }
        return RespEnum.SUCCESS.getCode().equals(response.code) ? response.data : null;
    }
    /**
     * <p>@Description 响应码枚举值 </p>
     * <p>@Version 1.0 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:22 </p>
     */
    public enum RespEnum{
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

        RespEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
