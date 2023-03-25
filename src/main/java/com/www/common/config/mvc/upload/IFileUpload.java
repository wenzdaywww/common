package com.www.common.config.mvc.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>@Description 文件上传Service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/5 22:37 </p>
 */
public interface IFileUpload {
    /**
     * <p>@Description 上传文件并返回文件绝对路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file 文件
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件绝对路径
     */
    String uploadFileBackPath(MultipartFile file, String prevPath,String fileName);
    /**
     * <p>@Description 上传文件并返回URL完整路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param httpAddr URL的地址端口,格式如：http://1.2.3.4:1000 或 https://1.2.3.4:1000
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件访问的URL完整路径
     */
    String uploadFileBackURL(MultipartFile file,String httpAddr,String prevPath,String fileName);
    /**
     * <p>@Description 上传文件并返回URL相对路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件访问的URL相对路径
     */
    String uploadFileBackURL(MultipartFile file, String prevPath,String fileName);
    /**
     * <p>@Description 上传文件并返回文件对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件对象
     */
    File uploadFileBackFile(MultipartFile file, String prevPath, String fileName);
}
