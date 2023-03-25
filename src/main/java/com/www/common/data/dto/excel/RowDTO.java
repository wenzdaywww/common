package com.www.common.data.dto.excel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description excel的行数据类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/25 19:29 </p>
 */
@Data
@Accessors(chain = true)
public class RowDTO {
    /** 行坐标，必填 **/
    private int rowIndex;
    /** 一行数据的单元格对象集合 **/
    private List<CellDTO> cellList;
    /**
     * <p>@Description 添加单元格数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 19:50 </p>
     * @param cell 单元格行数据
     * @return 行数据对象
     */
    public RowDTO add(CellDTO cell){
        if(CollectionUtils.isEmpty(cellList)){
            cellList = new ArrayList<>();
        }
        if(cell != null){
            cellList.add(cell);
        }
        return this;
    }
}
