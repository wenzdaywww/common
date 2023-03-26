package com.www.common.utils;

import com.www.common.config.exception.BusinessException;
import com.www.common.data.dto.excel.CellDTO;
import com.www.common.data.dto.excel.SheetDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>@Description excel文件工具类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/26 15:28 </p>
 */
@Slf4j
public class ExcelUtils {

    /**
     * <p>@Description 更新excel数据，根据dataMap更新指定单元格数据，文件不存在则更新失败 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:28 </p>
     * @param filePath excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @param dataMap 待写入的数据，格式：Map<sheet页序号,Map<行号,Map<列号,单元格数据>>>
     * @return true写入成功，false写入失败
     */
    public static boolean writeUpdateExcel(String filePath,String password, Map<Integer,Map<Integer, Map<Integer,CellDTO>>> dataMap) {
        if(MapUtils.isEmpty(dataMap)){
            return false;
        }
        FileInputStream fis = null;
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            if(!file.exists()){
                return false;
            }
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
            Iterator<Integer> iterator = dataMap.keySet().iterator();
            while (iterator.hasNext()){
                int sheetIndex = iterator.next();
                //获取sheet第一页（根据自己需要）
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                if(sheet != null){
                    //获取表格的行数
                    int totalRowNumber = sheet.getPhysicalNumberOfRows();
                    //获取待更新的行数据
                    Map<Integer,Map<Integer,CellDTO>> rowMap = dataMap.get(sheetIndex);
                    if(MapUtils.isNotEmpty(rowMap)){
                        //行遍历
                        Workbook finalWorkbook = workbook;
                        rowMap.forEach((rowNum, cellMap) -> {
                            //有效行内才能编辑
                            if(rowNum <= totalRowNumber){
                                Row row = sheet.getRow(rowNum);
                                if(MapUtils.isNotEmpty(cellMap)){
                                    int totalCellNum = row.getPhysicalNumberOfCells();//总列表
                                    //单元格遍历
                                    cellMap.forEach((cellNum,cellDTO) -> {
                                        //待更新列数在最大列数范围内则更新单元格，反之则创建单元格
                                        Cell cell = cellNum < totalCellNum ? row.getCell(cellNum) : row.createCell(cellNum);
                                        cell.setCellValue(cellDTO.getCellValue());
                                        CellStyle cellType = finalWorkbook.createCellStyle();
                                        //设置单元格背景色
                                        setCellStyle(cellDTO,cellType,cell);
                                    });
                                }
                            }
                        });
                    }
                }
            }
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            return true;
        }catch (EncryptedDocumentException e){
            log.error("读取excel文件数据失败，密码错误");
            return false;
        }catch (Exception e) {
            log.error("从Excel读取数据异常", e);
            return false;
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
    /**
     * <p>@Description 写入excel数据，从第1个sheet页开始写入，如果文件已存在，则覆盖写入 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:28 </p>
     * @param filePath excel文件绝对路径
     * @param excelList 待写入的数据
     * @return true写入成功，false写入失败
     */
    public static boolean writeCoverExcel(String filePath, List<SheetDTO> excelList) {
        if(CollectionUtils.isEmpty(excelList)){
            return false;
        }
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
            Workbook workbook = new XSSFWorkbook();
            excelList.forEach(sheetDTO -> {
                //设置sheet页数据
                Sheet sheet = workbook.createSheet(sheetDTO.getSheetName());
                workbook.setSheetName(sheetDTO.getSheetIndex(),sheetDTO.getSheetName());
                if(CollectionUtils.isNotEmpty(sheetDTO.getRowList())){
                    //设置行数据
                    sheetDTO.getRowList().forEach(rowDTO -> {
                        Row row = sheet.createRow(rowDTO.getRowIndex());
                        if(CollectionUtils.isNotEmpty(rowDTO.getCellList())){
                            //设置单元格数据
                            rowDTO.getCellList().forEach(cellDTO -> {
                                //设置单元格数值
                                Cell cell = row.createCell(cellDTO.getCellIndex());
                                cell.setCellValue(cellDTO.getCellValue());
                                CellStyle cellType = workbook.createCellStyle();
                                //设置单元格背景色
                                setCellStyle(cellDTO,cellType,cell);
                            });
                        }
                    });
                }
            });
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            return true;
        }catch (EncryptedDocumentException e){
            log.error("读取excel文件数据失败，密码错误");
            return false;
        }catch (Exception e) {
            log.error("从Excel读取数据异常", e);
            return false;
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
            }
        }
    }
    /**
     * <p>@Description 读取excel的第1个sheet页数据，从第rowNum行读取返回,读取成功或失败后可选是否删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:28 </p>
     * @param filePath excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @param rowNum 开始读取的行数
     * @param isdelete 读取成功或失败后是否删除文件
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    public static ArrayList<ArrayList<String>> readExcel(String filePath, String password, int rowNum, boolean isdelete) {
        FileInputStream fis = null;
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        try {
            rowNum = rowNum < 0 ? 0 : rowNum;
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
            return dataList;
        }catch (EncryptedDocumentException e){
            throw new BusinessException("读取excel文件数据失败，密码错误");
        }catch (Exception e) {
            log.error("从Excel读取数据异常", e);
            throw new BusinessException("读取excel文件数据失败");
        } finally {
            try {
                if(isdelete){
                    FileUtils.deleteFile(filePath);
                }
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
     * <p>@Description 设置单元格样式 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 22:29 </p>
     * @param cellDTO 单元格样式配置信息
     * @param cellType 单元格样式
     * @param cell 单元格
     */
    private static void setCellStyle(CellDTO cellDTO, CellStyle cellType, Cell cell){
        //设置单元格背景色
        if(cellDTO.getFillBackgroundColor() != null){
            cellType.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellType.setFillForegroundColor(cellDTO.getFillBackgroundColor());
            cell.setCellStyle(cellType);
        }
    }
}
