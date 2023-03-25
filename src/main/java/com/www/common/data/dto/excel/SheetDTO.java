package com.www.common.data.dto.excel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description excel的sheet类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/25 19:29 </p>
 */
@Data
@Accessors(chain = true)
public class SheetDTO {
    /** sheet页坐标，必填 **/
    private int sheetIndex;
    /** sheet页名称，必填 **/
    private String sheetName;
    /** 行数据集合 **/
    private List<RowDTO> rowList;
    /**
     * <p>@Description 添加行数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 19:50 </p>
     * @param row 行数据
     * @return sheet页对象
     */
    public SheetDTO add(RowDTO row){
        if(CollectionUtils.isEmpty(rowList)){
            rowList = new ArrayList<>();
        }
        if(row != null){
            rowList.add(row);
        }
        return this;
    }
}
