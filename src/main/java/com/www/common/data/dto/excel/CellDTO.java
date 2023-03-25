package com.www.common.data.dto.excel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * <p>@Description excel的单元格类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/25 19:29 </p>
 */
@Data
@Accessors(chain = true)
public class CellDTO {
    /** 列坐标，必填 **/
    private int cellIndex;
    /** 单元格值，必填 **/
    private String cellValue;
    /** 单元格背景填充色 **/
    private Short fillBackgroundColor;
    /**
     * <p>@Description 设置单元格背景填充色 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 22:25 </p>
     * @param color 颜色值
     * @return
     */
    public CellDTO setFillBackgroundColor(IndexedColors color) {
        this.fillBackgroundColor = color.getIndex();
        return this;
    }
    /**
     * <p>@Description 设置单元格背景填充色 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 22:25 </p>
     * @param color 颜色值
     * @return
     */
    public CellDTO setFillBackgroundColor(Short color) {
        this.fillBackgroundColor = color;
        return this;
    }
}
