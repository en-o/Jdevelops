package cn.tannn.jdevelops.jpa.utils;

import cn.tannn.jdevelops.annotations.jpa.enums.SpecBuilderDateFun;
import cn.tannn.jdevelops.result.bean.ColumnSFunction;
import cn.tannn.jdevelops.result.bean.ColumnUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Jpa项目里的工具类
 *
 * @author tn
 * @version 1
 * @date 2020/6/28 23:16
 */
public class JpaUtils {




    /**
     * 处理时间格式的key
     *
     * @param function  SpecBuilderDateFun
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B>       B
     */
    public static <B> Expression<String> functionTimeFormat(SpecBuilderDateFun function,
                                                            Root<B> root,
                                                            CriteriaBuilder builder,
                                                            String selectKey) {
        return builder
                .function(function.getName()
                        , String.class
                        , root.get(selectKey)
                        , builder.literal(function.getSqlFormat()));
    }


    /**
     * 格式化时间数据的值为字符串
     * bean:  LocalDateTime
     * sql：  timestamp
     * e.g.  mysql： date_format(user0_.create_time, "SQL 的 时间类型")
     * pgssql： to_char(user0_.create_time, "SQL 的 时间类型")
     *
     * @param function  SpecBuilderDateFun
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B>       B
     */
    public static <B> Expression<String> functionTimeFormat(SpecBuilderDateFun function,
                                                            Root<B> root,
                                                            CriteriaBuilder builder,
                                                            ColumnSFunction<B, ?> selectKey) {

        return builder
                .function(function.getName()
                        , String.class
                        , root.get(ColumnUtil.getFieldName(selectKey))
                        , builder.literal(function.getSqlFormat()));
    }


    /**
     * sql  date函数 固定死的
     * e.g.   DATE ( user0_.create_time ) =?
     *
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B>       B
     */
    public static <B> Expression<Date> functionTime(Root<B> root,
                                                    CriteriaBuilder builder,
                                                    String selectKey) {
        return builder
                .function("date"
                        , Date.class
                        , root.get(selectKey));
    }
}
