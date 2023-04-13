package com.www.common.config.oauth2.util;

import com.www.common.config.redis.RedisOperation;
import com.www.common.data.constant.CharConstant;
import com.www.common.config.oauth2.dto.TokenInfoDTO;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>@Description redis的token处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/27 21:54 </p>
 */
public class RedisTokenHandler {
    /** redis的key前缀 **/
    private static final String PREFIX = "oauth2_token" + CharConstant.COLON;

    /**
     * <p>@Description 删除用户登录的token到redis中 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 22:50 </p>
     * @param tokenInfo token信息
     * @param redisKeyPrefix 用户登录的token保存到redis中的key的前缀，结尾不含冒号
     * @return boolean true删除成功，false删除失败
     */
    public static boolean deleteUserIdToken(TokenInfoDTO tokenInfo,String redisKeyPrefix){
        if(tokenInfo == null){
            return false;
        }
        //获取用户登录的token到redis中的key值
        String key = RedisTokenHandler.getUserIdTokenKey(tokenInfo,redisKeyPrefix);
        return RedisOperation.deleteKey(key);
    }
    /**
     * <p>@Description 保存用户登录的token到redis中 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 21:56 </p>
     * @param tokenInfo token信息
     * @param token token值
     * @param expiresSeconds 令牌有效时间
     * @param redisKeyPrefix 用户登录的token保存到redis中的key的前缀，结尾不含冒号
     * @return boolean true保存成功，false保存失败
     */
    public static boolean setUserIdToken(TokenInfoDTO tokenInfo,String token,int expiresSeconds,String redisKeyPrefix){
        if(StringUtils.isBlank(token) || tokenInfo == null){
            return false;
        }
        //获取用户登录的token到redis中的key值
        String key = RedisTokenHandler.getUserIdTokenKey(tokenInfo,redisKeyPrefix);
        //将token保存到redis中
        RedisOperation.set(key,token,expiresSeconds);
        return true;
    }
    /**
     * <p>@Description 判断当前token是否是redis中有效的token </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 21:58 </p>
     * @param tokenInfo token信息
     * @param token token值
     * @param redisKeyPrefix 用户登录的token保存到redis中的key的前缀，结尾不含冒号
     * @return boolean true有效，false无效
     */
    public static boolean isEffectiveToken(TokenInfoDTO tokenInfo,String token,String redisKeyPrefix){
        if(tokenInfo == null || StringUtils.isBlank(token)){
            return false;
        }
        //获取用户登录的token到redis中的key值
        String key = RedisTokenHandler.getUserIdTokenKey(tokenInfo,redisKeyPrefix);
        return  (RedisOperation.hasKey(key) && StringUtils.equals(token,RedisOperation.get(key)));
    }
    /**
     * <p>@Description 判断当前token是否是redis中无效的token </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 21:58 </p>
     * @param tokenInfo token信息
     * @param token token值
     * @param redisKeyPrefix 用户登录的token保存到redis中的key的前缀，结尾不含冒号
     * @return boolean true无效，false有效
     */
    public static boolean isInvalidToken(TokenInfoDTO tokenInfo,String token,String redisKeyPrefix){
        return !isEffectiveToken(tokenInfo,token,redisKeyPrefix);
    }
    /**
     * <p>@Description 获取用户登录的token到redis中的key值 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/27 22:52 </p>
     * @param tokenInfo token信息
     * @param redisKeyPrefix 用户登录的token保存到redis中的key的前缀，结尾不含冒号
     * @return java.lang.String
     */
    private static String getUserIdTokenKey(TokenInfoDTO tokenInfo,String redisKeyPrefix){
        if(tokenInfo == null){
            return null;
        }
        //用户key格式：oauth2_token:客户端ID:用户ID
        return redisKeyPrefix + CharConstant.COLON + tokenInfo.getClient_id() + CharConstant.COLON + tokenInfo.getUser_name();
    }
}
