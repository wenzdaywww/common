package com.www.common.config.datasource;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.www.common.config.datasource.core.MultiDataSourceConfig;
import com.www.common.config.datasource.core.ReadWriteInterceptor;
import com.www.common.config.datasource.sources.impl.ReadOneDataSource;
import com.www.common.config.datasource.sources.impl.ReadTwoDataSource;
import com.www.common.config.datasource.sources.impl.WriteDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * <p>@Description 多数据源自动配置配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:53 </p>
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(value = {DataSourceProperties.class,MybatisPlusProperties.class})
@ConditionalOnProperty( prefix = "com.www.common.datasource", name = "enable", havingValue = "true")
public class DataSourceAutoConfiguration extends MultiDataSourceConfig {
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:00 </p>
     * @param properties
     * @param interceptorsProvider
     * @param typeHandlersProvider
     * @param languageDriversProvider
     * @param resourceLoader
     * @param databaseIdProvider
     * @param configurationCustomizersProvider
     * @param mybatisPlusPropertiesCustomizerProvider
     * @param applicationContext
     */
    public DataSourceAutoConfiguration(MybatisPlusProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider, ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider, ApplicationContext applicationContext) {
        super(properties, interceptorsProvider, typeHandlersProvider, languageDriversProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider, mybatisPlusPropertiesCustomizerProvider, applicationContext);
    }
    /**
     * <p>@Description 注册读写分离数据源切换的拦截器-AOP注入对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:59 </p>
     * @return com.www.common.config.datasource.interceptor.ReadWriteInterceptor
     */
    @Bean
    public ReadWriteInterceptor ReadWriteInterceptor(){
        return new ReadWriteInterceptor();
    }
    /**
     * <p>@Description 注册写权限数据源实现类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:01 </p>
     * @return com.www.common.config.datasource.datasoure.WriteDataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "com.www.common.datasource.write")
    @ConditionalOnProperty(prefix = "com.www.common.datasource.write",name = {"url"})
    public WriteDataSource writeDataSource(){
        return new WriteDataSource();
    }
    /**
     * <p>@Description 注册读权限数据源1实现类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:01 </p>
     * @return com.www.common.config.datasource.datasoure.WriteDataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "com.www.common.datasource.read-one")
    @ConditionalOnProperty(prefix = "com.www.common.datasource.read-one",name = {"url"})
    public ReadOneDataSource readOneDataSource(){
        return new ReadOneDataSource();
    }
    /**
     * <p>@Description 注册读权限数据源1实现类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:01 </p>
     * @return com.www.common.config.datasource.datasoure.WriteDataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "com.www.common.datasource.read-two")
    @ConditionalOnProperty(prefix = "com.www.common.datasource.read-two",name = {"url"})
    public ReadTwoDataSource readTwoDataSource(){
        return new ReadTwoDataSource();
    }
}
