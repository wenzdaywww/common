package com.www.common.utils;

import com.www.common.data.constant.CharConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>@Description 字符串工具类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/6/7 21:10 </p>
 */
public class MyStringUtils {
    /**
     * <p>@Description 字符串中的{}设置值，按顺序依次替换为valArr的值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/6/7 21:25 </p>
     * @param msg    待设置的字符串
     * @param valArr 待设置的值
     * @return 替换后的字符串
     */
    public static String format(String msg, Object... valArr) {
        if (StringUtils.isBlank(msg) || valArr == null || valArr.length == 0) {
            return msg;
        }
        StringBuilder sb = new StringBuilder(msg);
        for (int i = 0; i < valArr.length; i++) {
            int index = sb.indexOf(CharConstant.LEFT_BRACE + CharConstant.RIGHT_BRACE);
            if (index > 0) {
                String val = valArr[i] == null ? "" : valArr[i].toString();
                sb.replace(index, index + 2, val);
            }
        }
        return sb.toString();
    }
}