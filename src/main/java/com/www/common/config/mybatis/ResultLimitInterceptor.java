package com.www.common.config.mybatis;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.www.common.config.mybatis.annotation.RowLimitInterceptor;
import com.www.common.data.constant.CharConstant;
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

import java.lang.reflect.Method;
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
        String mapperShortMethodName = mapperLongMethodName.substring(mapperLongMethodName.lastIndexOf(CharConstant.POINT) + 1);
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
        //通过反射判断mapper接口类的方法是否配置了@RowLimitInterceptor
        int limitNum = mybatisProperties.getLimitNum();
        Object[] annoArr = this.getRowLimitAnnotation(mapperLongMethodName);
        //配置注解，但结果集不大于0，则不需要限制数量，直接返回
        if((Boolean) annoArr[0] == true){
            if((Integer)annoArr[1] <= 0){
                return invocation.proceed();
            }else {
                //获取RowLimitInterceptor注解配置的结果集数量
                limitNum = (Integer) annoArr[1] > 0 ? (Integer) annoArr[1] : limitNum;
            }
        }
        //组装新sql语句
        String limitTypeTemp = StringUtils.equalsIgnoreCase(mybatisProperties.getDatabase(),ORACLE_DATABASE) ? " WHERE " + limitType + " <=" : limitType ;
        String limitSql = String.format(LIMIT_SQL,originalSql,limitTypeTemp,limitNum);
        metaObject.setValue("delegate.boundSql.sql",limitSql);
        log.info("结果集数量限制后SQL：{}",limitSql);
        return invocation.proceed();
    }
    /**
     * <p>@Description 获取mapper接口类的RowLimitInterceptor注解信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/27 19:29 </p>
     * @param mapperLongMethodName mapper接口方法完整路径
     * @return 0=是否使用RowLimitInterceptor注解配置结果集，1=注解配置的结果集数量
     */
    private Object[] getRowLimitAnnotation(String mapperLongMethodName){
        int annoLimitNum = 0;//注解配置的结果集数量
        boolean isAnnno = false;//是否使用RowLimitInterceptor注解配置结果集
        Object[] arr = new Object[2];
        try {
            //获取mapper类字符串
            String mapperClassStr = mapperLongMethodName.substring(0,mapperLongMethodName.lastIndexOf(CharConstant.POINT));//类路径
            String mapperShortMethodName = mapperLongMethodName.substring(mapperLongMethodName.lastIndexOf(CharConstant.POINT) + 1);//方法名
            Class mapperClass = Class.forName(mapperClassStr);
            Class[] interFaceArr = null;//接口类数组
            if(mapperClass.isInterface()){
                interFaceArr = new Class[1];
                interFaceArr[0] = mapperClass;
            }else {
                interFaceArr = mapperClass.getInterfaces();
            }
            //遍历接口类
            interfaceFor: for (int i = 0; interFaceArr != null && i < interFaceArr.length;i++){
                Method[] methodArr = interFaceArr[i].getDeclaredMethods();
                //遍历接口方法
                for (int j = 0; methodArr != null && j < methodArr.length;j++){
                    //mapper类没用重载方法，不需要考虑
                    if(StringUtils.equals(methodArr[j].getName(),mapperShortMethodName)){
                        RowLimitInterceptor[] annoArr = methodArr[j].getDeclaredAnnotationsByType(RowLimitInterceptor.class);
                        if(annoArr != null && annoArr.length > 0){
                            annoLimitNum = annoArr[0].limitNum();
                            isAnnno = true;
                            break interfaceFor;
                        }
                    }
                }
            }
        }catch (Exception e){
        }finally {
            arr[0] = isAnnno;
            arr[1] = annoLimitNum;
        }
        return arr;
    }
}
