package com.www.common.utils;

import com.www.common.pojo.enums.DateFormatEnum;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>@Description 日期处理工具 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/4 15:07 </p>
 */
public class DateUtils {
    /**
     * <p>@Description 判断两个日期相差几个月 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/23 16:17 </p>
     * @param start 起始日期
     * @param end 到期日期
     * @return int -1为错误情况，其他为两日期的月份绝对值差
     */
    public static int getMonths(Date start, Date end) {
        if(start == null || end == null){
            return -1;
        }
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }
    /**
     * <p>@Description 获取当前系统日期时间 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:10 </p>
     * @return java.util.Date
     */
    public static Date getCurrentDateTime(){
        return new Date();
    }
    /**
     * <p>@Description 日期转换字符串 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/4 15:11 </p>
     * @param date
     * @param dateFormat
     * @return java.lang.String
     */
    public static String format(Date date, DateFormatEnum dateFormat){
        if(date == null || dateFormat == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());
        return sdf.format(date);
    }
    /**
     * <p>@Description 字符串转为日期 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/7 22:59 </p>
     * @param dateStr 字符串日期
     * @param dateFormat 日期格式
     * @return java.util.Date 日期
     */
    public static Date parse(String dateStr,DateFormatEnum dateFormat){
        if(StringUtils.isBlank(dateStr) || dateFormat == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
