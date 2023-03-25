package com.www.common.config.exception;

import com.www.common.data.constant.CharConstant;
import com.www.common.data.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * <p>@Description 全局异常处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:57 </p>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
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
     * @return ResponseDTO<java.lang.String>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> handlerException(Exception e){
        log.error("发生Exception异常：",e);
        return new ResponseEntity<>(new Result<>("请求失败"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * <p>@Description 自定义异常类：业务异常 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 21:00 </p>
     * @param e 业务异常
     * @return ResponseDTO<java.lang.String>
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<String>> handlerBusinessException(BusinessException e){
//        log.error("发生BusinessException异常：",e);
        return new ResponseEntity<>(new Result<>(e.getMsg()), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<Result<String>> handlerConstraintViolationException(ConstraintViolationException e){
//        log.error("发生ConstraintViolationException异常：",e);
        HashSet<ConstraintViolation> errSet = (HashSet) e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        errSet.forEach(er -> {
            sb.append(er.getMessage() + CharConstant.SEMICOLON + CharConstant.SPACE);
        });
        return new ResponseEntity<>(new Result<>(sb.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * <p>@Description 参数为自定义对象类的数据校验捕获返回,如：find(@Validated UserDTO user) </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 21:00 </p>
     * @param ex
     * @return Response<String>
     */
    @Override
    public ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
//        log.error("发生BindException异常：",e);
        List<FieldError> errList = ex.getFieldErrors();
        StringBuilder sb = Optional.ofNullable(errList).filter(item -> CollectionUtils.isNotEmpty(errList)).map(
                    list -> {
                        StringBuilder sbTmp = new StringBuilder();
                        list.forEach(er -> {
                            sbTmp.append(er.getDefaultMessage() + CharConstant.SEMICOLON + CharConstant.SPACE);
                        });
                        return sbTmp;
                    }
                ).orElse(new StringBuilder());
        return new ResponseEntity<>(new Result<>(sb.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
