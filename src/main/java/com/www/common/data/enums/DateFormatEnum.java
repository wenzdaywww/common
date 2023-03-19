package com.www.common.data.enums;

/**
 * <p>@Description 日期格式 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 10:29 </p>
 */
public enum DateFormatEnum {
    /**  显示年月日时分秒，例如 2020-01-01 20:00:00 **/
    YYYYMMDDHHMMSS1("yyyy-MM-dd HH:mm:ss"),
    /** 显示年月日时分秒，例如 2020/01/01 20:00:00 **/
    YYYYMMDDHHMMSS2("yyyy/MM/dd HH:mm:ss"),
    /** 显示年月日时分秒，例如 2020年01月01日 20:00:00 **/
    YYYYMMDDHHMMSS3("yyyy年MM月dd日 HH:mm:ss"),
    /** 显示年月日时分秒，例如 2020.01.01 20:00:00 **/
    YYYYMMDDHHMMSS4("yyyy.MM.dd HH:mm:ss"),
    /** 显示年月日时分秒，例如 20200101200101 **/
    YYYYMMDDHHMMSS5("yyyyMMddHHmmss"),

    /** 显示年月日时分秒毫秒，例如 20200101200102999 **/
    YYYYMMDDHHMMSSSSS("yyyyMMddHHmmssSSS"),

    /**  显示年月日时分秒，例如 2020-1-1 20:00:00 **/
    YYYYMDHHMMSS1("yyyy-M-d HH:mm:ss"),
    /** 显示年月日时分秒，例如 2020/1/1 20:00:00 **/
    YYYYMDHHMMSS2("yyyy/M/d HH:mm:ss"),
    /** 显示年月日时分秒，例如 2020年1月1日 20:00:00 **/
    YYYYMDHHMMSS3("yyyy年M月d日 HH:mm:ss"),
    /** 显示年月日时分秒，例如 2020.1.1 20:00:00 **/
    YYYYMDHHMMSS4("yyyy.M.d HH:mm:ss"),
    /** 显示年月日时分秒，例如 202011200102 **/
    YYYYMDHHMMSS5("yyyyMdHHmmss"),

    /** 显示年月日，例如 2020-01-01 **/
    YYYYMMDD1("yyyy-MM-dd"),
    /** 显示年月日，例如 2020/01/01 **/
    YYYYMMDD2("yyyy/MM/dd"),
    /** 显示年月日，例如 2020年01月01日 **/
    YYYYMMDD3("yyyy年MM月dd日"),
    /** 显示年月日，例如 2015.01.11 **/
    YYYYMMDD4("yyyy.MM.dd"),
    /** 显示年月日，例如 20200101 **/
    YYYYMMDD5("yyyyMMdd"),

    /** 显示年月日，例如 2020-01-01 **/
    YYYYMD1("yyyy-M-d"),
    /** 显示年月日，例如 2020/01/01 **/
    YYYYMD2("yyyy/M/d"),
    /** 显示年月日，例如 2020年01月01日 **/
    YYYYMD3("yyyy年M月d日"),
    /** 显示年月日，例如 2015.01.11 **/
    YYYYMD4("yyyy.M.d"),
    /** 显示年月日，例如 20200101 **/
    YYYYMD5("yyyyMd"),

    /** 显示年月，例如 2020-01 **/
    YYYYMM1("yyyy-MM"),
    /** 显示年月，例如 2020/01 **/
    YYYYMM2("yyyy/MM"),
    /** 显示年月，例如 2020年01月 **/
    YYYYMM3("yyyy年MM月"),
    /** 显示年月，例如 2020.01 **/
    YYYYMM4("yyyy.MM"),
    /** 显示年月，例如 2020年01月 **/
    YYYYMM5("yyyyMM"),

    /** 显示年月，例如 2020-1 **/
    YYYYM1("yyyy-M"),
    /** 显示年月，例如 2020/1 **/
    YYYYM2("yyyy/M"),
    /** 显示年月，例如 2020年1月 **/
    YYYYM3("yyyy年M月"),
    /** 显示年月，例如 202015.1 **/
    YYYYM4("yyyy.M"),
    /** 显示年月，例如 2020年1月 **/
    YYYYM5("yyyyM"),

    /** 显示年，例如 2020 **/
    YYYY1("yyyy"),
    /** 显示年，例如 2020年 **/
    YYYY2("yyyy年"),

    /** 显示月日，例如 01-01 **/
    MMDD1("MM-dd"),
    /** 显示月日，例如 01/01 **/
    MMDD2("MM/dd"),
    /** 显示月日，例如 01月01日 **/
    MMDD3("MM月dd日"),
    /** 显示月日，例如 01.01 **/
    MMDD4("MM.dd"),
    /** 显示月日，例如 0101 **/
    MMDD5("MMdd"),

    /** 显示月日，例如 1-1 **/
    MD1("MM-dd"),
    /** 显示月日，例如 1/1 **/
    MD2("MM/dd"),
    /** 显示月日，例如 1月1日 **/
    MD3("MM月dd日"),
    /** 显示月日，例如 1.1 **/
    MD4("MM.dd"),
    /** 显示月日，例如 11 **/
    MD5("MMdd"),

    /** 显示月，例如 01 **/
    MM1("MM"),
    /** 显示月，例如 01月 **/
    MM2("MM月"),

    /** 显示月，例如 1 **/
    M1("M"),
    /** 显示月，例如 1月 **/
    M2("M月"),

    /** 显示日，例如 01 **/
    DD1("dd"),
    /** 显示日，例如 01日 **/
    DD2("dd日"),

    /** 显示日，例如 1 **/
    D1("d"),
    /** 显示日，例如 1日 **/
    D2("d日"),

    /** 显示时分秒，例如 20:01:01 **/
    HHMMSS("HH:mm:ss"),

    ;
    /** 格式 **/
    private String format;

    DateFormatEnum(String format){
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
