package com.daixu.jandan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Liompei
 * time : 2017/11/9 9:56
 * 1137694912@qq.com
 * remark:时间格式器
 */

public class TimeUtil {
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    //String时间转long
    //strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    public static Date stringToDate(String strTime, String formatType) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Date date = null;
        date = format.parse(strTime);
        return date;
    }

    /**
     * @param date 传入的时间
     * @return
     */
    public static String getTimeFormatText(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        long diff = System.currentTimeMillis() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }
}
