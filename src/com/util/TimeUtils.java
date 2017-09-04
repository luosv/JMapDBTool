package com.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Administrator
 */
public class TimeUtils {

    private static final Logger log = LogManager.getLogger(TimeUtils.class);
    //设置的时间值
    private static long m_ServerBeginTime = 0;
    private static boolean isTimeGMSet = false;

    //当前时间值与系统时间的差值
    private static long m_BetweenNowAndSetTime = 0;

    //用ThreadLocal处理SimpleDateFormat非线程安全的问题/////////////////////////
    private static final ThreadLocal<DateFormat> sdfNoSecond = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };

    public static DateFormat getSdfNoSecond() {
        return sdfNoSecond.get();
    }
    ////////////////////////////////////////////////////////////////////////////

    public static void setServerBeginTime(long ServerBeginTime) {
        m_BetweenNowAndSetTime = ServerBeginTime - System.currentTimeMillis();
        m_ServerBeginTime = ServerBeginTime;
    }

    public static void setTime(long setTime) {
        m_BetweenNowAndSetTime = setTime - System.currentTimeMillis();
        isTimeGMSet = true;
    }

    //获得游戏的当前时间值
    public static boolean isTimeGMSet() {
        return isTimeGMSet;
    }

    /**
     * 获得自服务器启动以来的毫秒数
     *
     * @return
     */
    public static long Time() {
        return System.currentTimeMillis() + m_BetweenNowAndSetTime;
    }

    /**
     * 获取当天0时0分0秒 到现在流逝的时间
     *
     * @return
     */
    public static long getTodayPassMillis() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(Time());
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int min = instance.get(Calendar.MINUTE);
        int second = instance.get(Calendar.SECOND);
        int millis = instance.get(Calendar.MILLISECOND);

        return hour * 3600000 + min * 60000 + second * 1000 + millis;
    }

    /**
     * 获取某天开始时间
     *
     * @param timeStr
     * @return
     * @throws java.text.ParseException
     */
    public static long getBeginTime(String timeStr) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(getDateByString(timeStr));
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int date = instance.get(Calendar.DATE);
        instance.set(year, month, date, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    /**
     * 获取某天开始时间
     *
     * @param time
     * @return
     */
    public static long getBeginTime(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int date = instance.get(Calendar.DATE);
        instance.set(year, month, date, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    /**
     * 获取今天开始时间
     *
     * @return
     */
    public static long getTodayBeginTime() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int date = instance.get(Calendar.DATE);
        instance.set(year, month, date, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @return
     */
    public static long getFirstDayOfWeek() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.set(Calendar.DAY_OF_WEEK, instance.getFirstDayOfWeek());
        return instance.getTimeInMillis();
    }

    /**
     * 获取本周开始时间
     *
     * @return
     */
    public static long getCurWeekBeginTime() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTimeInMillis();
    }

    /**
     * 获取本周星期一是今年第几周
     *
     * @return
     */
    public static int getCurWeekOfYear() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        int year = instance.get(Calendar.YEAR);
        int weekno = instance.get(Calendar.WEEK_OF_YEAR);
        if (weekno == 1) {
            int moon = instance.get(Calendar.MONTH);
            if (moon > 1) {
                instance.set(Calendar.DAY_OF_MONTH, instance.get(Calendar.DAY_OF_MONTH) - 7);
                weekno = instance.get(Calendar.WEEK_OF_YEAR) + 1;
            }
        }
        return year * 100 + weekno;
    }

    //获取本月开始时间
    public static long getMonthBeginTime() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        instance.set(year, month, 1, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    //获取下个月开始时间
    public static long getNextMonthBeginTime() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        if (month >= 11) {
            year += 1;
            month = 0;
        } else {
            month += 1;
        }
        instance.set(year, month, 1, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    //获得格式化的时间值
    public static String NowToString() {
        long now = Time();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    //获得格式化的时间值
    public static String NowTGToString() {
        long now = Time();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获得格式化的时间值
     *
     * @param time
     * @return
     */
    public static String format2string(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String format3string(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }

    /**
     * 获得格式化的时间值
     *
     * @param time
     * @param format "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String format2string(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * netbeans/eclipse等IDE下 运行/调试 关闭时IDE内部调用Process.destroy() <br />
     * 无法触发到JVM shutdown hook, 导致关闭时无法回存 <br />
     * 故在IDE环境下添加了system property: ideDebug
     *
     * @return
     */
    public static boolean isIDEEnvironment() {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }

    /**
     * 判断两个时间是否在同一天
     *
     * @param time
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time, long time2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        int d1 = instance.get(Calendar.DAY_OF_YEAR);
        int y1 = instance.get(Calendar.YEAR);
        instance.setTimeInMillis(time2);
        int d2 = instance.get(Calendar.DAY_OF_YEAR);
        int y2 = instance.get(Calendar.YEAR);
        return d1 == d2 && y1 == y2;
    }

    /**
     * 判断两个时间是否在同一周
     *
     * @param time
     * @param time2
     * @return
     */
    public static boolean isSameWeek(long time, long time2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        int w1 = instance.get(Calendar.WEEK_OF_YEAR);
        int y1 = instance.get(Calendar.YEAR);
        instance.setTimeInMillis(time2);
        int w2 = instance.get(Calendar.WEEK_OF_YEAR);
        int y2 = instance.get(Calendar.YEAR);
        return w1 == w2 && y1 == y2;
    }

    /**
     * 判断两个时间是否在同一月
     *
     * @param time
     * @param time2
     * @return
     */
    public static boolean isSameMonth(long time, long time2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        int w1 = instance.get(Calendar.MONTH);
        int y1 = instance.get(Calendar.YEAR);
        instance.setTimeInMillis(time2);
        int w2 = instance.get(Calendar.MONTH);
        int y2 = instance.get(Calendar.YEAR);
        return w1 == w2 && y1 == y2;
    }

    /**
     * 获取某时间距离1970至戳的天数
     *
     * @param time
     * @return
     */
    public static int getDays(long time) {
        Calendar old = Calendar.getInstance();
        old.set(1970, 0, 1, 0, 0, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long intervalMilli = cal.getTimeInMillis() - old.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        return (int) xcts;
    }

    /**
     * 获取1970至今的天数 （计数会在在每天指定的小时+1，用来判断每天X点清数据之类的）
     *
     * @param hour 每天第X个小时+1
     * @return
     */
    public static int getCurDay(int hour) {
        TimeZone zone = TimeZone.getDefault();    //默认时区
        long s = Time() / 1000 - hour * 3600;
        if (zone.getRawOffset() != 0) {
            s = s + zone.getRawOffset() / 1000;
        }
        s = s / 86400; //86400 = 24 * 60 * 60 (一天时间的秒数)
        return (int) s;
    }

    /**
     * 指定时间的年份
     *
     * @param time
     * @return
     */
    public static int getYear(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.YEAR);
    }

    /**
     * 返回当前的周数
     *
     * @return
     */
    public static int getWeek() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(Time());
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return instance.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 指定时间的月份,0-11
     *
     * @param time
     * @return
     */
    public static int getMonth(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.MONTH);
    }

    /**
     * 获取日期(一个月内的第几天)
     *
     * @param time
     * @return
     */
    public static int getDayOfMonth(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取第几周（一年中的第几周）
     *
     * @param time
     * @return
     */
    public static int getWeekOfYear(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        instance.setTimeInMillis(time);
        return instance.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取小时
     *
     * @param time
     * @return
     */
    public static int getDayOfHour(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     *
     * @param time
     * @return
     */
    public static int getDayOfMin(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     *
     * @param time
     * @return
     */
    public static int getDayOfSecond(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.SECOND);
    }

    /**
     * 获取指定时间 是一月内的第几周
     *
     * @param time
     * @return
     */
    public static int getDayOfWeekInMonth(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        instance.setTimeInMillis(time);
        return instance.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获取星期几
     *
     * @param time
     * @return
     */
    public static int getDayOfWeek(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        int i = instance.get(Calendar.DAY_OF_WEEK);
        if (i == 1) {
            return 7;
        } else {
            i -= 1;
        }
        return i;
    }

    /**
     * 获取一年内的第几天
     *
     * @param time
     * @return
     */
    public static int getDayOfYear(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 字符串转日期("yyyy-MM-dd HH:mm:ss");
     *
     * @param date
     * @return
     * @throws java.text.ParseException
     */
    public static Date getDateByString(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(date);
    }

    /**
     * 判断两个时间中间所差天数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int getBetweenDays(long time1, long time2) {
        Calendar instance1 = Calendar.getInstance();
        instance1.setTimeInMillis(time1);
        instance1.set(Calendar.HOUR_OF_DAY, 0);
        instance1.set(Calendar.MINUTE, 0);
        instance1.set(Calendar.SECOND, 0);
        instance1.set(Calendar.MILLISECOND, 0);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(time2);
        instance2.set(Calendar.HOUR_OF_DAY, 0);
        instance2.set(Calendar.MINUTE, 0);
        instance2.set(Calendar.SECOND, 0);
        instance2.set(Calendar.MILLISECOND, 0);
        return (int) ((instance1.getTimeInMillis() - instance2.getTimeInMillis()) / (24 * 60 * 60 * 1000));
    }

    /**
     * 获取1970至今的时间, 1获取秒，2 分钟，3小时，4天数,5周数
     *
     * @param x
     * @param time
     * @return
     */
    public static long GetCurTimeInMin(int x, long time) {
        TimeZone zone = TimeZone.getDefault();    //默认时区
        long s = time / 1000;
        if (zone.getRawOffset() != 0) {
            s = s + zone.getRawOffset() / 1000;
        }
        switch (x) {
            case 1:
                break;
            case 2:
                s = s / 60;
                break;
            case 3:
                s = s / 3600;
                break;
            case 4:
                s = s / 86400;
                break;
            case 5:
                s = s / 86400 + 3;// 补足天数，星期1到7算一周
                s = s / 7;
                break;
            default:
                break;
        }
        return s;
    }

    /**
     * 指定小时与分，秒，计算与当前时间的差值， 不跨天
     *
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static int getDecNowToTime(int hour, int min, int sec) {
        long now = Time();
        Calendar instance1 = Calendar.getInstance();
        instance1.setTimeInMillis(now);
        instance1.set(Calendar.HOUR_OF_DAY, hour);
        instance1.set(Calendar.MINUTE, min);
        instance1.set(Calendar.SECOND, sec);
        instance1.set(Calendar.MILLISECOND, 0);
        int res = (int) ((instance1.getTimeInMillis() - now) / 1000);
        return res > 0 ? res : 0;
    }

    /**
     * 指定小时与分，秒，返回当天指定的小时分秒的当前时间值
     *
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static long getToTime(int hour, int min, int sec) {
        long now = Time();
        Calendar instance1 = Calendar.getInstance();
        instance1.setTimeInMillis(now);
        instance1.set(Calendar.HOUR_OF_DAY, hour);
        instance1.set(Calendar.MINUTE, min);
        instance1.set(Calendar.SECOND, sec);
        instance1.set(Calendar.MILLISECOND, 0);
        return instance1.getTimeInMillis();
    }

    /**
     * 返回离下一个星期几还剩的秒值
     *
     * @param weekday
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static int getDecNowToTime(int weekday, int hour, int min, int sec) {
        long now = Time();

        int wk = getDayOfWeek(now);
        Calendar instance1 = Calendar.getInstance();
        instance1.setTimeInMillis(now);

        if (wk < weekday) {
            //本周
            instance1.add(Calendar.DAY_OF_MONTH, weekday - wk);
        } else if (wk > weekday) {
            //下一周
            instance1.add(Calendar.DAY_OF_MONTH, 7 - wk + weekday);
        } else {
            //当天
            int time = getDecNowToTime(hour, min, sec);
            if (time == 0)//等于0 表示下一周去了
            {
                instance1.add(Calendar.DAY_OF_MONTH, 7 - wk + weekday);
            } else {
                return time;//不是就返回当前时间
            }
        }

        instance1.set(Calendar.HOUR_OF_DAY, hour);
        instance1.set(Calendar.MINUTE, min);
        instance1.set(Calendar.SECOND, sec);
        instance1.set(Calendar.MILLISECOND, 0);
        int res = (int) ((instance1.getTimeInMillis() - now) / 1000);
        return res > 0 ? res : 0;
    }

    /**
     * 根据时间表达式 返回long
     *
     * @param exp 时间表达式<br />
     *            格式：年-月-日-星期-时-分
     * @return time
     */
    public static long getTime(String exp) {
        return expDate(exp);
    }

    /**
     * 根据表达式计算开始时间，时间以Date格式标识
     *
     * @param exp 时间表达式<br />
     *            格式：年-月-日-星期-时-分
     * @return time
     */
    public static long expDate(String exp) {
        long time;
        try {
            String[] timer = exp.split("-");
            // Get the User provided Time
            Calendar userCal = Calendar.getInstance();
            // Get System Calendar Date
            userCal.setTimeInMillis(Time());
            Calendar sys = Calendar.getInstance();
            sys.setTimeInMillis(Time());
            if ("*".equals(timer[0]))
                userCal.set(Calendar.YEAR, sys.get(java.util.Calendar.YEAR));
            else
                userCal.set(Calendar.YEAR, Integer.parseInt(timer[0]));
            if ("*".equals(timer[1]))
                userCal.set(Calendar.MONTH, sys.get(java.util.Calendar.MONTH));
            else
                userCal.set(Calendar.MONTH, Integer.parseInt(timer[1]));
            if ("*".equals(timer[2]))
                userCal.set(Calendar.DAY_OF_MONTH, sys.get(Calendar.DAY_OF_MONTH));
            else
                userCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timer[2]));
            if ("*".equals(timer[3]))
                userCal.set(Calendar.DAY_OF_WEEK, sys.get(java.util.Calendar.DAY_OF_WEEK));
            else {
                userCal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(timer[3]) + 1);
            }

            if ("*".equals(timer[4]))
                userCal.set(Calendar.HOUR_OF_DAY, sys.get(java.util.Calendar.HOUR_OF_DAY));
            else
                userCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timer[4]));
            if ("*".equals(timer[5]))
                userCal.set(Calendar.MINUTE, sys.get(java.util.Calendar.MINUTE));
            else
                userCal.set(Calendar.MINUTE, Integer.parseInt(timer[5]));
            userCal.set(Calendar.SECOND, 0);
            userCal.set(Calendar.MILLISECOND, 0);
            time = userCal.getTimeInMillis();
        } catch (Exception ex) {
            ex.printStackTrace();
            time = new Date().getTime();
        }
        return time;
    }

}
