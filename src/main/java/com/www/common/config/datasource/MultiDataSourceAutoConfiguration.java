package com.www.common.config.datasource;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.www.common.config.datasource.core.ReadWriteDataSourceProxy;
import com.www.common.config.datasource.core.ReadWriteInterceptor;
import com.www.common.config.datasource.sources.IReadDataSoure;
import com.www.common.config.datasource.sources.IWriteDataSoure;
import com.www.common.config.datasource.sources.impl.ReadOneDataSource;
import com.www.common.config.datasource.sources.impl.ReadTwoDataSource;
import com.www.common.config.datasource.sources.impl.WriteDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.aspectj.util.SoftHashMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * <p>@Description 多数据源自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:53 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {MultiDataSourceProperties.class,MybatisPlusProperties.class})
@ConditionalOnProperty( prefix = "com.www.common.datasource", name = "enable", havingValue = "true")
public class MultiDataSourceAutoConfiguration extends MybatisPlusAutoConfiguration {
    /** 写权限数据源前缀 **/
    public static final String WRITE_DATA_SOURCE_PREFIX = "writeDataSource_";
    /** 读权限数据源前缀 **/
    public static final String READ_DATA_SOURCE_PREFIX = "readDataSource_";
    /** 写权限数据源个数 **/
    private static int writeNum = 0;
    /** 写权限数据源个数 **/
    private static int readNum = 0;
    @Autowired
    private ApplicationContext applicationContext;

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
    public MultiDataSourceAutoConfiguration(MybatisPlusProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider, ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider, ApplicationContext applicationContext) {
        super(properties, interceptorsProvider, typeHandlersProvider, languageDriversProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider, mybatisPlusPropertiesCustomizerProvider, applicationContext);
        log.info("启动加载>>>多数据源自动配置类");
    }
    /**
     * <p>@Description 注册读写分离数据源切换的拦截器-AOP注入对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:59 </p>
     * @return
     */
    @Bean
    public ReadWriteInterceptor ReadWriteInterceptor(){
        return new ReadWriteInterceptor();
    }
    /**
     * <p>@Description 注册写权限数据源实现类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:01 </p>
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.www.common.datasource.write",name = {"url"})
    public WriteDataSource writeDataSource(){
        return new WriteDataSource();
    }
    /**
     * <p>@Description 注册默认数据源对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:01 </p>
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.www.common.datasource.write",name = {"url"})
    @ConditionalOnMissingBean
    public DataSource dataSource(){
        return new WriteDataSource();
    }
    /**
     * <p>@Description 注册读权限数据源1实现类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:01 </p>
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.www.common.datasource.read-one",name = {"url"})
    public ReadOneDataSource readOneDataSource(){
        return new ReadOneDataSource();
    }
    /**
     * <p>@Description 注册读权限数据源1实现类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 22:01 </p>
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.www.common.datasource.read-two",name = {"url"})
    public ReadTwoDataSource readTwoDataSource(){
        return new ReadTwoDataSource();
    }
    /**
     * <p>@Description 加载数据源类型 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:47 </p>
     * @return
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource(){
        ReadWriteDataSourceProxy proxy = new ReadWriteDataSourceProxy();
        SoftHashMap targetDataSource = new SoftHashMap<>();
        //加载写权限的数据源
        Map<String, IWriteDataSoure> writeMap = applicationContext.getBeansOfType(IWriteDataSoure.class); //写权限数据源集合
        IWriteDataSoure DefaultDataSource = null;
        if(MapUtils.isNotEmpty(writeMap)){
            for (String key : writeMap.keySet()){
                targetDataSource.put(WRITE_DATA_SOURCE_PREFIX + writeNum, writeMap.get(key));
                writeNum ++;
                if(DefaultDataSource == null){
                    DefaultDataSource = writeMap.get(key);
                }
            }
            log.info("启动加载>>>多数据源自动配置>>>加载{}个读写权限的数据源",writeNum);
        }
        //加载读权限的数据源
        Map<String, IReadDataSoure> readMap = applicationContext.getBeansOfType(IReadDataSoure.class);//读权限数据源集合
        if(MapUtils.isNotEmpty(readMap)){
            for (String key : readMap.keySet()){
                targetDataSource.put(READ_DATA_SOURCE_PREFIX + readNum, readMap.get(key));
                readNum ++;
            }
            log.info("启动加载>>>多数据源自动配置>>>加载{}个只读权限的数据源",readNum);
        }
        //默认数据源
        proxy.setDefaultTargetDataSource(DefaultDataSource);
        //添加从数据源
        proxy.setTargetDataSources(targetDataSource);
        return proxy;
    }
    /**
     * <p>@Description 加载数据源到mybatis的SqlSessionFactory中 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:47 </p>
     * @param dataSource
     * @return
     */
    @Bean
    @Override
    public SqlSessionFactory sqlSessionFactory(@Qualifier("routingDataSource") DataSource dataSource) throws Exception {
        return super.sqlSessionFactory(dataSource);
    }
    /**
     * <p>@Description 获取写权限数据源个数 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/30 22:26 </p>
     * @return int 写权限数据源个数
     */
    public static int getWriteNum() {
        return writeNum;
    }
    /**
     * <p>@Description 获取读权限数据源个数 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/30 22:26 </p>
     * @return int 读权限数据源个数
     */
    public static int getReadNum() {
        return readNum;
    }
}
