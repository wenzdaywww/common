package com.www.common.utils;

import com.www.common.config.redis.RedisOperation;
import com.www.common.data.enums.DateFormatEnum;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.util.Date;

/**
 * <p>@Description 分布式ID生成工具 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/4/14 22:23 </p>
 */
public class UidGeneratorUtils {
    /**
     * <p>@Description 获取redis的全局ID,随机增加至少step值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 04:06 </p>
     * @param uidKey 全局IDKey
     * @param start id起始值
     * @param step id步进值
     * @return
     */
    public static long getRedisUid(String uidKey,Long start,int step){
        int number = (int)(Math.random()*10+step); //生成的随机数范围在[step,step+10)
        if(!RedisOperation.hasKey(uidKey)){
            RedisOperation.set(uidKey,start);
        }
        return RedisOperation.hashIncrement(uidKey,number);
    }
    /**
     * <p>@Description 获取redis的全局ID,随机增加至少step值，默认起始值1000 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 04:06 </p>
     * @param uidKey 全局IDKey
     * @param step id步进值
     * @return
     */
    public static long getRedisUid(String uidKey,int step){
        return UidGeneratorUtils.getRedisUid(uidKey,1000L,step);
    }
    /**
     * <p>@Description 获取雪花算法（SnowFlake）实现的ID
     * 默认时间戳：2020-01-01 00:00:00
     * </p>
     * <p>@Author www </p>
     * <p>@Date 2022/4/14 22:28 </p>
     * @return long
     */
    public static long getSnowFlakeID(){
        return SnowFlakeID.nextId();
    }
    /**
     * <p>@Description 获取8位UUID </p>
     * <p>@Author www </p>
     * <p>@Date 2022/4/14 22:32 </p>
     * @return java.lang.String 8位UUID
     */
    public static String get8Uuid(){
        return UUID.generate8Uuid();
    }
    /**
     * <p>@Description 使用雪花算法获取日志全局跟踪号 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/31 23:01 </p>
     * @return java.lang.String
     */
    public static String getTraceId(){
        return Long.toString(getSnowFlakeID());
    }
    /**
     * <p>@Description 设置雪花算法的时间戳 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/4/14 22:35 </p>
     * @param date 时间戳
     */
    public static void setSnowFlakeIDTimestamp(Date date){
        SnowFlakeID.setStartTimestamp(date);
    }
    /**
     * <p>@Description 基于雪花算法（SnowFlake）实现的ID
     * 雪花算法ID结构: 1bit不使用 + 41bit时间戳 + 10bit工作机器ID + 12bit序列号
     * </p>
     * <p>@Version 1.0 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/4/14 21:24 </p>
     */
    private static class SnowFlakeID {
        /** 起始的时间戳,取当前时间*/
        private static long START_TIMESTAMP = DateUtils.parse("2016-11-04 13:14:52",DateFormatEnum.YYYY_MM_DD_HH_MM_SS).getTime();
        /** 数据中心占用的位数 */
        private final static long DATACENTER_ID_BIT = 5;
        /** 工作机器标识占用的位数 */
        private final static long WORKER_ID_BIT = 5;
        /** 序列号占用的位数 */
        private final static long SEQUENCE_BIT = 12;
        /** 支持的序列号最大值 */
        private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
        /** 工作机器标识向左移12位 */
        private final static long WORKER_ID_LEFT = SEQUENCE_BIT;
        /** 数据中心向左移(12+5)位 */
        private final static long DATACENTER_ID_LEFT = SEQUENCE_BIT + WORKER_ID_BIT;
        /** 时间戳向左移(12+5+5)位 */
        private final static long TIMESTAMP_LEFT = DATACENTER_ID_LEFT + DATACENTER_ID_BIT;
        /** 数据中心 */
        private static long dataCenterId = getDataCenterId();
        /** 工作机器标识 */
        private static long workerId = getWorkId();
        /** 序列号 */
        private static long sequence = 0L;
        /** 上一次时间戳 */
        private static long lastTimestamp = -1L;

        /**
         * <p>@Description 获取下一个ID (该方法是线程安全的) </p>
         * <p>@Author www </p>
         * <p>@Date 2022/4/14 21:28 </p>
         * @return long 下一个ID
         */
        public synchronized static long nextId() {
            long currentTimestamp = currentTimeMillis();//返回以毫秒为单位的当前时间
            //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (currentTimestamp < lastTimestamp) {
                throw new RuntimeException("时钟向后移动。拒绝生成id");
            }
            //如果是同一时间生成的，则进行毫秒内序列
            if (currentTimestamp == lastTimestamp) {
                //相同毫秒内，序列号自增
                sequence = (sequence + 1) & MAX_SEQUENCE;
                //同一毫秒的序列数已经达到最大
                if (sequence == 0L) {
                    currentTimestamp = nextTimeMillis();
                }
            } else {
                //不同毫秒内，序列号置为0
                sequence = 0L;
            }
            //上次生成ID的时间戳
            lastTimestamp = currentTimestamp;
            //移位并通过或运算拼到一起组成64位的ID
            //时间戳部分
            return (currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT
                    //数据中心部分
                    | dataCenterId << DATACENTER_ID_LEFT
                    //工作机器标识部分
                    | workerId << WORKER_ID_LEFT
                    //序列号部分
                    | sequence;
        }
        /**
         * <p>@Description 阻塞到下一个毫秒，直到获得新的时间戳 </p>
         * <p>@Author www </p>
         * <p>@Date 2022/4/14 21:28 </p>
         * @return long 当前时间戳
         */
        private static long nextTimeMillis() {
            long mill = currentTimeMillis();
            while (mill <= lastTimestamp) {
                mill = currentTimeMillis();
            }
            return mill;
        }
        /**
         * <p>@Description 返回以毫秒为单位的当前时间 </p>
         * <p>@Author www </p>
         * <p>@Date 2022/4/14 21:28 </p>
         * @return long 当前时间(毫秒)
         */
        private static long currentTimeMillis() {
            return System.currentTimeMillis();
        }
        /**
         * <p>@Description 根据IP地址获取工作机器ID </p>
         * <p>@Author www </p>
         * <p>@Date 2022/4/14 22:10 </p>
         * @return java.lang.Long 工作机器ID
         */
        private static Long getWorkId(){
            try {
                String hostAddress = Inet4Address.getLocalHost().getHostAddress();
                int[] ints = StringUtils.toCodePoints(hostAddress);
                int sums = 0;
                for(int b : ints){
                    sums += b;
                }
                return (long)(sums % 32);
            } catch (Exception e) {
                // 如果获取失败，则使用随机数备用
                return RandomUtils.nextLong(0,31);
            }
        }
        /**
         * <p>@Description 根据HostName获取数据中心ID </p>
         * <p>@Author www </p>
         * <p>@Date 2022/4/14 22:11 </p>
         * @return java.lang.Long 数据中心ID
         */
        private static Long getDataCenterId(){
            int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
            int sums = 0;
            for (int i: ints) {
                sums += i;
            }
            return (long)(sums % 32);
        }
        /**
         * <p>@Description 设置起始时间戳 </p>
         * <p>@Author www </p>
         * <p>@Date 2022/4/14 22:20 </p>
         * @param date 时间
         */
        public static void setStartTimestamp(Date date){
            START_TIMESTAMP = date != null ? date.getTime() : START_TIMESTAMP;
        }
    }
    /**
     * <p>@Description 基于UUID生成的ID </p>
     * <p>@Version 1.0 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/4/14 22:25 </p>
     */
    private static class UUID{
        /** 16进制数组 **/
        private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
                "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z" };
        /**
         * <p>@Description 生成8位UUId </p>
         * <p>@Author www </p>
         * <p>@Date 2021/12/31 23:03 </p>
         * @return java.lang.String
         */
        public static String generate8Uuid() {
            StringBuffer shortBuffer = new StringBuffer();
            String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
            for (int i = 0; i < 8; i++) {
                String str = uuid.substring(i * 4, i * 4 + 4);
                int x = Integer.parseInt(str, 16);
                shortBuffer.append(chars[x % 0x3E]);
            }
            return shortBuffer.toString();
        }
    }
}
