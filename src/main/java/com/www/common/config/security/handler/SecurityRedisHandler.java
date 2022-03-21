package com.www.common.config.security.handler;

import com.www.common.config.redis.RedisOperation;
import com.www.common.pojo.constant.CharConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * <p>@Description Security的redis操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/4 12:55 </p>
 */
@Component
@ConditionalOnProperty(prefix = "com.www.common.securuty",name = "enable") //是否开启Security安全
public class SecurityRedisHandler {
    /** 使用redis保存用户的token的key前缀 **/
    @Value("${com.www.common.securuty.user-prefix}")
    private String redisUserPrefix;

    /**
     * <p>@Description 判断redis中是否已经保存token </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 12:58 </p>
     * @param userId 用户id
     * @return boolean true存在，false不存在
     */
    public boolean hasToken(String userId){
        String tokenKey = redisUserPrefix + CharConstant.COLON + userId;
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
        String tokenKey = redisUserPrefix + CharConstant.COLON + userId;
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
        String tokenKey = redisUserPrefix + CharConstant.COLON + userId;
        return RedisOperation.deleteKey(tokenKey);
    }
    /**
     * <p>@Description 将token保存到redis中 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 13:04 </p>
     * @param userId 用户id
     * @param token 令牌
     * @param expirationTime 过期时间（秒）
     * @return boolean true保存成功，false失败
     */
    public boolean saveToken(String userId,String token,int expirationTime){
        //将token添加到redis中
        RedisOperation.set(redisUserPrefix + CharConstant.COLON + userId,token, expirationTime);
        return true;
    }
}
