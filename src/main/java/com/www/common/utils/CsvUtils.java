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

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>@Description csv文件工具类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/26 15:28 </p>
 */
@Slf4j
public class CsvUtils {
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
     * <p>@Description 读取csv文件数据，同时更新对应单元格数据后转为excel文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 22:28 </p>
     * @param filePath excel文件绝对路径
     * @param dataMap 待写入的数据，为空则只复制csv数据。格式：Map<行号,Map<列号,单元格数据>>,如果行号大于原csv数据行数，则依次插入新行，但列号则固定，即插入第几列不变
     * @return excel文件绝对路径
     */
    public static String csvToExcel(String filePath,Map<Integer, Map<Integer,CellDTO>> dataMap){
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
        boolean isOk = ExcelUtils.writeCoverExcel(excelName,excelList);
        return isOk ? excelName : null;
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
    public static ArrayList<ArrayList<String>> readCsv(String filePath, int rowNum, boolean isdelete) {
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
}
