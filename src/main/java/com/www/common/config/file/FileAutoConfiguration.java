package com.www.common.config.file;

import com.www.common.config.file.upload.IFileService;
import com.www.common.config.file.upload.impl.FileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>@Description 文件上传自动配置类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/10 20:48 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = FileProperties.class)
@ConditionalOnProperty(prefix = "com.www.common.file",name = "enable")
public class FileAutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private FileProperties myMvcProperties;
    /** 资源映射前缀 **/
    private static final String FILE = "file:";

    /**
     * <p>@Description 资源拦截配置 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/10 22:23 </p>
     * @param registry
     * @return void
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置文件访问的相对路径，
        registry.addResourceHandler(myMvcProperties.getUrlPath()).addResourceLocations(FILE + myMvcProperties.getSavePath());
        log.info("配置MVC文件资源拦截规则>>>文件访问的URL相对路径={}",myMvcProperties.getUrlPath());
    }
    /**
     * <p>@Description 注册文件上传对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:24 </p>
     * @return com.www.common.config.mvc.upload.IFileUpload
     */
    @Bean
    public IFileService fileUploadImpl(){
        return new FileServiceImpl();
    }
}
