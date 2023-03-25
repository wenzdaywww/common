package com.www.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>@Description BigDecimal金额工具类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/16 23:22 </p>
 */
public class MoneyUtils {
    /**
     * <p>@Description 金额处理，为空则返回0，其他则保留2位小数 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 23:23 </p>
     * @param money
     * @return java.math.BigDecimal
     */
    public static BigDecimal nullToZero(BigDecimal money){
        return money == null ? BigDecimal.ZERO : money.setScale(2, RoundingMode.HALF_UP);
    }
    /**
     * <p>@Description 字符串金额转为金额处理，为空则返回0，其他则保留2位小数 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 23:23 </p>
     * @param money 字符串金额
     * @return java.math.BigDecimal 金额
     */
    public static BigDecimal strToAmt(String money){
        BigDecimal amt = null;
        try {
            amt = new BigDecimal(money);
            amt = amt.setScale(2, RoundingMode.HALF_UP);
        }catch (Exception e){
            amt = BigDecimal.ZERO;
        }
        return amt;
    }
}
