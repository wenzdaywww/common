package com.www.common.config.code;

import com.www.common.config.redis.RedisOperation;
import com.www.common.pojo.constant.RedisCommonContant;
import com.www.common.pojo.dto.code.CodeDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>@Description 数据字典redis操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/4 12:22 </p>
 */
@Component
@ConditionalOnProperty(prefix = "com.www.common.code",name = "enable")
public class CodeRedisHandler {

    /**
     * <p>@Description 获取redis中的数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 12:53 </p>
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, com.www.common.pojo.dto.code.CodeDTO>>
     */
    public Map<String, Map<String, CodeDTO>> getCodeData(){
        return (Map<String,Map<String, CodeDTO>>) RedisOperation.hashGet(RedisCommonContant.CODE_DATA);
    }
}
