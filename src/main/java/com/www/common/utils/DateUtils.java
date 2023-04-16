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
     * <p>@Description 判断2个日期相差的天数，不区分 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/16 15:53 </p>
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return startDate大于或小于endDate返回正数天数，startDate等于endDate返回0天数，startDate和endDate其中任一为null返回-1
     */
    public static int getAbsDays(Date startDate, Date endDate) {
        if(startDate == null || endDate == null){
            return -1;
        }
        return Math.abs(getDays(startDate, endDate));
    }
    /**
     * <p>@Description 判断2个日期相差的天数 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/16 15:53 </p>
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return startDate小于endDate返回正数天数，startDate大于endDate返回负数天数，startDate等于endDate或其中任一为null返回0天数
     */
    public static int getDays(Date startDate, Date endDate) {
        if(startDate == null || endDate == null){
            return 0;
        }
        Calendar startCaledar = Calendar.getInstance();
        Calendar endClaendar = Calendar.getInstance();
        if(startDate.compareTo(endDate) == -1){
            startCaledar.setTime(startDate);
            endClaendar.setTime(endDate);
        }else if(startDate.compareTo(endDate) == 1){
            startCaledar.setTime(endDate);
            endClaendar.setTime(startDate);
        }else {
            return 0;
        }
        int days = endClaendar.get(Calendar.DAY_OF_YEAR)-startCaledar.get(Calendar.DAY_OF_YEAR);
        int y2 = endClaendar.get(Calendar.YEAR);
        if (startCaledar.get(Calendar.YEAR) != y2) {
            do {
                days += startCaledar.getActualMaximum(Calendar.DAY_OF_YEAR);
                startCaledar.add(Calendar.YEAR, 1);
            } while (startCaledar.get(Calendar.YEAR) != y2);
        }
        return startDate.compareTo(endDate) == -1 ? days : -1*days;
    }
    /**
     * <p>@Description 判断2个日期相差的月数 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/16 15:53 </p>
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return startDate大于或小于endDate返回正数月数，startDate等于endDate返回0天月数，startDate和endDate其中任一为null返回-1
     */
    public static int getAbsMonths(Date startDate, Date endDate) {
        if(startDate == null || endDate == null){
            return -1;
        }
        return Math.abs(getMonths(startDate, endDate));
    }
    /**
     * <p>@Description 判断两个日期相差几个月 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/23 16:17 </p>
     * @param startDate 起始日期
     * @param endDate 到期日期
     * @return startDate小于endDate返回正数月数，startDate大于endDate返回负数月数，startDate等于endDate或其中任一为null返回0月数
     */
    public static int getMonths(Date startDate, Date endDate) {
        if(startDate == null || endDate == null){
            return 0;
        }
        Calendar startCaledar = Calendar.getInstance();
        Calendar endClaendar = Calendar.getInstance();
        if(startDate.compareTo(endDate) == -1){
            startCaledar.setTime(startDate);
            endClaendar.setTime(endDate);
        }else if(startDate.compareTo(endDate) == 1){
            startCaledar.setTime(endDate);
            endClaendar.setTime(startDate);
        }else {
            return 0;
        }
        int months = endClaendar.get(Calendar.MONTH) - startCaledar.get(Calendar.MONTH);
        int y2 = endClaendar.get(Calendar.YEAR);
        if (startCaledar.get(Calendar.YEAR) != y2) {
            do {
                months += 12;
                startCaledar.add(Calendar.YEAR, 1);
            } while (startCaledar.get(Calendar.YEAR) != y2);
        }
        return startDate.compareTo(endDate) == -1 ? months : -1*months;
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
