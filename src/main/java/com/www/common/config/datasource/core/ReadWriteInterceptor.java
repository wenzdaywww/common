package com.www.common.config.datasource.core;

import com.www.common.config.datasource.MultiDataSourceAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

/**
 * <p>@Description 读写分离数据源切换的拦截器-AOP注入 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 20:48 </p>
 */
@Slf4j
@Aspect
public class ReadWriteInterceptor implements Ordered {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:03 </p>
     * @return
     */
    public ReadWriteInterceptor(){
        log.info("开启读写分离数据源切换AOP拦截");
    }
    /**
     * <p>@Description 读数据操作AOP切面 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:49 </p>
     * @param proceedingJoinPoint 切点
     * @return java.lang.Object
     */
    @Around(value = "execution(* com.www..*.data.mapper..*.find*(..))")
    public Object readIsFind(ProceedingJoinPoint proceedingJoinPoint){
        return this.readOnly(proceedingJoinPoint);
    }
    /**
     * <p>@Description 读数据操作AOP切面 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:49 </p>
     * @param proceedingJoinPoint 切点
     * @return java.lang.Object
     */
    @Around(value = "execution(* com.www..*.data.mapper..*.select*(..))")
    public Object readOnly(ProceedingJoinPoint proceedingJoinPoint){
        Object result = null;
        try {
            DataSourceHolder.setReadDataSourceType();
            result = proceedingJoinPoint.proceed();
        }catch (Exception e){
            log.error("连接slave数据源Exception异常：{}",e.getMessage());
            //有配置从数据库出现异常再从主数据库查询
            if(MultiDataSourceAutoConfiguration.getReadNum() != 0){
                result = insert(proceedingJoinPoint);
            }
        } catch (Throwable throwable) {
            log.error("连接slave数据源throwable异常：{}",throwable.getMessage());
        } finally {
            DataSourceHolder.clearDataSourceType();
        }
        return result;
    }
    /**
     * <p>@Description 插入数据操作AOP切面 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:49 </p>
     * @param proceedingJoinPoint 切点
     * @return java.lang.Object
     */
    @Around(value = "execution(* com.www..*.data.mapper..*.insert*(..))")
    public Object insert(ProceedingJoinPoint proceedingJoinPoint){
        Object result = null;
        try {
            DataSourceHolder.setWriteDataSourceType();
            result = proceedingJoinPoint.proceed();
        }catch (Exception e){
            log.error("连接master数据源Exception异常：{}",e.getMessage());
        } catch (Throwable throwable) {
            log.error("连接master数据源throwable异常：{}",throwable.getMessage());
        } finally {
            DataSourceHolder.clearDataSourceType();
        }
        return result;
    }
    /**
     * <p>@Description 删除数据操作AOP切面 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:50 </p>
     * @param proceedingJoinPoint 切点
     * @return java.lang.Object
     */
    @Around(value = "execution(* com.www..*.data.mapper..*.delete*(..))")
    public Object delete(ProceedingJoinPoint proceedingJoinPoint){
        return insert(proceedingJoinPoint);
    }
    /**
     * <p>@Description 更新数据操作AOP切面 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:50 </p>
     * @param proceedingJoinPoint 切点
     * @return java.lang.Object
     */
    @Around(value = "execution(* com.www..*.data.mapper..*.update*(..))")
    public Object update(ProceedingJoinPoint proceedingJoinPoint){
        return insert(proceedingJoinPoint);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
