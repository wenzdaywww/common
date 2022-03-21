package com.www.common.config.datasource.core;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.www.common.config.datasource.interfaces.IReadDataSoure;
import com.www.common.config.datasource.interfaces.IWriteDataSoure;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * <p>@Description mybatis的多个数据源配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 20:47 </p>
 */
@Slf4j
@Configuration
@EnableTransactionManagement
//com.www.common.datasource.enable=true才开启多数据源配置
@ConditionalOnProperty(prefix = "com.www.common.datasource",name = "enable")
public class MultiDataSourceConfig extends MybatisPlusAutoConfiguration {
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
     * @return
     */
    public MultiDataSourceConfig(MybatisPlusProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider, ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider, ApplicationContext applicationContext) {
        super(properties, interceptorsProvider, typeHandlersProvider, languageDriversProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider, mybatisPlusPropertiesCustomizerProvider, applicationContext);
        log.info("配置mybatis多个数据源");
    }
    /**
     * <p>@Description 加载数据源类型 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 20:47 </p>
     * @return org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
     */
    @Bean(name = "routingDataSource")
    public AbstractRoutingDataSource routingDataSource(){
        ReadWriteDataSource proxy = new ReadWriteDataSource();
        SoftHashMap targetDataSource = new SoftHashMap<>();
        //加载写权限的数据源
        Map<String,IWriteDataSoure> writeMap = applicationContext.getBeansOfType(IWriteDataSoure.class); //写权限数据源集合
        IWriteDataSoure DefaultDataSource = null;
        if(MapUtils.isNotEmpty(writeMap)){
           for (String key : writeMap.keySet()){
               targetDataSource.put(WRITE_DATA_SOURCE_PREFIX + writeNum, writeMap.get(key));
               writeNum ++;
               if(DefaultDataSource == null){
                   DefaultDataSource = writeMap.get(key);
               }
           }
           log.info("加载{}个读写权限的数据源",writeNum);
        }
        //加载读权限的数据源
        Map<String,IReadDataSoure> readMap = applicationContext.getBeansOfType(IReadDataSoure.class);//读权限数据源集合
        if(MapUtils.isNotEmpty(readMap)){
            for (String key : readMap.keySet()){
                targetDataSource.put(READ_DATA_SOURCE_PREFIX + readNum, readMap.get(key));
                readNum ++;
            }
            log.info("加载{}个只读权限的数据源",readNum);
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
     * @return org.apache.ibatis.session.SqlSessionFactory
     */
    @Bean(name = "sqlSessionFactory")
    @Override
    public SqlSessionFactory sqlSessionFactory(@Qualifier("routingDataSource") DataSource dataSource) throws Exception {
        return super.sqlSessionFactory(dataSource);
    }
    /**
     * <p>@Description 获取写权限数据源个数 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/30 22:26 </p>
     * @return int
     */
    public static int getWriteNum() {
        return writeNum;
    }
    /**
     * <p>@Description 获取读权限数据源个数 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/30 22:26 </p>
     * @return int
     */
    public static int getReadNum() {
        return readNum;
    }
}
