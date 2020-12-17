package com.detabes.logs.cache.p6spy;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * now + "|" + elapsed + "|" + category + "|connection " + connectionId + "|url " + url + "|" + P6Util.singleLine(prepared) + "|" + P6Util.singleLine(sql);
 * #01-07-20 14:37:38:871 | took 1ms | statement | connection 0| url jdbc:p6spy:mysql://localhost:3306/data?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=true
 *
 * @author tn
 * @version 1
 * @ClassName P6SpyLogger
 * @description 自定义日志打印
 * @date 2020/7/3 9:19
 */
public class P6SpyLogger implements MessageFormattingStrategy {

    static final String SELECT_1 = "SELECT 1";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if(!SELECT_1.equals(P6Util.singleLine(prepared).toUpperCase())){
            StringBuilder sb = new StringBuilder();
            sb
                    .append("=====================================================\n")
                    .append("连接id：").append(connectionId).append("\n")
                    .append("当前时间：").append(now).append("\n")
                    .append("类别：").append(category).append("\n")
                    .append("花费时间(ms)：").append(elapsed).append("\n")
                    .append("预编译sql：").append(P6Util.singleLine(prepared)).append("\n")
                    .append("最终执行的sql：").append(P6Util.singleLine(sql))
                    .append("\n=====================================================\n");
            return sb.toString();
        }else{
            return "数据库连接池中线程断线重连机制 - 判断线程是否需要重连";
        }

    }
}
