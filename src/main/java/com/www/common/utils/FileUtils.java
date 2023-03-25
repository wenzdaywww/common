package com.www.common.utils;

import com.csvreader.CsvReader;
import com.www.common.config.exception.BusinessException;
import com.www.common.data.constant.CharConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
     * <p>@Description 读取csv或excel文件，csv从第1行读取返回，excel从第rowNum行读取返回，读取成功或失败后删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:41 </p>
     * @param filePath csv或excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @param rowNum 开始读取的行数
     * @param isdelete 读取成功或失败后是否删除文件
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    public static ArrayList<ArrayList<String>> readCsvOrExcel(String filePath, String password, int rowNum, boolean isdelete){
        String fileType = filePath.substring(filePath.lastIndexOf(CharConstant.POINT),filePath.length());
        if(StringUtils.equalsIgnoreCase(fileType,".csv")){
            return FileUtils.readCsv(filePath,isdelete);
        }else if(StringUtils.equalsAnyIgnoreCase(fileType,".xlsx",".xls")){
            return FileUtils.readExcel(filePath,password,rowNum,isdelete);
        }else {
            throw new BusinessException("仅支持读取.xls、.xlsx、.csv文件");
        }
    }
    /**
     * <p>@Description 读取csv文件，从第1行读取返回,读取成功或失败后可选是否删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 21:52 </p>
     * @param filePath csv文件绝对路径
     * @param isdelete 读取成功或失败后是否删除文件
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    private static ArrayList<ArrayList<String>> readCsv(String filePath, boolean isdelete) {
        ArrayList<ArrayList<String>> arrList = new ArrayList<>();
        CsvReader reader = null;
        try {
            reader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            while (reader.readRecord()) {
                ArrayList<String> vaList = (ArrayList<String>) Arrays.stream(reader.getValues()).collect(Collectors.toList());
                arrList.add(vaList); // 按行读取，并把每一行的数据添加到list集合
            }

        } catch (Exception e) {
            log.error("读取文件：{}失败，失败信息：{}",filePath,e);
            throw new BusinessException("读取csv文件数据失败");
        }finally {
            if(isdelete){
                FileUtils.deleteFile(filePath);
            }
            if(reader != null){
                reader.close();
            }
        }
        if(isdelete){
            FileUtils.deleteFile(filePath);
        }
        return arrList;
    }
    /**
     * <p>@Description 读取excel，从第rowNum行读取返回,读取成功或失败后可选是否删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:28 </p>
     * @param filePath excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @param rowNum 开始读取的行数
     * @param isdelete 读取成功或失败后是否删除文件
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    private static ArrayList<ArrayList<String>> readExcel(String filePath, String password, int rowNum, boolean isdelete) {
        FileInputStream fis = null;
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        try {
            fis = new FileInputStream(filePath);
            Workbook workbook = null;
            if(StringUtils.isBlank(password)){
                workbook = WorkbookFactory.create(fis);
            }else {
                POIFSFileSystem pfs = new POIFSFileSystem(fis);
                fis.close();
                EncryptionInfo encInfo = new EncryptionInfo(pfs);
                Decryptor decryptor = Decryptor.getInstance(encInfo);
                decryptor.verifyPassword(password);
                workbook = new XSSFWorkbook(decryptor.getDataStream(pfs));
            }
            //获取sheet第一页（根据自己需要）
            Sheet sheet = workbook.getSheetAt(0);
            //获取表格的行数
            int totalRowNumber = sheet.getPhysicalNumberOfRows();
            for (int i = rowNum; i < totalRowNumber; i++) {
                ArrayList<String> cellList = new ArrayList();
                //获取一行数据
                Row row = sheet.getRow(i);
                //变量一行数据的每个单元格，row.getPhysicalNumberOfCells()是单元格的数量
                for (int j = 0, jLen = row.getPhysicalNumberOfCells(); j < jLen; j++) {
                    Cell cell = row.getCell(j);
                    cellList.add(cell.getStringCellValue());
                }
                dataList.add(cellList);
            }
            if(isdelete){
                FileUtils.deleteFile(filePath);
            }
            return dataList;
        }catch (EncryptedDocumentException e){
            throw new BusinessException("读取excel文件数据失败，密码错误");
        }catch (Exception e) {
            log.error("从Excel读取数据异常", e);
            throw new BusinessException("读取excel文件数据失败");
        } finally {
            if(isdelete){
                FileUtils.deleteFile(filePath);
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                log.error("从Excel读取数据异常", e);
                throw new BusinessException("读取excel文件数据失败");
            }
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
}
