package com.www.common.utils;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * <p>@Description 数字处理工具栏 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/2/7 22:06 </p>
 */
public class NumberUtils {
    /** 数字格式 **/
    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    /**
     * <p>@Description 设置数据精度 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/7 22:09 </p>
     * @param value 需要处理的数据，必须是基本数字数据类型
     * @param maximum 设置小数位数
     * @param roundingMo 舍入形式
     * @return java.lang.String 处理后的数据
     */
    public static String format(Object value,int maximum,RoundingMode roundingMo){
        // 保留几位小数
        numberFormat.setMaximumFractionDigits(maximum);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        numberFormat.setRoundingMode(roundingMo);
        return numberFormat.format(value);
    }
    /**
     * <p>@Description 保留数字为3为小数 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/7 22:09 </p>
     * @param value 需要处理的数据，必须是基本数字数据类型
     * @return java.lang.String 处理后的数据
     */
    public static String format3(Object value){
        return format(value,3,RoundingMode.UP);
    }

}
