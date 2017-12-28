package com.test.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with: lemon.
 * Date: 2016/9/16  23:03
 * Description:
 */
public class PubMethod {

    /**
     * Created with: lemon.
     * Date: 2016/9/25  18:41
     * Description: 各种非空判断
     */
    public static boolean isEmpty(String Value) {

        return (Value == null || Value.trim().equals(""));
    }

    public static boolean isEmpty(StringBuffer Value) {

        return (Value == null || (Value.toString().trim()).equals(""));
    }

    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Set set) {
        if (set == null || set.size() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Map map) {
        if (map == null || map.size() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Object Value) {
        if (Value == null)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Double value) {
        if (value == null || value.doubleValue() == 0.0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Long obj) {
        if (obj == null || obj.longValue() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Object[] Value) {
        if (Value == null || Value.length == 0)
            return true;
        else
            return false;
    }

    /**
     * Created with: lemon.
     * Date: 2016/9/25  19:11
     * Description: str to date
     */
    public static Date strToDate(String strDate, String strFormat) {
        if (isEmpty(strFormat)) {
            strFormat = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {

        }
        return date;
    }

    /**
     * Created with: lemon.
     * Date: 2016/10/25  14:43
     * Description: date to str
     */
    public static String dateToStr(Date date, String formatStr) {
        if (PubMethod.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String formatDate = sdf.format(date);
        return formatDate;
    }

    /**
     * Created with: lemon.
     * Date: 2016/9/25  17:43
     * Description: 获取某天时间几天后的字符串
     */
    public static String getDateStr(String dateStr, String formatStr, int day) {
        if (PubMethod.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd";
        }
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String formatDate = sdf.format(calendar.getTime());
        return formatDate;
    }

    /**
     * Created with lemon
     * Time: 2017/7/17 14:27
     * Description: 获取 某天时间 n 个月之前的时间
     */
    public static Date getMonthBefor(Date date, int month) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
            return sdf.parse(sdf.format(calendar.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Created with lemon
     * Time: 2017/7/28 10:44
     * Description: 格式化日期格式 string——>string
     */

    public static String dateStrFormat(String dateStr, String oldPattern, String formatStr) {

        if (PubMethod.isEmpty(oldPattern)) {
            oldPattern = "yyyy-MM-dd";
        }
        if (PubMethod.isEmpty(formatStr)) {
            formatStr = "yyyyMMdd";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date d = null;
        try {
            d = sdf1.parse(dateStr);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
        }
        return sdf.format(d);
    }

    public static Date dateAddNum(Date oldDate, int addYear, int addMonth, int addDay) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(oldDate);
        rightNow.add(Calendar.YEAR, addYear);
        rightNow.add(Calendar.MONTH, addMonth);
        rightNow.add(Calendar.DAY_OF_MONTH, addDay);
        Date newDate = rightNow.getTime();
        return newDate;
    }

    /**
     * @Author: lemon
     * @Time: 2017/9/20 10:43
     * @Describe: 两个日期计算间隔天数
     * <p>
     * fDate   减数
     * oDate   被减数
     */
    public static int daysBetween(Date fDate, Date oDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fDate);
        long fDateTime = cal.getTimeInMillis();
        cal.setTime(oDate);
        long oDateTime = cal.getTimeInMillis();
        Long intervalDays = (oDateTime - fDateTime) / (1000 * 3600 * 24);
        return intervalDays.intValue();
    }

    /**
     * Created with lemon
     * Time: 2017/9/20 10:53
     * Description: 把日期转换成druid所需格式
     * 2017-09-09T00:00:00+0800/2017-09-09T23:59:59+0800
     */

    public static String getDruidDateStr(String dateStr) {
        dateStr = dateStr + "T00:00:00+0800/" + dateStr + "T23:59:59+0800";
        return dateStr;
    }

    public static void main(String args[]) {
        int i = 0;
        while (i < 3) {
            System.out.println(i);
            if (i > 3) {
                break;
            }
            i++;
        }

    }

}
