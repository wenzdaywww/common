package com.www.common.config.code.read;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * <p>@Description 启动自动从redis读取数据字典数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/1 16:04 </p>
 */
@Slf4j
public class CodeDictReadRunnerImpl implements ApplicationRunner {
    @Autowired
    private CodeRedisReadHandler codeRedisReadHandler;
    /**
     * <p>@Description 启动自加载数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 16:05 </p>
     * @param args
     * @return void
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动加载>>>启动自动从redis读取数据字典数据");
        codeRedisReadHandler.initCodeData();
    }

}
