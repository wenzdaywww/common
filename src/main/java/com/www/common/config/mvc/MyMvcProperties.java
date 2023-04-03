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
    private Boolean enable = false;
    /** 文件访问的URL相对路径,必须是/开头，**结尾 **/
    private String urlPath;
    /** 文件保存的绝对路径 **/
    private String savePath;
}
