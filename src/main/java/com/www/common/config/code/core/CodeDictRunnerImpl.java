package com.www.common.config.code.core;

import com.www.common.config.code.CodeDict;
import com.www.common.pojo.dto.code.CodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Map;

/**
 * <p>@Description 数据字典加载 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/1 16:04 </p>
 */
@Slf4j
public class CodeDictRunnerImpl implements ApplicationRunner {
    @Autowired
    private CodeRedisHandler codeRedisHandler;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 20:46 </p>
     */
    public CodeDictRunnerImpl(){
        log.info("启动自加载数据字典数据");
    }
    /**
     * <p>@Description 启动自加载数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 16:05 </p>
     * @param args
     * @return void
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动自加载数据字典数据");
        this.initCodeData();
    }
    /**
     * <p>@Description 初始化code数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 17:19 </p>
     * @return void
     */
    public void initCodeData(){
        try {
            Map<String,Map<String, CodeDTO>> codeMap = codeRedisHandler.getCodeData();
            if(MapUtils.isNotEmpty(codeMap)){
                CodeDict.initCode(codeMap);
                log.info("加载code_data数据{}条",codeMap.size());
            }else {
                log.info("加载code_data失败，redis中不存在数据");
                throw new RuntimeException("加载code_data失败，redis中不存在数据");
            }
        }catch (Exception e){
            log.error("加载code_data失败，失败原因：{}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
