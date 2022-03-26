package com.www.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description 自定义Mybatis配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:01 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = MybatisProperties.class)
@ConditionalOnClass(MybatisPlusInterceptor.class)
public class MyBatisCustomAutoConfiguration {
    @Autowired
    private MybatisProperties mybatisProperties;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:46 </p>
     */
    public MyBatisCustomAutoConfiguration(){
        log.info("启动加载：自定义Mybatis配置");
    }
    /**
     * <p>@Description 新的分页插件,一缓和二缓遵循mybatis的规则,
     *    需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     * </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:01 </p>
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        log.info("启动加载：自定义Mybatis配置：配置Mybatis分页拦截器");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    /**
     * <p>@Description 自定义拦截器规则 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/26 20:26 </p>
     * @param sqlSessionFactory
     * @return java.lang.String
     */
    @Bean
    public String myInterceptor(SqlSessionFactory sqlSessionFactory){
        if(BooleanUtils.isNotFalse(mybatisProperties.getLimit())){
            ResultLimitInterceptor resultLimitInterceptor = new ResultLimitInterceptor(mybatisProperties);
            sqlSessionFactory.getConfiguration().addInterceptor(resultLimitInterceptor);
        }
        return "success";
    }
}
