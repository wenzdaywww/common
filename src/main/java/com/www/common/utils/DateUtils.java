package com.www.common.utils;

import com.www.common.data.enums.DateFormatEnum;
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
     * <p>@Description 获取日期所在月份的第一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:18 </p>
     * @param date 日期
     * @return 日期所在月份的第一天
     */
    public static Date monthFirstDay(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int firstDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);//获取某月最小天数
        c.set(Calendar.DAY_OF_MONTH,firstDay);//设置日历中月份的最小天数
        return c.getTime();
    }
    /**
     * <p>@Description 获取日期所在月份的第一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:19 </p>
     * @param date 日期
     * @param formatEnum 日期格式
     * @return 日期所在月份的第一天
     */
    public static String monthFirstDay(Date date,DateFormatEnum formatEnum){
        Date firstDate = monthFirstDay(date);
        return format(firstDate,formatEnum);
    }
    /**
     * <p>@Description 获取日期所在月份的最后一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:18 </p>
     * @param date 日期
     * @return 日期所在月份的第一天
     */
    public static Date monthLastDay(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);//获取某月最大天数
        c.set(Calendar.DAY_OF_MONTH,lastDay);//设置日历中月份的最大天数
        return c.getTime();
    }
    /**
     * <p>@Description 获取日期所在月份的最后一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:19 </p>
     * @param date 日期
     * @param formatEnum 日期格式
     * @return 日期所在月份的第一天
     */
    public static String monthLastDay(Date date,DateFormatEnum formatEnum){
        Date firstDate = monthLastDay(date);
        return format(firstDate,formatEnum);
    }
    /**
     * <p>@Description 获取日期所在年份的第一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:18 </p>
     * @param date 日期
     * @return 日期所在年份的第一天
     */
    public static Date yearFirstDay(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int lastDay = c.getActualMinimum(Calendar.DAY_OF_YEAR);//获取某年最小天数
        c.set(Calendar.DAY_OF_YEAR,lastDay);//设置日历中年份的最小天数
        return c.getTime();
    }
    /**
     * <p>@Description 获取日期所在年份的第一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:19 </p>
     * @param date 日期
     * @param formatEnum 日期格式
     * @return 日期所在年份的第一天
     */
    public static String yearFirstDay(Date date,DateFormatEnum formatEnum){
        Date firstDate = yearFirstDay(date);
        return format(firstDate,formatEnum);
    }
    /**
     * <p>@Description 获取日期所在年份的最后一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:18 </p>
     * @param date 日期
     * @return 日期所在年份的最后一天
     */
    public static Date yearLastDay(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_YEAR);//获取某年最大天数
        c.set(Calendar.DAY_OF_YEAR,lastDay);//设置日历中年份的最大天数
        return c.getTime();
    }
    /**
     * <p>@Description 获取日期所在年份的最后一天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/14 22:19 </p>
     * @param date 日期
     * @param formatEnum 日期格式
     * @return 日期所在年份的最后一天
     */
    public static String yearLastDay(Date date,DateFormatEnum formatEnum){
        Date firstDate = yearLastDay(date);
        return format(firstDate,formatEnum);
    }
    /**
     * 在给定的日期加上或减去指定格式后的日期
     * @param sourceDate 原始时间
     * @param calendar   要调整的格式，Calendar类的属性
     * @param num      要调整的数值，向前为负数，向后为正数
     * @return
     */
    public static Date stepDate(Date sourceDate, int calendar,int num) {
        if(sourceDate == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(calendar, num);
        return c.getTime();
    }
    /**
     * 在给定的日期加上或减去指定月份后的日期
     * @param sourceDate 原始时间
     * @param month      要调整的月份，向前为负数，向后为正数
     * @return
     */
    public static Date stepMonth(Date sourceDate, int month) {
        if(sourceDate == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }
    /**
     * 在给定的时间加上或减去指定小时
     * @param sourceDate 原始时间
     * @param hour      要调整的小时，向前为负数，向后为正数
     * @return
     */
    public static Date stepHour(Date sourceDate, int hour) {
        if(sourceDate == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }
    /**
     * 在给定的日期加上或减去指定天数后的日期
     * @param sourceDate 原始时间
     * @param day      要调整的天数，向前为负数，向后为正数
     * @return
     */
    public static Date stepDay(Date sourceDate, int day) {
        if(sourceDate == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }
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
