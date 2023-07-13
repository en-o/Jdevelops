package cn.jdevelops.util.time.enums;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * sql 是格式化
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/13 11:18
 */
public interface SqlTimeFormat {


    /**
     * 格式  mysql %Y  年
     */
    String MYSQL_FORMAT_DATETIME_YEAR = "%Y";
    /**
     * 格式  mysql %Y-%m  年月
     */
    String MYSQL_FORMAT_DATETIME_MONTH = "%Y-%m";
    /**
     * 格式  mysql %Y-%m-%d 年月日
     */
    String MYSQL_FORMAT_DATETIME_DAY = "%Y-%m-%d";
    /**
     * 年月日时分秒  mysql %Y-%m-%d %T
     */
    String MYSQL_FORMAT_DATETIME_SECOND = "%Y-%m-%d %T";
    /**
     * 时分秒 mysql %T
     */
    String MYSQL_FORMAT_DATETIME_SIMPLE_DAY = "%T";



}
