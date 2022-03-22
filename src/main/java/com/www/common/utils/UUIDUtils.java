package com.www.common.utils;

import com.www.common.pojo.enums.DateFormatEnum;

import java.util.UUID;

/**
 * <p>@Description 唯一ID工具类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/31 22:57 </p>
 */
public class UUIDUtils {
    /** 16进制数组 **/
    private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    /**
     * <p>@Description 获取日志全局跟踪号 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/31 23:01 </p>
     * @return java.lang.String
     */
    public static String getTraceId(){
        return DateUtils.format(DateUtils.getCurrentDateTime(), DateFormatEnum.YYYYMMDDHHMMSSSSS) + "-" + generateUuid8();
    }
    /**
     * <p>@Description 生成8位UUId </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/31 23:03 </p>
     * @return java.lang.String
     */
    public static String generateUuid8() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
