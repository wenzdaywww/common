package com.www.common.config.file.upload.impl;

import com.www.common.config.file.FileProperties;
import com.www.common.config.file.upload.IFileService;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * <p>@Description 文件上传Service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/5 22:38 </p>
 */
@Slf4j
public class FileServiceImpl implements IFileService {
    /** mvc配置信息 **/
    @Autowired
    private FileProperties myMvcProperties;

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/3/22 21:21 </p>
     */
    public FileServiceImpl(){
        log.info("启动加载>>>文件上传自动配置");
    }

    /**
     * <p>@Description 将文件的绝对路径转为url访问路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 00:43 </p>
     * @param filePath
     * @return url访问路径，开头不含/
     */
    @Override
    public String convertToURL(String filePath) {
        String urlPath = myMvcProperties.getUrlPath();// /doc/**
        urlPath = urlPath.replace(CharConstant.STAR2,CharConstant.EMPTY); //  /doc/
        String newPath = StringUtils.replaceChars(filePath,CharConstant.RIGHT_SLASH,CharConstant.LEFT_SLASH);
        return StringUtils.substring(newPath,StringUtils.indexOf(newPath,urlPath)+1);
    }
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
    @Override
    public String uploadFileBackURL(MultipartFile file, String httpAddr, String prevPath, String fileName) {
        String url = this.uploadFileBackURL(file,prevPath,fileName);
        return StringUtils.isNotBlank(url) ? httpAddr + url : null;
    }
    /**
     * <p>@Description 上传文件并返回路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param httpAddr URL的地址端口,格式如：http://1.2.3.4:1000 或 https://1.2.3.4:1000
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 0=文件访问的URL完整路径,1=文件保存的绝对路径
     */
    @Override
    public String[] uploadFile(MultipartFile file, String httpAddr, String prevPath, String fileName) {
        String[] pathArr = this.saveFileReturnPath(file,prevPath,fileName);
        pathArr[0] = httpAddr + pathArr[0];
        return pathArr;
    }
    /**
     * <p>@Description 上传文件并返回路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 0=文件访问的URL相对路径,1=文件保存的绝对路径
     */
    @Override
    public String[] uploadFile(MultipartFile file, String prevPath, String fileName) {
        return this.saveFileReturnPath(file,prevPath,fileName);
    }
    /**
     * <p>@Description 上传文件并返回文件访问的URL相对路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件访问的URL相对路径
     */
    @Override
    public String uploadFileBackURL(MultipartFile file, String prevPath, String fileName) {
        return this.saveFile(file,prevPath,fileName,true);
    }
    /**
     * <p>@Description 上传文件并返回文件绝对路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file 文件
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件绝对路径
     */
    @Override
    public String uploadFileBackPath(MultipartFile file, String prevPath, String fileName){
        return this.saveFile(file,prevPath,fileName,false);
    }
    /**
     * <p>@Description 上传文件并返回文件对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:03 </p>
     * @param file     文件
     * @param prevPath 在文件保存的绝对路径(${com.www.common.file.save-path})下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件对象
     */
    @Override
    public File uploadFileBackFile(MultipartFile file, String prevPath, String fileName) {
        String[] path = this.getPath(prevPath);
        //保存上传的文件并返回文件对象
        return this.saveFile(file,path[1],fileName);
    }
    /**
     * <p>@Description 保存上传的文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/10 22:28 </p>
     * @param file 文件
     * @param prevPath 在文件保存的绝对路径下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @param isReturnUrl 是否返回url访问路径，ture返回url访问路径，false返回文件绝对路径
     * @return java.lang.String 返回url访问相对路径或文件绝对路径
     */
    private String saveFile(MultipartFile file, String prevPath, String fileName,boolean isReturnUrl){
        String[] pathArr = this.saveFileReturnPath(file,prevPath,fileName);
        return isReturnUrl ? pathArr[0] : pathArr[1];
    }
    /**
     * <p>@Description 保存上传的文件并返回文件对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 15:48 </p>
     * @param file 文件
     * @param prevPath 在文件保存的绝对路径下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @param fileName 保存的文件名，不含文件格式
     * @return 0=文件访问的URL完整路径,1=文件保存的绝对路径
     */
    private String[] saveFileReturnPath(MultipartFile file, String prevPath, String fileName){
        //获取url访问路径和文件保存绝对路径
        String[] path = this.getPath(prevPath);
        //保存上传的文件并返回文件对象
        File targetFile = this.saveFile(file,path[1],fileName);
        String type = targetFile.getName().substring(targetFile.getName().lastIndexOf(CharConstant.POINT));
        String[] pathArr = new String[2];
        pathArr[0] = path[0] + fileName + type; //文件访问的URL完整路径
        pathArr[1] = targetFile.getAbsolutePath(); //文件保存的绝对路径
        return pathArr;
    }
    /**
     * <p>@Description 保存上传的文件并返回文件对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/10 22:28 </p>
     * @param file 文件
     * @param savePath 文件保存的绝对路径
     * @param fileName 保存的文件名，不含文件格式
     * @return 文件对象
     */
    private File saveFile(MultipartFile file, String savePath, String fileName){
        if(file == null){
            throw new RuntimeException("文件对象为空");
        }
        //获取原始文件名称(包含格式)
        String origFileFullName = file.getOriginalFilename();
        //获取文件名称（不包含格式）
        String orgFileName = origFileFullName.substring(0, origFileFullName.lastIndexOf(CharConstant.POINT));
        //获取文件类型，以最后一个`.`为标识
        String fileType = origFileFullName.substring(origFileFullName.lastIndexOf(CharConstant.POINT) + 1);
        //判断文件夹是否存在，不存在则创建
        File filePath = new File(savePath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdirs();
        }
        if(StringUtils.isBlank(fileName)){
            //设置文件新名称: 当前时间+文件名称（不包含格式）
            String date = DateUtils.format(DateUtils.getCurrentDateTime(), DateFormatEnum.YYYYMMDDHHMMSSSSS);
            fileName = date + CharConstant.MINUS_SIGN + orgFileName + CharConstant.POINT + fileType;
        }else {
            fileName += CharConstant.POINT  + fileType;
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
            throw new RuntimeException("文件保存失败，失败信息：{}",e);
        }
        return targetFile;
    }
    /**
     * <p>@Description 获取url访问路径和文件保存绝对路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 20:34 </p>
     * @param prevPath 在文件保存的绝对路径下再添加一级或多级文件夹路径，选填，如temp、temp/test
     * @return [0]= url访问路径,[1]=文件保存绝对路径
     */
    private String[] getPath(String prevPath){
        String urlPath = myMvcProperties.getUrlPath();//文件访问的URL相对路径
        String savePath = myMvcProperties.getSavePath();//文件保存的绝对路径
        if(StringUtils.isBlank(urlPath)){
            throw new RuntimeException("文件访问的URL相对路径未配置");
        }
        if(StringUtils.isBlank(savePath)){
            throw new RuntimeException("文件保存的绝对路径未配置");
        }
        //添加上一级路径
        urlPath = StringUtils.isNotBlank(prevPath) ? urlPath.replace(CharConstant.STAR2,prevPath + CharConstant.LEFT_SLASH) : urlPath.replace(CharConstant.STAR2,CharConstant.EMPTY);
        savePath = StringUtils.isNotBlank(prevPath) ? savePath + prevPath + CharConstant.LEFT_SLASH : savePath;
        String[] path = {urlPath,savePath};
        return path;
    }
}
