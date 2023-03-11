package com.www.common.config.code.read;

import com.www.common.config.redis.RedisOperation;
import com.www.common.config.code.dto.CodeDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p>@Description 数据字典redis操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/4 12:22 </p>
 */
@Slf4j
public class CodeRedisHandler {
    /** redis中数据字典的key **/
    private String codeDataKey;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/11 23:37 </p>
     * @param codeDataKey redis中字典的key
     */
    CodeRedisHandler(String codeDataKey){
        this.codeDataKey = codeDataKey;
        log.info("codeDataKey"+ codeDataKey);
    }
    /**
     * <p>@Description 获取redis中的数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 12:53 </p>
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, com.www.common.pojo.dto.code.CodeDTO>>
     */
    public Map<String, Map<String, CodeDTO>> getCodeData(){
        return (Map<String,Map<String, CodeDTO>>) RedisOperation.hashGet(this.codeDataKey);
    }
}
