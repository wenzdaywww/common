package com.www.common.config.mvc;

import com.www.common.config.mvc.upload.IFileUpload;
import com.www.common.config.mvc.upload.impl.FileUploadImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>@Description MVC配置 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/10 20:48 </p>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = MyMvcProperties.class)
@ConditionalOnProperty(prefix = "com.www.common.file",name = "enable")
public class MyMvcAutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private MyMvcProperties myMvcProperties;
    @Autowired
    private Environment environment;
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
        log.info("配置MVC文件资源拦截规则");
        //配置图片访问的相对路径
        registry.addResourceHandler(myMvcProperties.getImgUrlPath()).addResourceLocations(FILE + myMvcProperties.getImgSavePath());
        //配置图片外其他文件访问的相对路径
        registry.addResourceHandler(myMvcProperties.getOtherUrlPath()).addResourceLocations(FILE + myMvcProperties.getOtherSavePath());
    }
    /**
     * <p>@Description 注册文件上传对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:24 </p>
     * @return com.www.common.config.mvc.upload.IFileUpload
     */
    @Bean
    public IFileUpload fileUploadImpl(){
        return new FileUploadImpl(myMvcProperties,environment);
    }
}
