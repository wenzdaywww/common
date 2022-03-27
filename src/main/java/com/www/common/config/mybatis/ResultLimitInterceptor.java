package com.www.common.config.mybatis;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;

/**
 * <p>@Description Mybatis查询结果集拦截器 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/26 19:20 </p>
 */
@Slf4j
@Intercepts(@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class}))
public class ResultLimitInterceptor implements Interceptor {
    /** 结果集数量限制类型 **/
    private String limitType;
    /** oracle数据库 **/
    private static final String ORACLE_DATABASE = "oracle";
    /** 组装后的sql语句 **/
    private final static String LIMIT_SQL = "SELECT TMP.* FROM ( %s ) TMP %s %d";
    private MybatisProperties mybatisProperties;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/26 20:29 </p>
     * @return
     */
    public ResultLimitInterceptor(MybatisProperties mybatisProperties){
        this.mybatisProperties = mybatisProperties;
        limitType = StringUtils.equalsIgnoreCase(mybatisProperties.getDatabase(),ORACLE_DATABASE) ? " ROWNUM " : " LIMIT ";
        log.info("启动加载：自定义Mybatis配置：配置Mybatis查询结果集拦截器");
    }
    /**
     * <p>@Description 自定义结果集数量拦截 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/26 20:22 </p>
     * @param invocation
     * @return java.lang.Object
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        //非SELECT 则跳过
        if(!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())){
            return invocation.proceed();
        }
        //mapper方法
        String mapperLongMethodName = mappedStatement.getId();
        String mapperShortMethodName = mapperLongMethodName.substring(mapperLongMethodName.lastIndexOf(".") + 1);
        //mybatis-plusd的selectCount方法直接跳过
        if(StringUtils.equalsAnyIgnoreCase(mapperShortMethodName,"selectCount","selectList_COUNT")){
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();//原sql语句
        //包含序列则跳过
        if(StringUtils.equalsAnyIgnoreCase(originalSql,".NEXTVAL",".CURRVAL")){
            return invocation.proceed();
        }
        //原sql语句已包含查询数量
        if(StringUtils.lastIndexOfIgnoreCase(originalSql, limitType) != -1){
            return invocation.proceed();
        }
        //组装新sql语句
        String limitTypeTemp = StringUtils.equalsIgnoreCase(mybatisProperties.getDatabase(),ORACLE_DATABASE) ? " WHERE " + limitType + " <=" : limitType ;
        String limitSql = String.format(LIMIT_SQL,originalSql,limitTypeTemp,mybatisProperties.getLimitNum());
        metaObject.setValue("delegate.boundSql.sql",limitSql);
        log.info("结果集数量限制后SQL：{}",limitSql);
        return invocation.proceed();
    }
}
