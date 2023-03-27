package com.www.common.config.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>@Description mybatis结果集数量配置，未设置则默认不限制数量， </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/27 18:57 </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RowLimitInterceptor {
    /**
     * <p>@Description 配置结果集数量，大于0为有效数量配置，不大于0则不限制数量 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/27 19:06 </p>
     * @return 结果集数量
     */
    int limitNum() default 0;
}
