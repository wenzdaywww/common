package com.www.common.config.code.read;

import com.www.common.config.code.CodeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>@Description 数据字典自动读取配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 20:42 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = CodeProperties.class)
@ConditionalOnProperty( prefix = "com.www.common.code", name = "read-enable", havingValue = "true")
public class CodeReadAutoConfiguration {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:13 </p>
     */
    public CodeReadAutoConfiguration(){
        log.info("启动加载：数据字典自动读取配置类");
    }
    /**
     * <p>@Description 注册数据字典redis操作类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 20:45 </p>
     * @return com.www.common.config.code.CodeRedisHandler
     */
    @Bean
    public CodeRedisReadHandler codeRedisReadHandler(){
        return new CodeRedisReadHandler();
    }
    /**
     * <p>@Description 注册数据字典加载对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 20:51 </p>
     * @return com.www.common.config.code.init.CodeDictRunner
     */
    @Bean
    public CodeDictReadRunnerImpl codeDictReadRunner(){
        return new CodeDictReadRunnerImpl();
    }
    /**
     * <p>@Description 注册定时加载字典对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 20:51 </p>
     * @return com.www.common.config.code.task.CodeDataTask
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.www.common.code",name = {"read-scheduled"})
    public CodeDataReadTask codeDataReadTask(){
        return new CodeDataReadTask();
    }

}
