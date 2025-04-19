package cn.tannn.jdevelops.utils.time.enums;


/**
 * sql 是格式化
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/13 11:18
 */
public enum SqlTimeFormatEnum {
    /**
     * 格式  mysql %Y
     */
    MYSQL_FORMAT_DATETIME_YEAR(SqlTimeFormat.MYSQL_FORMAT_DATETIME_YEAR, "年"),
    /**
     * 格式  mysql %Y-%m
     */
    MYSQL_FORMAT_DATETIME_MONTH(SqlTimeFormat.MYSQL_FORMAT_DATETIME_MONTH,  "年月"),
    /**
     * 格式  mysql %Y-%m-%d
     */
    MYSQL_FORMAT_DATETIME_DAY(SqlTimeFormat.MYSQL_FORMAT_DATETIME_DAY, "年月日"),
    /**
     * 格式  mysql %Y-%m-%d %T
     */
    MYSQL_FORMAT_DATETIME_SECOND(SqlTimeFormat.MYSQL_FORMAT_DATETIME_SECOND, "年月日时分秒"),
    /**
     * 格式  mysql %T
     */
    MYSQL_FORMAT_DATETIME_SIMPLE_DAY(SqlTimeFormat.MYSQL_FORMAT_DATETIME_SIMPLE_DAY, "时分秒"),

    /**
     * 周  mysql %u
     */
    MYSQL_FORMAT_WEEK(SqlTimeFormat.MYSQL_FORMAT_WEEK, "周"),

    /**
     * 年周  mysql %Y-%u
     */
    MYSQL_FORMAT_Y_WEEK(SqlTimeFormat.MYSQL_FORMAT_Y_WEEK, "年周"),
    ;

    private final String format;
    private final String remark;

    SqlTimeFormatEnum(String format, String remark) {
        this.format = format;
        this.remark = remark;
    }

    public String getFormat() {
        return format;
    }

    public String getRemark() {
        return remark;
    }

}
