package cn.tannn.jdevelops.annotations.jpa.enums;


/**
 * 查询时处理时间 [时间 -> 字符串]
 *  目前按照字符串格式化设计,暂时不考虑 date类型
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
     * 只测试过msql 年月日时分秒
     */
    DATE_FORMAT("DATE_FORMAT","%Y-%m-%d %T"),

    /**
     * 只测试过msql 年月日
     */
    DATE_FORMAT_DATE("DATE_FORMAT","%Y-%m-%d"),

    /**
     * 只测试过msql 年月
     */
    DATE_FORMAT_MOUTH("DATE_FORMAT","%Y-%m"),


    /**
     * 只测试过msql 年月
     */
    DATE_FORMAT_YEAR("DATE_FORMAT","%Y"),


    /**
     * 只测试过PGSQL ,人大金仓 - 年月日时分秒
     */
    TO_CHAR("to_char","YYYY-MM-DD HH24:MI:SS"),


    /**
     * 只测试过PGSQL ,人大金仓 - 年月日
     */
    TO_CHAR_DATE("to_char","YYYY-MM-DD"),



    /**
     * 只测试过PGSQL ,人大金仓 - 年月
     */
    TO_CHAR_MOUTH("to_char","YYYY-MM"),



    /**
     * 只测试过PGSQL ,人大金仓 - 年
     */
    TO_CHAR_YEAR("to_char","YYYY"),

    ;


    /**
     * 时间函数名
     */
    private final String name;




    /**
     * sql 格式， sqlDateFunName.DATE_FORMAT 才用的到
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
