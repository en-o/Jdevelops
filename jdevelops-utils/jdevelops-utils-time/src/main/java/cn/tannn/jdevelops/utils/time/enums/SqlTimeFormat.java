package cn.tannn.jdevelops.utils.time.enums;


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

    /**
     * 周数-年周 202501[年份+周数]
     */
    String MYSQL_FORMAT_Y_WEEK = "%Y-%u'";

    /**
     * 周数
     */
    String MYSQL_FORMAT_WEEK = "%u'";

}
