package com.www.common.config.exception;

import com.www.common.utils.MyStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>@Description 自定义异常类：业务异常 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/22 22:45 </p>
 */
@ResponseStatus(code = HttpStatus.OK, reason = "业务异常")
public class BusinessException extends RuntimeException{
    /** 异常码 **/
    private int code = 500;
    /**  异常信息 **/
    private String msg;
    /**
     * <p>@Description 业务异常 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:48 </p>
     * @param code 异常码
     * @param msg 异常信息
     * @return
     */
    public BusinessException(int code,String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    /**
     * <p>@Description 业务异常 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:48 </p>
     * @param code 异常码
     * @param msg 异常信息
     * @param valArr 异常信息设置值
     * @return
     */
    public BusinessException(int code,String msg,Object... valArr){
        super(MyStringUtils.format(msg,valArr));
        this.code = code;
        this.msg = MyStringUtils.format(msg,valArr);
    }
    /**
     * <p>@Description 业务异常 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:48 </p>
     * @param msg 异常信息
     * @return
     */
    public BusinessException(String msg){
        super(msg);
        this.msg = msg;
    }
    /**
     * <p>@Description 业务异常 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:48 </p>
     * @param msg 异常信息
     * @param valArr 异常信息设置值
     * @return
     */
    public BusinessException(String msg,Object... valArr){
        super(MyStringUtils.format(msg,valArr));
        this.msg = MyStringUtils.format(msg,valArr);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
