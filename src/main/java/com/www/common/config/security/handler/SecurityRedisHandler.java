package com.www.common.config.security.handler;

import com.www.common.config.redis.RedisOperation;
import com.www.common.config.security.MySecurityProperties;
import com.www.common.data.constant.CharConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * <p>@Description Security的redis操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/4 12:55 </p>
 */
@Slf4j
public class SecurityRedisHandler {
    @Autowired
    private MySecurityProperties mySecurityProperties;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/23 10:56 </p>
     */
    public SecurityRedisHandler(){
        log.info("启动加载：Security认证自动配置类：注册Security的redis操作类");
    }
    /**
     * <p>@Description 判断redis中是否已经保存token </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 12:58 </p>
     * @param userId 用户id
     * @return boolean true存在，false不存在
     */
    public boolean hasToken(String userId){
        String tokenKey = mySecurityProperties.getTokenPrefix() + CharConstant.COLON + userId;
        return RedisOperation.hasKey(tokenKey);
    }
    /**
     * <p>@Description 获取redis中的token </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 13:00 </p>
     * @param userId 用户id
     * @return java.lang.String token
     */
    public String getToken(String userId){
        String tokenKey = mySecurityProperties.getTokenPrefix() + CharConstant.COLON + userId;
        return RedisOperation.get(tokenKey);
    }
    /**
     * <p>@Description 删除redis中的token </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 13:00 </p>
     * @param userId 用户id
     * @return java.lang.String token
     */
    public boolean deleteToken(String userId){
        String tokenKey = mySecurityProperties.getTokenPrefix() + CharConstant.COLON + userId;
        return RedisOperation.deleteKey(tokenKey);
    }
    /**
     * <p>@Description 将token保存到redis中 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 13:04 </p>
     * @param userId 用户id
     * @param token 令牌
     * @param expirationTime 过期时间（小时）
     * @return boolean true保存成功，false失败
     */
    public boolean saveToken(String userId,String token,int expirationTime){
        //将token添加到redis中
        RedisOperation.set(mySecurityProperties.getTokenPrefix() + CharConstant.COLON + userId,token, expirationTime, TimeUnit.HOURS);
        return true;
    }
}
