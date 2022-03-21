package com.www.common.pojo.constant;

/**
 * <p>@Description redis公共参数的key设置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/26 22:44 </p>
 */
public class RedisCommonContant {
    /** 资源服务ID的url的scope分布式锁key **/
    public static final String URL_SCOPE_LOCK =  "MY-BLOG:LOCK:URL_SCOPE:SYS_MENU";
    /** 资源服务ID的url的scope的redis的key前缀,完整格式： OAUTH2:RESOURCE_ID:URL_SCOPE:资源服务ID **/
    public static final String URL_SCOPE_PREFIX = "OAUTH2:RESOURCE_ID:URL_SCOPE:";
    /** 数据字典的key **/
    public static final String CODE_DATA = "MY-BLOG:CODE_DICT:CODE_DATA:CODE_TYPE";
    /** 数据字典的分布式锁key **/
    public static final String CODE_DATA_LOCK = "MY-BLOG:LOCK:CODE_DICT:CODE_DATA";
}
