package com.www.common.config.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
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
@ConditionalOnProperty(prefix = "com.www.common.file",name = "enable")
public class FileMvcConfig implements WebMvcConfigurer {
    /** 图片访问路径 **/
    @Value("${com.www.common.file.imgUrlPath}")
    private String imgUrlPath;
    /** 图片保存的绝对路径 **/
    @Value("${com.www.common.file.imgSavePath}")
    private String imgSavePath;
    /** 图片外其他文件访问路径 **/
    @Value("${com.www.common.file.otherUrlPath}")
    private String otherUrlPath;
    /** 图片外其他文件保存的绝对路径 **/
    @Value("${com.www.common.file.otherSavePath}")
    private String otherSavePath;
    /** 资源映射前缀 **/
    private String file = "file:";
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
        registry.addResourceHandler(imgUrlPath).addResourceLocations(file + imgSavePath);
        //配置图片外其他文件访问的相对路径
        registry.addResourceHandler(otherUrlPath).addResourceLocations(file + otherSavePath);
    }
}
