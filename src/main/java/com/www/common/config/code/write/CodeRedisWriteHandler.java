package com.www.common.config.code.write;

import com.www.common.config.code.CodeDict;
import com.www.common.config.code.CodeProperties;
import com.www.common.config.code.dto.CodeDTO;
import com.www.common.config.code.mapper.CodeDataMapper;
import com.www.common.config.redis.RedisOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>@Description 数据字典redis操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/4 12:22 </p>
 */
@Slf4j
public class CodeRedisWriteHandler {
    @Autowired
    private CodeProperties codeProperties;
    @Autowired
    private CodeDataMapper codeDataMapper;
    /**
     * <p>@Description 初始化code数据写入redis </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 17:19 </p>
     * @return void
     */
    public void initCodeData() {
        if(StringUtils.isNotBlank(codeProperties.getCodeRedisLock())){
            boolean isWait = true; //是否等待获取分布式锁
            String value = UUID.randomUUID().toString();
            while (isWait){
                try {
                    if(RedisOperation.lock(codeProperties.getCodeRedisLock(), value,60)){
                        isWait = false;
                        //查询code数据写入redis
                        this.writeToRedis();
                    }
                }catch (Exception e){
                    isWait = false;
                    log.error("查询所有CODE_DATA，发生异常：{}",e);
                }finally {
                    // 释放锁
                    RedisOperation.unlock(codeProperties.getCodeRedisLock(),value);
                }
            }
        }else {
            //查询code数据写入redis
            this.writeToRedis();
        }
    }
    /**
     * <p>@Description 查询code数据写入redis </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/12 13:23 </p>
     * @return void
     */
    private void writeToRedis(){
        List<CodeDTO> codeList = codeDataMapper.findAllCodeData();
        if(CollectionUtils.isNotEmpty(codeList)){
            Map<String, Map<String, CodeDTO>> codeMap = new HashMap<>();
            for (CodeDTO dto : codeList){
                Map<String, CodeDTO> keyMap = new HashMap<>();
                if(codeMap.containsKey(dto.getType())){
                    keyMap = codeMap.get(dto.getType());
                    keyMap.put(dto.getCodeKey(),dto);
                }else {
                    keyMap.put(dto.getCodeKey(),dto);
                    codeMap.put(dto.getType(),keyMap);
                }
            }
            if (MapUtils.isNotEmpty(codeMap)){
                RedisOperation.deleteKey(codeProperties.getCodeRedisKey());
                for (String key : codeMap.keySet()){
                    RedisOperation.hashSet(codeProperties.getCodeRedisKey(),key,codeMap.get(key));
                }
            }
            CodeDict.initCode(codeMap);
            log.info("写入redis的code_data数据{}条",codeList.size());
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
