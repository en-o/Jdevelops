package com.detabes.time.constant;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间格式常量
 * @author tn
 * @date 2020/4/9 11:02
 */
public interface TimeFormat {

    /** 默认格式  yyyy-MM-dd HH:mm:ss  */
     String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 格式  yyyy-MM-dd  */
     String NORM_DATE_FORMAT = "yyyy-MM-dd";

    /** 格式  yyyyMMdd  */
     String PURE_DATE_FORMAT = "yyyyMMdd";

    /** 格式  yyyyMM  */
     String YM_DATE_FORMAT = "yyyyMM";

    /** 格式  yyyy-MM-dd HH:mm  */
     String NORM_DATETIME_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

    /** 格式  yyyyMMddHHmmss  */
     String PURE_DATETIME_FORMAT = "yyyyMMddHHmmss";

    /** 格式  MMddHHmmss  */
     String NOYEAR_DATETIME_FORMAT = "MMddHHmmss";

    /** 格式  yyyyMMddHH  */
     String YMDH_DATE_FORMAT = "yyyyMMddHH";

    /** 格式  yyyy */
     String YEAR_DATE_FORMAT = "yyyy";


    /** 格式  yyyy/MM/dd  */
     String EN_DATE_FORMAT = "yyyy/MM/dd";

    /** 格式  yyyy/MM  */
     String EN_DATE_MONTH_FORMAT = "yyyy/MM";

    /** 格式  yyyy/MM/dd HH:mm  */
     String EN_DATETIME_MINUTE_FORMAT = "yyyy/MM/dd HH:mm";

    /** 格式  yyyy/MM/dd HH:mm:ss  */
     String EN_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /** 格式  yyyy年MM月dd日 */
     String CN_DATE_FORMAT = "yyyy年MM月dd日";

    /** 格式  yyyy年MM月  */
     String CN_DATE_MONTH_FORMAT = "yyyy年MM月";

    /** 格式  yyyy年MM月dd日 HH:mm  */
     String CN_DATETIME_MINUTE_FORMAT = "yyyy年MM月dd日 HH:mm";

    /** 格式  yyyy年MM月dd日 HH:mm:ss  */
     String CN_DATETIME_FORMAT = "yyyy年MM月dd日 HH:mm:ss";


    /** 格式  yyyy-MM  */
     String SIMPLE_DATE_MONTH_FORMAT = "yyyy-MM";

    /** 格式  MM-dd  */
     String SIMPLE_DATE_DAY_FORMAT = "MM-dd";

    /** 格式  MM-dd HH:mm  */
     String SIMPLE_DATETIME_MINUTE_FORMAT = "MM-dd HH:mm";

    /** 格式  MM-dd HH:mm:ss */
     String SIMPLE_DATETIME_SECOND_FORMAT = "MM-dd HH:mm:ss";


    /** 格式  HH:mm  */
     String SIMPLE_TIME_MINUTE_FORMAT = "HH:mm";

    /** 格式  HH:mm:ss */
     String NORM_TIME_FORMAT = "HH:mm:ss";

    /** 格式  MM/dd */
     String EN_SIMPLE_DATE_DAY_FORMAT = "MM/dd";

    /** 格式  MM/dd HH:mm  */
     String EN_SIMPLE_DATETIME_MINUTE_FORMAT = "MM/dd HH:mm";

    /** 格式  MM/dd HH:mm:ss */
     String EN_SIMPLE_DATETIME_SECOND_FORMAT = "MM/dd HH:mm:ss";



    /** 格式  MM月dd日  */
     String CN_SIMPLE_DATE_DAY_FORMAT = "MM月dd日";

    /** 格式  MM月dd日 HH:mm  */
     String CN_SIMPLE_DATETIME_MINUTE_FORMAT = "MM月dd日 HH:mm";

    /** 格式  MM月dd日 HH:mm:ss */
     String CN_SIMPLE_DATETIME_SECOND_FORMAT = "MM月dd日 HH:mm:ss";

    /** 格式  yyyy.MM.dd  */
     String SPECIAL_SIMPLE_DATE_FORMAT = "yyyy.MM.dd";

    /** 格式  MM.dd  */
     String SPECIAL_SIMPLE_DATE_DAY_FORMAT = "MM.dd";


    /** 格式  公元 2019-01-01 +0800 星期二 10:39:06:863 下午  */
     String AD_DATETIME_FORMAT = "G y-MM-dd Z E HH:mm:ss:SSS a";

    /** 格式  EEE, dd MMM yyyy HH:mm:ss z  */
     String HTTP_DATETIME_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

    /** 格式  EEE MMM dd HH:mm:ss zzz yyyy  */
     String JDK_DATETIME_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    /** 格式  yyyy-MM-dd'T'HH:mm:ss'Z  */
     String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";




    /** 格式  mysql %Y */
     String MYSQL_YEAR_DATE_FORMAT = "%Y";

    /** 格式  mysql %Y-%m */
     String MYSQL_SIMPLE_DATE_MONTH_FORMAT = "%Y-%m";

    /** 格式  mysql %Y-%m-%d */
     String MYSQL_NORM_DATE_FORMAT = "%Y-%m-%d";

    /** 格式  mysql %Y-%m-%d %T*/
     String MYSQL_DEFAULT_FORMAT = "%Y-%m-%d %T";

    /** 格式  mysql %T*/
     String MYSQL_HHMMSS_FORMAT = "%T";

    /**
     * 获取方法里 所有的 时间格式
     * @return 所有时间格式
     * @throws IllegalAccessException 非法
     */
    static List<String> getAllFormart() throws IllegalAccessException {
        List<String> list = new ArrayList<>();
        Field[] fields = TimeFormat.class.getFields();
        for (Field f: fields) {
            //获取属性f.getName()
            //获取属性值f
            Object value = f.get(TimeFormat.class);
            list.add(value.toString());
        }
        return list;
    }
}
