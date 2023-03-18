package com.www.common.data.enums;

/**
 * <p>@Description 日期格式 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 10:29 </p>
 */
public enum DateFormatEnum {
    /** 仅显示时分秒，例如 09:51:53 **/
    HHMMSS("HH:mm:ss"),
    /** 仅显示年月日(无符号)，例如 20150811 **/
    YYYYMMDD("yyyyMMdd"),
    /** 仅显示年，例如 2015 **/
    YYYY("yyyy"),
    /** 仅显示月日，例如 2015 **/
    MM_DD("MM-dd"),
    /** 仅显示月，例如 01 **/
    MM("MM"),
    /** 仅显示日，例如 31 **/
    DD("dd"),
    /** 仅显示年月日，例如 2015-08-11 **/
    YYYY_MM_DD("yyyy-MM-dd"),
    /**  显示年月日时分秒，例如 2015-08-11 09:51:53 **/
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    /** 显示年月日时分秒(无符号)，例如 20150811095153 **/
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    /** 显示年月日时分秒毫秒(无符号)，例如 20150811095153 **/
    YYYYMMDDHHMMSSSSS("yyyyMMddHHmmssSSS");
    /** 格式 **/
    private String format;

    DateFormatEnum(String format){
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
