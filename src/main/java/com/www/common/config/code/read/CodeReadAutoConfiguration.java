package com.www.common.config.code.read;

import com.www.common.config.code.CodeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>@Description 数据字典读取自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 20:42 </p>
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(value = CodeProperties.class)
@ConditionalOnProperty( prefix = "com.www.common.code", name = "read-enable", havingValue = "true")
public class CodeReadAutoConfiguration {
    /**
     * <p>@Description 注册数据字典redis操作类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 20:45 </p>
     * @return
     */
    @Bean
    public CodeRedisReadHandler codeRedisReadHandler(){
        return new CodeRedisReadHandler();
    }
    /**
     * <p>@Description 注册数据字典加载对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 20:51 </p>
     * @return
     */
    @Bean
    public CodeDictReadRunnerImpl codeDictReadRunner(){
        return new CodeDictReadRunnerImpl();
    }
    /**
     * <p>@Description 注册定时加载字典对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 20:51 </p>
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.www.common.code",name = {"read-scheduled"})
    public CodeDataReadTask codeDataReadTask(){
        return new CodeDataReadTask();
    }

}
