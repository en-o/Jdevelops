package cn.jdevelops.data.jap.enums;


/**
 * 查询时处理时间 [时间 -> 字符串]
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-06-10 16:59
 */
public enum SpecBuilderDateFun {

    /**
     * 空的，给注解占位用的，其他不要用
     */
    NULL(null,null),

    /**
     * 只测试过msql
     */
    DATE_FORMAT("DATE_FORMAT","%Y-%m-%d %T"),


    /**
     * 只测试过PGSQL ,人大金仓
     */
    TO_CHAR("to_char","YYYY-MM-DD HH24:MI:SS"),

    ;


    /**
     * 时间函数名
     */
    private final String name;




    /**
     * sql 格式， sqlDateFunName.DATE_FORMAT 才用的到
     * @see cn.jdevelops.util.time.enums.TimeFormatEnum#MYSQL_FORMAT_DATETIME_SECOND
     */
    private String sqlFormat;


    SpecBuilderDateFun(String name,  String sqlFormat) {
        this.name = name;
        this.sqlFormat = sqlFormat;
    }

    public String getSqlFormat() {
        return sqlFormat;
    }

    public String getName() {
        return name;
    }


}
