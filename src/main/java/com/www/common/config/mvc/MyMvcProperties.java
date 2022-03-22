package com.www.common.config.mvc;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description  </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:13 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.file")
public class MyMvcProperties {
    /** 是否开启文件上传 **/
    private Boolean enable;
    /** 图片访问路径 **/
    private String imgUrlPath;
    /** 图片保存的绝对路径 **/
    private String imgSavePath;
    /** 图片外其他文件访问路径 **/
    private String otherUrlPath;
    /** 图片外其他文件保存的绝对路径 **/
    private String otherSavePath;
}
