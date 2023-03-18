package com.www.common.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>@Description redis操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:07 </p>
 */
@Slf4j
@Component
@ConditionalOnClass(RedisTemplate.class)
public final class RedisOperation {
    /** redisTemplate操作模板 **/
    private static volatile RedisTemplate<String,Object> redisTemplate;

    /**
     * <p>@Description 私有构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:11 </p>
     */
    public RedisOperation(){}
    /**
     * <p>@Description 返回redisTemplate实例 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.String, java.lang.Object>
     */
    public static RedisTemplate<String,Object> getRedisTemplate(){
        return redisTemplate;
    }
    /**
     * <p>@Description 获取key的剩余有效时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/7 20:58 </p>
     * @param key key值
     * @param timeUnit 时间单位
     * @return long 剩余有效时间
     */
    public static long getExpireTime(String key,TimeUnit timeUnit){
        if(StringUtils.isBlank(key)){
            return 0L;
        }
        return getRedisTemplate().getExpire(key,timeUnit);
    }
    /**
     * <p>@Description 获取key的剩余有效时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/7 20:58 </p>
     * @param key key值
     * @return long 剩余有效时间(秒)
     */
    public static long getExpireTime(String key){
        if(StringUtils.isBlank(key)){
            return 0L;
        }
        return getRedisTemplate().getExpire(key,TimeUnit.SECONDS);
    }
    /**
     * <p>@Description 获取所有模糊匹配的key值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/7 20:48 </p>
     * @param key 模糊匹配的key前缀，需包含*
     * @return java.util.Set<java.lang.String> 所有模糊匹配的key值
     */
    public static Set<String> getAllKeys(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return getRedisTemplate().keys(key);
    }
    /**
     * <p>@Description 设置key失效时间(秒) </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param seconds 失效时间(秒)
     * @return boolean true设置成功，false设置失败
     */
    public static boolean keyExpire(String key,long seconds){
        return getRedisTemplate().expire(key,seconds,TimeUnit.SECONDS);
    }
    /**
     * <p>@Description 设置key失效时间(秒) </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param timeout 失效时间
     * @param unit 时间单位
     * @return boolean true设置成功，false设置失败
     */
    public static boolean keyExpire(String key,long timeout, TimeUnit unit){
        return getRedisTemplate().expire(key,timeout,unit);
    }
    /**
     * <p>@Description 判断key值是否存在 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @return boolean true存在，false不存在
     */
    public static boolean hasKey(String key){
        return getRedisTemplate().hasKey(key);
    }
    /**
     * <p>@Description 删除key </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/17 20:56 </p>
     * @param key 键
     * @return boolean true删除成功，false失败
     */
    public static boolean deleteKey(String key){
        return getRedisTemplate().delete(key);
    }
    /**
     * <p>@Description 删除模糊key名 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/17 20:56 </p>
     * @param key 模糊键
     * @return boolean true删除成功，false失败
     */
    public static boolean deleteFuzzyKey(String key){
        Set<String> keys = getAllKeys(key);
        if(CollectionUtils.isNotEmpty(keys)){
            getRedisTemplate().delete(keys);
            return true;
        }
        return false;
    }
    /**
     * <p>@Description 保存String数据,不设置超时 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @return java.lang.Object
     */
    public static Object set(String key,Object value){
        getRedisTemplate().opsForValue().set(key,value);
        return value;
    }
    /**
     * <p>@Description 保存String数据,并设置超时时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @param seconds 超时时间(秒)
     * @return java.lang.Object
     */
    public static Object set(String key,Object value,long seconds){
        set(key,value,seconds,TimeUnit.SECONDS);
        return value;
    }
    /**
     * <p>@Description 保存String数据,并设置超时时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @param time 超时时间
     * @param timeUnit 超时时间单位
     * @return java.lang.Object
     */
    public static Object set(String key,Object value,long time,TimeUnit timeUnit){
        getRedisTemplate().opsForValue().set(key,value,time,timeUnit);
        return value;
    }
    /**
     * <p>@Description 获取分布式锁 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @return boolean true获取锁成功，false获取锁失败
     */
    public static boolean lock(String key,String value){
        return setNX(key,value);
    }
    /**
     * <p>@Description 获取分布式锁 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @param seconds 超时时间(秒)
     * @return boolean true获取锁成功，false获取锁失败
     */
    public static boolean lock(String key,String value,long seconds){
        return setNX(key,value,seconds);
    }
    /**
     * <p>@Description 释放分布式锁 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @return boolean true释放锁成功，false释放锁失败
     */
    public static boolean unlock(String key,String value){
        // 判断是否是当前线程获取分布式锁，是则删除key
        if (StringUtils.equals(value,get(key))){
            deleteKey(key);
            return true;
        }
        return false;
    }

    /**
     * <p>@Description key不存在时保存String数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @return boolean true键不存在，保存成功，false键存在，保存失败
     */
    public static boolean setNX(String key,String value){
        return getRedisTemplate().opsForValue().setIfAbsent(key,value);
    }
    /**
     * <p>@Description key不存在时保存String数据，并设置超时时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @param seconds 超时时间(秒)
     * @return boolean true键不存在，保存成功，false键存在，保存失败
     */
    public static boolean setNX(String key,String value,long seconds){
        return getRedisTemplate().opsForValue().setIfAbsent(key,value,seconds,TimeUnit.SECONDS);
    }
    /**
     * <p>@Description key不存在时保存String数据，并设置超时时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @param value 值
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return boolean true键不存在，保存成功，false键存在，保存失败
     */
    public static boolean setNX(String key,String value,long timeout,TimeUnit unit){
        return getRedisTemplate().opsForValue().setIfAbsent(key,value,timeout,unit);
    }
    /**
     * <p>@Description 获取String数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:07 </p>
     * @param key 键值
     * @return java.lang.Object
     */
    public static String get(String key){
        return (String) getRedisTemplate().opsForValue().get(key);
    }
    /**
     * <p>@Description 保存Hash数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param okey 对象键值
     * @param value 值
     * @return java.lang.Object
     */
    public static Object hashSet(String key, String okey, Object value){
        getRedisTemplate().opsForHash().put(key,okey,value);
        return value;
    }
    /**
     * <p>@Description 将对象保存Hash数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param obj 待保存的对象
     * @return java.lang.Object
     */
    public static Object hashSet(String key, Object obj){
        return hashSet(key,obj,null);
    }
    /**
     * <p>@Description 将对象保存Hash数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param obj 待保存的对象
     * @param fieldArr 待保存的对象中的字段名数组，null则保存对象所有字段
     * @return java.lang.Object
     */
    public static Object hashSet(String key, Object obj,String[] fieldArr){
        return hashSet(key,obj,fieldArr,null,null);
    }
    /**
     * <p>@Description 将对象保存Hash数据,并设置失效时间  </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param obj 待保存的对象
     * @param fieldArr 待保存的对象中的字段名数组，null则保存对象所有字段
     * @param seconds 失效时间(秒),null则不设置失效时间
     * @return java.lang.Object
     */
    public static Object hashSet(String key, Object obj,String[] fieldArr,Long seconds){
        return hashSet(key,obj,fieldArr,seconds,TimeUnit.SECONDS);
    }
    /**
     * <p>@Description 将对象保存Hash数据,并设置失效时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param obj 待保存的对象
     * @param fieldArr 待保存的对象中的字段名数组，null则保存对象所有字段
     * @param timeout 失效时间,null则不设置失效时间
     * @param unit 失效时间单位
     * @return java.lang.Object
     */
    public static Object hashSet(String key, Object obj,String[] fieldArr,Long timeout, TimeUnit unit){
        if(obj == null){
            return null;
        }
        //需要保存到redis的字段名称
        List<String> list = Arrays.asList(fieldArr);
        boolean isAllField = CollectionUtils.isEmpty(list);//是否保存所有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields){
            field.setAccessible(true);//可以获取到私有属性
            try {
                if(isAllField){
                    hashSet(key,field.getName(),field.get(obj));
                }else if(list.contains(field.getName())){
                    hashSet(key,field.getName(),field.get(obj));
                }
            } catch (IllegalAccessException e) {
                log.error("将对象保存Hash数据异常，异常信息：{}",e.getMessage());
            }
        }
        //设置失效时间
        if(timeout != null){
            keyExpire(key,timeout,unit);
        }
        return obj;
    }
    /**
     * <p>@Description 获取存储在哈希表中指定字段的值 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param okey 对象字段
     * @return java.lang.Object
     */
    public static Object hashGet(String key, String okey){
        return getRedisTemplate().opsForHash().get(key,okey);
    }
    /**
     * <p>@Description 获取在哈希表中指定 key 的所有字段和值 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @return java.lang.Object
     */
    public static Object hashGet(String key){
        return getRedisTemplate().opsForHash().entries(key);
    }
    /**
     * <p>@Description 获取在哈希表中指定key的所有字段和值并转为Class<T>类对象  </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param clazz 查询返回的对象类名
     * @return Class<T> 返回的对象数据
     */
    public static <T> T hashGet(String key,Class<T> clazz){
        return hashGet(key,clazz,null);
    }
    /**
     * <p>@Description 获取在哈希表中指定key的所有字段和值并转为Class<T>类对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param clazz 查询返回的对象类名
     * @param fieldArr 查询需要返回的对象的字段数组,null则返回所有字段
     * @return Class<T> 返回的对象数据
     */
    public static <T> T hashGet(String key,Class<T> clazz,String[] fieldArr){
        try {
            List<String> list = Arrays.asList(fieldArr);
            boolean isAllField = CollectionUtils.isEmpty(list);//是否获取所有字段
            T resultObj = clazz.newInstance();
            Field[] fields = resultObj.getClass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);//可以获取到私有属性
                if(isAllField){//获取所有字段
                    Object obj = hashGet(key,field.getName());
                    //将obj值保存到field中
                    setObjValue(obj,field,resultObj);
                }else if(list.contains(field.getName())){//获取指定字段
                    Object obj = hashGet(key,field.getName());
                    //将obj值保存到field中
                    setObjValue(obj,field,resultObj);
                }
            }
            return resultObj;
        } catch (Exception e) {
            log.error("获取在哈希表值发生异常，异常信息：{}",e.getMessage());
        }
        return null;
    }
    /**
     * <p>@Description 将obj值保存到field中 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 01:02 </p>
     * @param obj 值对象
     * @param field 字段对象
     * @param resultObj 对象
     * @return void
     */
    private static void setObjValue(Object obj,Field field,Object resultObj){
        try {
            //由于Long数据保存后取出为Integer，所以需要强转为Long
            if(obj != null && obj instanceof Integer && (field.getGenericType().toString().equals("class java.lang.Long"))){
                field.set(resultObj,((Integer)obj).longValue());
            }else {
                field.set(resultObj,obj);
            }
        } catch (Exception e) {
            log.error("获取在哈希表值发生异常，异常信息：{}",e.getMessage());
        }
    }
    /**
     * <p>@Description Hash数据的okey的值自增value </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param value 自增值，正数为自增，负数为自减
     * @return Long
     */
    public static Long hashIncrement(String key, long value){
        return getRedisTemplate().opsForValue().increment(key,value);
    }
    /**
     * <p>@Description 从左边保存List数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:08 </p>
     * @param key 键值
     * @param value 值
     * @return java.lang.Object
     */
    public static Object listSet(String key, Object value){
        getRedisTemplate().opsForList().leftPush(key,value);
        return value;
    }
    /**
     * <p>@Description 获取List集合中所有数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:09 </p>
     * @param key 键值
     * @return java.lang.Object
     */
    public static Object listGet(String key){
        return getRedisTemplate().opsForList().range(key,0,-1);
    }
    /**
     * <p>@Description 保存Set数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:09 </p>
     * @param key 键值
     * @param value 值
     * @return java.lang.Object
     */
    public static Object setSet(String key, Object value){
        getRedisTemplate().opsForSet().add(key,value);
        return value;
    }
    /**
     * <p>@Description 返回Set集合中的所有数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:09 </p>
     * @param key 键值
     * @return java.lang.Object
     */
    public static Object setGet(String key){
        return getRedisTemplate().opsForSet().members(key);
    }
    /**
     * <p>@Description 保存ZSet数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:09 </p>
     * @param key 键值
     * @param value 值
     * @param score 分值
     * @return java.lang.Object
     */
    public static Object zsetSet(String key, Object value, double score){
        getRedisTemplate().opsForZSet().add(key,value,score);
        return value;
    }
    /**
     * <p>@Description 获取ZSet数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:09 </p>
     * @param key 键值
     * @return java.lang.Object
     */
    public static Object zsetGet(String key){
        return getRedisTemplate().opsForZSet().range(key,0,-1);
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisOperation.redisTemplate = redisTemplate;
    }
}
