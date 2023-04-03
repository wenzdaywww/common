package com.www.common.utils;

import com.www.common.config.exception.BusinessException;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.constant.FileTypeConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * <p>@Description 文件操作类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/22 21:33 </p>
 */
@Slf4j
public class FileUtils {
    /**
     * <p>@Description 读取csv或excel文件，从第1行读取返回，读取成功或失败后删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:41 </p>
     * @param filePath csv或excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    public static ArrayList<ArrayList<String>> readCsvOrExcelDel(String filePath, String password){
        return FileUtils.readCsvOrExcel(filePath,password,0,true);
    }
    /**
     * <p>@Description 读取csv或excel文件，从第1行读取返回，读取后不删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:41 </p>
     * @param filePath csv或excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    public static ArrayList<ArrayList<String>> readCsvOrExcel(String filePath, String password){
        return FileUtils.readCsvOrExcel(filePath,password,0,false);
    }
    /**
     * <p>@Description 读取csv或excel文件，从第rowNum行读取返回，读取成功或失败后删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:41 </p>
     * @param filePath csv或excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @param rowNum 开始读取的行数
     * @param isdelete 读取成功或失败后是否删除文件
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    public static ArrayList<ArrayList<String>> readCsvOrExcel(String filePath, String password, int rowNum, boolean isdelete){
        //根据文件路径获取文件扩展名
        String fileType = getFileType(filePath);
        if(StringUtils.equalsIgnoreCase(fileType, FileTypeConstant.CSV)){
            return CsvUtils.readCsv(filePath,rowNum,isdelete);
        }else if(StringUtils.equalsAnyIgnoreCase(fileType,FileTypeConstant.XLS,FileTypeConstant.XLSX)){
            return ExcelUtils.readExcel(filePath,password,rowNum,isdelete);
        }else {
            throw new BusinessException("仅支持读取.xls、.xlsx、.csv文件");
        }
    }
    /**
     * <p>@Description 删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 23:16 </p>
     * @param filePath 文件绝对路径
     * @return true删除成功，fasle删除失败
     */
    public static boolean deleteFile(String filePath){
        try {
            File file = new File(filePath);
            if(file.isFile()){
                return file.delete();
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    /**
     * <p>@Description 根据文件路径获取文件扩展名 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 23:48 </p>
     * @param filePath 文件路径
     * @return 扩展名,包含小数点，如  .csv
     */
    public static String getFileType(String filePath){
        return StringUtils.isBlank(filePath) ? "" :
                StringUtils.substring(filePath,filePath.lastIndexOf(CharConstant.POINT),filePath.length());
    }
    /**
     * <p>@Description 根据文件路径获取文件名 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 23:48 </p>
     * @param filePath 文件路径
     * @return 文件名,含扩展名
     */
    public static String getFileName(String filePath){
        return StringUtils.isBlank(filePath) ? "" :
                StringUtils.substring(filePath,filePath.lastIndexOf(CharConstant.LEFT_SLASH)+1,filePath.length());
    }
}
