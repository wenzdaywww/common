package com.www.common.utils;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.www.common.config.exception.BusinessException;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.constant.FileTypeConstant;
import com.www.common.data.dto.excel.CellDTO;
import com.www.common.data.dto.excel.RowDTO;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
            return FileUtils.readCsv(filePath,rowNum,isdelete);
        }else if(StringUtils.equalsAnyIgnoreCase(fileType,FileTypeConstant.XLS,FileTypeConstant.XLSX)){
            return FileUtils.readExcel(filePath,password,rowNum,isdelete);
        }else {
            throw new BusinessException("仅支持读取.xls、.xlsx、.csv文件");
        }
    }
    /**
     * <p>@Description 更新csv文件的第1个sheet页数据指定单元格数据,文件不存在则更新失败 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 18:08 </p>
     * @param filePath csv文件绝对路径
     * @param dataMap 待更新的数据，Map<行号,Map<列号,单元格数据>>,行号必须是在文件种已存在的数据才能写入成功
     * @param isCover true覆盖单元格数据，false在指定单元格之后插入数据
     * @return true写入成功，false写入失败
     */
    public static boolean writeUpdateCsv(String filePath, Map<Integer,Map<Integer,String>> dataMap,boolean isCover) {
        if(MapUtils.isEmpty(dataMap)){
            return false;
        }
        CsvReader reader = null;
        try {
            File file = new File(filePath);
            if(!file.exists()){
                return false;
            }
            reader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            ArrayList<ArrayList<String>> rowList = new ArrayList<>();
            //先读取行数据
            for (int row = 0; reader.readRecord();row++){
                //行数据
                ArrayList<String> vaList = (ArrayList<String>) Arrays.stream(reader.getValues()).collect(Collectors.toList());
                //判断是待更新的行数据
                if(dataMap.containsKey(row)){
                    dataMap.get(row).forEach((cell,value) -> {
                        //覆盖原单元格数据
                        if(isCover){
                            vaList.set(cell,value);
                        }else {//在指定单元格之后插入数据
                            vaList.add(cell,value);
                        }
                    });
                }
                rowList.add(vaList);
            }
            reader.close();
            reader = null;
            return writeNewCsv(filePath,rowList);
        } catch (Exception e) {
            log.error("写入文件：{}失败，失败信息：{}",filePath,e);
            return false;
        }finally {
            if(reader != null){
                reader.close();
            }
        }
    }
    /**
     * <p>@Description 从csv文件的第1个sheet页数据第1行写入数据，文件存在则覆盖，不存在则创建 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 17:48 </p>
     * @param filePath csv文件绝对路径，文件存在则覆盖，不存在则创建
     * @param dataList 待写入的数据，从第1行写入，格式：行集合<单元格集合<单元格数据>>
     * @return true写入成功，false写入失败
     */
    public static boolean writeNewCsv(String filePath, ArrayList<ArrayList<String>> dataList) {
        if(CollectionUtils.isEmpty(dataList)){
            return false;
        }
        CsvWriter writer = null;
        try {
            writer = new CsvWriter(filePath,',', Charset.forName("UTF-8"));
            for (int row = 0; row < dataList.size(); row++){
                String[] rowArr = dataList.get(row).toArray(new String[] {});
                writer.writeRecord(rowArr);
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            log.error("写入文件：{}失败，失败信息：{}",filePath,e);
            return false;
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }
    /**
     * <p>@Description 读取csv文件数据，同时更新对应单元格数据后转为excel文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:28 </p>
     * @param filePath excel文件绝对路径
     * @param dataMap 待写入的数据，为空则只复制csv数据。格式：Map<行号,Map<列号,单元格数据>>,如果行号大于原csv数据行数，则依次插入新行，但列号则固定，即插入第几列不变
     * @return excel文件绝对路径
     */
    public static String csvToExcel(String filePath,Map<Integer,Map<Integer,CellDTO>> dataMap){
        //读取csv文件数据
        ArrayList<ArrayList<String>> csvList = readCsv(filePath,0,false);
        if(CollectionUtils.isEmpty(csvList)){
            return null;
        }
        String excelName = StringUtils.substring(filePath,0,filePath.lastIndexOf(CharConstant.POINT)) + FileTypeConstant.XLSX;
        //csv数据转为excel数据
        List<RowDTO> csvDataList = new ArrayList<>();
        for (int rowNum = 0;rowNum < csvList.size();rowNum++){
            List<CellDTO> cellList = new ArrayList<>();
            for (int cellNum = 0;cellNum < csvList.get(rowNum).size();cellNum++){
                CellDTO cellDTO = new CellDTO();
                cellDTO.setCellValue(csvList.get(rowNum).get(cellNum)).setCellIndex(cellNum);
                cellList.add(cellDTO);
            }
            RowDTO rowDTO = new RowDTO();
            rowDTO.setRowIndex(rowNum).setCellList(cellList);
            csvDataList.add(rowDTO);
        }
        List<RowDTO> excelDataList = csvDataList.subList(0,csvDataList.size());
        //将待更新的数据插入待保存的excel数据中
        if(MapUtils.isNotEmpty(dataMap)){
            dataMap.forEach((rowNum,cellMap) -> {
                //待更新的行数据在csv原数据行数内
                if(rowNum < csvDataList.size()){
                    RowDTO rowDTO = excelDataList.get(rowNum);
                    List<CellDTO> cellList = rowDTO.getCellList();
                    cellMap.forEach((cellNum,cellDTO) -> {
                        //更新
                        if(cellNum < cellList.size()){
                            cellList.get(cellNum).setCellValue(cellDTO.getCellValue())
                                    .setFillBackgroundColor(cellDTO.getFillBackgroundColor());
                        }else {//插入新数据
                            cellList.add(cellDTO);
                        }
                    });
                }else {//待更新的数据超出csv原数据行数，则依次插入新行
                    List<CellDTO> cellList = new ArrayList<>();
                    cellMap.forEach((cellNum,cellDTO) -> {
                        cellList.add(cellDTO);
                    });
                    RowDTO rowDTO = new RowDTO();
                    rowDTO.setCellList(cellList).setRowIndex(excelDataList.size());
                    excelDataList.add(rowDTO);
                }
            });
        }
        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setSheetIndex(0).setSheetName("sheet").setRowList(excelDataList);
        List<SheetDTO> excelList = new ArrayList<>();
        excelList.add(sheetDTO);
        //生成excel文件
        boolean isOk = writeCoverExcel(excelName,excelList);
        return isOk ? excelName : null;
    }
    /**
     * <p>@Description 更新excel数据，根据dataMap更新指定单元格数据，文件不存在则更新失败 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:28 </p>
     * @param filePath excel文件绝对路径
     * @param password excel文件密码，excel文件加密时必输，否则会报错
     * @param dataMap 待写入的数据，格式：Map<sheet页序号,Map<行号,Map<列号,单元格数据>>>
     * @return true写入成功，false写入失败
     */
    public static boolean writeUpdateExcel(String filePath,String password, Map<Integer,Map<Integer,Map<Integer,CellDTO>>> dataMap) {
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
     * <p>@Description 设置单元格样式 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 22:29 </p>
     * @param cellDTO 单元格样式配置信息
     * @param cellType 单元格样式
     * @param cell 单元格
     */
    private static void setCellStyle(CellDTO cellDTO,CellStyle cellType,Cell cell){
        //设置单元格背景色
        if(cellDTO.getFillBackgroundColor() != null){
            cellType.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellType.setFillForegroundColor(cellDTO.getFillBackgroundColor());
            cell.setCellStyle(cellType);
        }
    }
    /**
     * <p>@Description 读取csv文件的第1个sheet页数据，从第rowNum行读取返回,读取成功或失败后可选是否删除文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 21:52 </p>
     * @param filePath csv文件绝对路径
     * @param rowNum 开始读取的行数
     * @param isdelete 读取成功或失败后是否删除文件
     * @return 文件数据，二维数据，一维坐标行数据，二维坐标单元格数据
     */
    private static ArrayList<ArrayList<String>> readCsv(String filePath, int rowNum, boolean isdelete) {
        ArrayList<ArrayList<String>> arrList = new ArrayList<>();
        CsvReader reader = null;
        try {
            rowNum = rowNum < 0 ? 0 : rowNum;
            reader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            int index = 0;
            while (reader.readRecord()) {
                if(index >= rowNum){
                    ArrayList<String> vaList = (ArrayList<String>) Arrays.stream(reader.getValues()).collect(Collectors.toList());
                    arrList.add(vaList); // 按行读取，并把每一行的数据添加到list集合
                }else {
                    index++;
                }
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
        return arrList;
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
    private static ArrayList<ArrayList<String>> readExcel(String filePath, String password, int rowNum, boolean isdelete) {
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
}
