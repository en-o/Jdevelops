package cn.tannn.jdevelops.p6spy;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * now + "|" + elapsed + "|" + category + "|connection " + connectionId + "|url " + url + "|" + P6Util.singleLine(prepared) + "|" + P6Util.singleLine(sql);
 * #01-07-20 14:37:38:871 | took 1ms | statement | connection 0| url jdbc:p6spy:mysql://localhost:3306/data?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=true
 * 自定义日志打印
 * @author tn
 * @version 1
 * @date 2020/7/3 9:19
 */
public class P6SpyLogger implements MessageFormattingStrategy {

    static final String SELECT_1 = "SELECT 1";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if(!SELECT_1.equalsIgnoreCase(P6Util.singleLine(prepared))){
            return "=====================================================\n" +
                    "连接id：" + connectionId + "\n" +
                    "当前时间：" + now + "\n" +
                    "类别：" + category + "\n" +
                    "花费时间(ms)：" + elapsed + "\n" +
                    "预编译sql：" + P6Util.singleLine(prepared) + "\n" +
                    "最终执行的sql：" + P6Util.singleLine(sql) +
                    "\n=====================================================\n";
        }else{
            return "数据库连接池中线程断线重连机制 - 判断线程是否需要重连";
        }

    }
}
