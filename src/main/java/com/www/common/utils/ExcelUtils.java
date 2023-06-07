package com.www.common.utils;

import com.www.common.config.exception.BusinessException;
import com.www.common.data.constant.CharConstant;
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
import org.aspectj.util.FileUtil;
import org.springframework.boot.actuate.autoconfigure.metrics.export.newrelic.NewRelicPropertiesConfigAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
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
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            if(!file.exists()){
                return false;
            }
            inputStream = new FileInputStream(filePath);
            Workbook workbook = null;
            if(StringUtils.isBlank(password)){
                workbook = WorkbookFactory.create(inputStream);
            }else {
                POIFSFileSystem pfs = new POIFSFileSystem(inputStream);
                inputStream.close();
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
            if(inputStream != null){
                try {
                    inputStream.close();
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
     * <p>@Description 根据模板文件配置的字段映射关系插入数据；
     *    <p> 1、最多支持data对象中的二级属性映射，如：#{name},#{user.name}</p>
     *    <p>2、如果写入字段为空，则需检查属性映射是否正确</p>
     *    <p>3、如果待写入的是List集合数据，则sheet页中配置的属性映射应是最后一行，否则会覆盖原有数据</p>
     * </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 15:47 </p>
     * @param templatePath excel模板文件绝对路径
     * @param newFilePath 插入数据后的excel文件保存路径，含文件扩展名，如：/home/ap/template-write.xlsx
     * @param data 待插入的数据对象
     * @return 插入数据后的excel文件保存完整路径，含文件扩展名，如：/home/ap/template-write.xlsx
     */
    public static String writeTemplateExcel(String templatePath, String newFilePath, Object data) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(templatePath);
            return ExcelUtils.writeTemplateExcel(inputStream,newFilePath,data);
        } catch (Exception e) {
            log.info("读取模板文件流失败，异常：{}",e);
            return null;
        }
    }
    /**
     * <p>@Description 根据模板文件配置的字段映射关系插入数据；
     *    <p> 1、最多支持data对象中的二级属性映射，如：#{name},#{user.name}</p>
     *    <p>2、如果写入字段为空，则需检查属性映射是否正确</p>
     *    <p>3、如果待写入的是List集合数据，则sheet页中配置的属性映射应是最后一行，否则会覆盖原有数据</p>
     * </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 15:47 </p>
     * @param inputStream excel模板文件流
     * @param newFilePath 插入数据后的excel文件保存路径，含文件扩展名，如：/home/ap/template-write.xlsx
     * @param data 待插入的数据对象
     * @return 插入数据后的excel文件保存完整路径，含文件扩展名，如：/home/ap/template-write.xlsx
     */
    public static String writeTemplateExcel(InputStream inputStream, String newFilePath, Object data) {
        if(data == null || inputStream == null || StringUtils.isAnyBlank(newFilePath)){
            return null;
        }
        OutputStream outputStream = null;
        try {
            Class tClass = data.getClass();//反射获取类
            //读取excel模板文件输入流
            Workbook workbook = WorkbookFactory.create(inputStream);
            //遍历sheet页
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()){
                Sheet sheet = sheetIterator.next();
                List rowList = new ArrayList();//需要循环插入的list集合数据。不含集合第一条数据
                List<CellDTO> cellStyleList = new ArrayList<>();//需要循环插入单元格样式Map<二级属性名称,CellStyle>
                int rowNum = sheet.getPhysicalNumberOfRows();//获取当前sheet的总行数
                //遍历sheet页中已存在的行对象
                for (int rowIndex = 0;rowIndex < rowNum;rowIndex++){
                    Row row = sheet.getRow(rowIndex);
                    int cellNum = row.getPhysicalNumberOfCells();//获取当前行数的总列数
                    //遍历行对象中的单元格对象
                    for (int cellIndex = 0;cellIndex < cellNum;cellIndex++){
                        Cell cell = row.getCell(cellIndex);
                        //模板文件配置的字段映射格式为  #{xxx}
                        if(StringUtils.startsWith(cell.getStringCellValue(),"#{")){
                            String cellValue = "";
                            //获取模板文件配置的字段映射，如：#{book.retainedProfits} 转为字符串数组，{"book","retainedProfits"}
                            String[] objArr = StringUtils.split(cell.getStringCellValue().replace("#{","").replace("}",""),".");
                            //字段映射格式只有一级属性，不含子属性，如： #{name}
                            if(objArr.length == 1){
                                Field field = tClass.getField(objArr[0]);
                                field.setAccessible(true);
                                Object fieldValue = field.get(data);
                                cellValue = fieldValue != null ? fieldValue.toString() : "";
                            }else if(objArr.length == 2){//字段映射格式有二级属性，如： #{book.retainedProfits}
                                //一级属性
                                Field field1Level = ExcelUtils.getField(tClass,objArr[0]);
                                field1Level.setAccessible(true);
                                Object field1Value = field1Level.get(data);
                                if(field1Value != null){
                                    Class fieldClass = field1Value.getClass();
                                    //如果一级属性是list集合
                                    if(field1Value instanceof List){
                                        //添加需要循环插入单元格样式
                                        CellDTO cellDTO = new CellDTO();
                                        cellDTO.setCellValue(objArr[1]).setCellStyle(cell.getCellStyle());
                                        cellStyleList.add(cellDTO);
                                        List dataList = ((List<?>) field1Value);
                                        Object listItem = dataList.get(0);
                                        field1Value = listItem;
                                        fieldClass = listItem.getClass();
                                        //待插入的list大于1条，则需要添加到rowList
                                        if(CollectionUtils.isEmpty(rowList) && dataList.size() > 1){
                                            rowList.addAll(dataList.subList(1,dataList.size()));
                                        }
                                    }
                                    //二级属性
                                    Field field2Level = ExcelUtils.getField(fieldClass,objArr[1]);
                                    field2Level.setAccessible(true);
                                    Object field2Value = field2Level.get(field1Value);
                                    cellValue = field2Value != null ? field2Value.toString() : "";
                                }
                            }
                            cell.setCellValue(cellValue);
                        }
                    }
                }
                //新增行数据循环插入的list集合数据。不含集合第一条数据
                for (int i = 0;i < rowList.size();i++){
                    Row row = sheet.createRow(rowNum);
                    Object obj = rowList.get(i);
                    Class objClass = obj.getClass();
                    for (int cellIndex = 0; cellIndex < cellStyleList.size();cellIndex++){
                        Field field = ExcelUtils.getField(objClass,cellStyleList.get(cellIndex).getCellValue());
                        field.setAccessible(true);
                        Object fieldValue1 = field.get(obj);
                        String cellValue = fieldValue1 != null ? fieldValue1.toString() : "";
                        //创建单元格
                        Cell cell = row.createCell(cellIndex);
                        cell.setCellValue(cellValue);
                        cell.setCellStyle(cellStyleList.get(cellIndex).getCellStyle());
                    }
                    rowNum++;
                }
            }
            //写入数据excel模板文件并转为新文件保存
            File outFile = new File(newFilePath);
            File dirFile = outFile.getParentFile();
            //linux中文件夹不存在需要先创建
            if(!dirFile.exists()){
                dirFile.mkdir();
            }
            if(!outFile.exists()){
                outFile.createNewFile();
            }
            outputStream = new FileOutputStream(outFile);
            workbook.write(outputStream);
            workbook.close();
            return outFile.getAbsolutePath().replace(CharConstant.RIGHT_SLASH,CharConstant.LEFT_SLASH);
        }catch (Exception e) {
            log.error("写入excel模板文件数据失败，异常信息：", e);
            return null;
        }finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
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
     * <p>@Description 获取反射类的属性 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/6/7 20:49 </p>
     * @param cls 反射类
     * @param name 属性名称
     * @return 属对象
     */
    private static Field getField(Class cls,String name){
        Field field = null;
        try {
            field = cls.getDeclaredField(name);
        }catch (Exception e){
            try {
                field = cls.getSuperclass().getDeclaredField(name);
            }catch (Exception e1){
                log.error("获取反射类的属性失败，失败信息：",e1);
                throw new BusinessException("获取反射类的属性失败");
            }
        }
        return field;
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
     * <p>@Description 删除excel中指定名称的sheet页 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 16:08 </p>
     * @param filePath excel完整路径
     * @param sheetName 待删除的sheet页名称
     * @return true删除成功。false删除失败
     */
    public static boolean deleteSheet(String filePath, String[] sheetName) {
        if(StringUtils.isBlank(filePath)){
            return false;
        }
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(inputStream);
            for (int i = 0;i < sheetName.length;i++){
                int sheetIndex = workbook.getSheetIndex(sheetName[i]);
                workbook.removeSheetAt(sheetIndex);
            }
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
            return true;
        }catch (Exception e) {
            log.error("删除excel的sheet页失败，异常", e);
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                }catch (Exception e ){
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
        if(StringUtils.isBlank(filePath)){
            return null;
        }
        FileInputStream inputStream = null;
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        try {
            rowNum = rowNum < 0 ? 0 : rowNum;
            inputStream = new FileInputStream(filePath);
            Workbook workbook = null;
            if(StringUtils.isBlank(password)){
                workbook = WorkbookFactory.create(inputStream);
            }else {
                POIFSFileSystem pfs = new POIFSFileSystem(inputStream);
                inputStream.close();
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
            if(isdelete){
                FileUtils.deleteFile(filePath);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
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
