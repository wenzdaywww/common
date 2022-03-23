package com.www.common.config.mvc.upload.impl;

import com.www.common.config.mvc.MyMvcProperties;
import com.www.common.config.mvc.upload.IFileUpload;
import com.www.common.pojo.constant.CharConstant;
import com.www.common.pojo.enums.DateFormatEnum;
import com.www.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * <p>@Description 文件上传Service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/5 22:38 </p>
 */
@Slf4j
public class FileUploadImpl implements IFileUpload {
    /** 图片类型  **/
    private String[] imgType = {"BMP","JPG","JPEG","PNG","GIF"};
    /** ip地址 **/
    private String ip;
    /** 端口 **/
    @Value("${server.port}")
    private String serverPort;
    /** mvc配置信息 **/
    @Autowired
    private MyMvcProperties myMvcProperties;
    /** 环境配置信息 **/
    @Autowired
    private Environment environment;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:21 </p>
     */
    public FileUploadImpl (){
        log.info("启动加载：自定义MVC配置类：配置文件上传");
    }
    /**
     * <p>@Description 初始化数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/30 01:20 </p>
     * @return void
     */
    @PostConstruct
    private void init(){
        ip = environment.getProperty("eureka.instance.ip-address"); //获取ip
    }
    /**
     * <p>@Description 上传文件并返回url地址 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param prevPath 上一级文件夹，可设置多级，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return com.www.myblog.common.pojo.ResponseDTO<java.lang.String>
     */
    @Override
    public String uploadFileBackURL(MultipartFile file, String prevPath, String fileName) {
        String path = this.uploadFileBackPath(file,prevPath,fileName);
        if(StringUtils.isNotBlank(path)){
            path = CharConstant.HTTP + ip + CharConstant.COLON + serverPort + path;
            return path;
        }else {
            return null;
        }
    }

    /**
     * <p>@Description 上传文件并返回地址 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file 文件
     * @param prevPath 上一级文件夹，可设置多级，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return com.www.myblog.common.pojo.ResponseDTO<java.lang.String>
     */
    @Override
    public String uploadFileBackPath(MultipartFile file, String prevPath, String fileName){
        return this.saveFile(file,prevPath,fileName);
    }
    /**
     * <p>@Description 保存上传的文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/10 22:28 </p>
     * @param file 文件
     * @param prevPath 上一级文件夹，可设置多级，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return java.lang.String 返回文件的相对路径
     */
    private String saveFile(MultipartFile file, String prevPath,String fileName){
        if(file == null){
            return null;
        }
        //获取原始文件名称(包含格式)
        String origFileFullName = file.getOriginalFilename();
        //获取文件名称（不包含格式）
        String orgFileName = origFileFullName.substring(0, origFileFullName.lastIndexOf("."));
        //获取文件类型，以最后一个`.`为标识
        String fileType = origFileFullName.substring(origFileFullName.lastIndexOf(".") + 1);
        String urlPath = "";
        String savePath = "";
        //上传的文件为图片
        if(Arrays.asList(imgType).contains(StringUtils.upperCase(fileType))){
            urlPath = myMvcProperties.getImgUrlPath();;
            savePath = myMvcProperties.getImgSavePath();;
        }else {
            urlPath = myMvcProperties.getOtherUrlPath();;
            savePath = myMvcProperties.getOtherSavePath() + DateUtils.format(DateUtils.getCurrentDateTime(), DateFormatEnum.YYYYMMDD);
        }
        //添加上一级路径
        urlPath = StringUtils.isNotBlank(prevPath) ? urlPath.replace("**",prevPath + CharConstant.LEFT_SLASH) : urlPath.replace("**","");
        savePath = StringUtils.isNotBlank(prevPath) ? savePath + prevPath + CharConstant.LEFT_SLASH : savePath;
        //判断文件夹是否存在，不存在则创建
        File filePath = new File(savePath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdirs();
        }
        if(StringUtils.isBlank(fileName)){
            //设置文件新名称: 当前时间+文件名称（不包含格式）
            String date = DateUtils.format(DateUtils.getCurrentDateTime(), DateFormatEnum.YYYYMMDDHHMMSSSSS);
            fileName = date + "-" + orgFileName + "." + fileType;
        }else {
            fileName += "." + fileType;
        }
        //在指定路径下创建一个文件
        File targetFile = new File(savePath, fileName);
        try {
            //将文件保存到服务器指定位置
            file.transferTo(targetFile);
            log.info("上传成功,文件的保存路径：{},原始文件名称：{},文件类型：{},新文件名称：{}", savePath,origFileFullName,fileType,fileName);
            //将文件在服务器的存储路径返回
        } catch (IOException e) {
            log.info("上传失败,失败信息：{}",e.getMessage());
            return null;
        }
        return urlPath + fileName;
    }
}
