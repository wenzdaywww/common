package com.www.common.config.exception;

import com.www.common.data.dto.response.ResponseDTO;
import com.www.common.data.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>@Description 全局异常处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:57 </p>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 21:00 </p>
     */
    public GlobalExceptionHandler(){
        log.info("启动配置：全局异常处理配置");
    }
    /**
     * <p>@Description 设置所有异常捕获返回 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 21:00 </p>
     * @param e
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseDTO<String> error(Exception e){
        log.error("发生异常：",e);
        return new ResponseDTO<>(ResponseEnum.FAIL,ResponseEnum.FAIL.getMsg());
    }
}
