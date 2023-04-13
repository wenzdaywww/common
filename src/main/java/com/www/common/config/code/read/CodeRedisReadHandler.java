package com.www.common.config.code.read;

import com.www.common.config.code.CodeDict;
import com.www.common.config.code.CodeProperties;
import com.www.common.config.code.dto.CodeDTO;
import com.www.common.config.redis.RedisOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * <p>@Description 数据字典redis读取操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/4 12:22 </p>
 */
@Slf4j
public class CodeRedisReadHandler {
    @Autowired
    private CodeProperties codeProperties;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/11 21:45 </p>
     * @return
     */
    public CodeRedisReadHandler(){
        log.info("启动加载>>>数据字典redis读取操作类");
    }
    /**
     * <p>@Description 初始化code数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 17:19 </p>
     * @return void
     */
    public void initCodeData(){
        try {
            Map<String,Map<String, CodeDTO>> codeMap = (Map<String,Map<String, CodeDTO>>) RedisOperation.hashGet(codeProperties.getCodeRedisKey());
            if(MapUtils.isNotEmpty(codeMap)){
                CodeDict.initCode(codeMap);
                log.info("读取code_data数据{}条",codeMap.size());
            }else {
                log.error("读取code_data失败，redis中不存在数据");
            }
        }catch (Exception e){
            log.error("读取code_data失败，失败原因：{}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * <p>@Description 获取redis中的数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/4 12:03 </p>
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, com.www.common.config.code.dto.CodeDTO>>
     */
    public Map<String, Map<String, CodeDTO>> getCodeData() {
        return (Map<String,Map<String, CodeDTO>>) RedisOperation.hashGet(codeProperties.getCodeRedisKey());
    }
}
