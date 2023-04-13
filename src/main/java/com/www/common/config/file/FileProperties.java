package com.www.common.config.file;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>@Description 文件上传配置属性类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 21:13 </p>
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.common.file")
public class FileProperties {
    /** 是否开启文件上传，默认关闭false **/
    private Boolean enable = false;
    /** 文件访问的URL相对路径,必须是/开头，**结尾 **/
    private String urlPath;
    /** 文件保存的绝对路径 **/
    private String savePath;
}
