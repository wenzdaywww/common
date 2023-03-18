package com.www.common.config.exception;

import com.www.common.data.constant.CharConstant;
import com.www.common.data.response.Response;
import com.www.common.data.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.OrderByElement;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public Response<String> handlerException(Exception e){
        log.error("发生异常：",e);
        return new Response<>(ResponseEnum.FAIL,ResponseEnum.FAIL.getMsg());
    }
    /**
     * <p>@Description 参数为单个参数数据校验的捕获返回，如: find(@NotBlank(message = "name不能为空") String name) </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 21:00 </p>
     * @param e
     * @return ResponseResponseDTO<String>
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public Response<String> handlerConstraintViolationException(ConstraintViolationException e){
        HashSet<ConstraintViolation> errSet = (HashSet) e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        errSet.forEach(er -> {
            sb.append(er.getMessage() + CharConstant.SEMICOLON + CharConstant.SPACE);
        });
        return new Response<>(ResponseEnum.FAIL,sb.toString());
    }
    /**
     * <p>@Description 参数为自定义对象类的数据校验捕获返回,如：find(@Validated UserDTO user) </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 21:00 </p>
     * @param e
     * @return Response<String>
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public Response<String> handlerBindException(BindException e){
        List<FieldError> errList = e.getFieldErrors();
        StringBuilder sb = Optional.ofNullable(errList).filter(item -> CollectionUtils.isNotEmpty(errList)).map(
                    list -> {
                        StringBuilder sbTmp = new StringBuilder();
                        list.forEach(er -> {
                            sbTmp.append(er.getDefaultMessage() + CharConstant.SEMICOLON + CharConstant.SPACE);
                        });
                        return sbTmp;
                    }
                ).orElse(new StringBuilder());
        return new Response<>(ResponseEnum.FAIL,sb.toString());
    }
}
