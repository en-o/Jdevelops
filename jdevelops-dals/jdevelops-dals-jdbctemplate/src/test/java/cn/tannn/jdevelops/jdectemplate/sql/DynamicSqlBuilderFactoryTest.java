package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.jdectemplate.sql.pojo.AccountQueryExample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/17 15:51
 */
public class DynamicSqlBuilderFactoryTest {

    @Test
    public void example() {
        // 创建查询对象
        AccountQueryExample query = new AccountQueryExample();
        query.setLoginName("admin");
        query.setName("张三");
        query.setStatus(Arrays.asList(1, 2));
        query.setPhone("123");
        query.setPageIndex(0);
        query.setPageSize(10);

        // 构建SQL
        DynamicSqlBuilder builder = DynamicSqlBuilderFactory.buildJdbc(query);
        System.out.println(builder.getSql());
        System.out.println(builder.getNativeSql());
//        // 执行查询
//        List<Account> accounts = namedParameterJdbcTemplate.query(
//                builder.getSql(),
//                builder.getNamedParams(),
//                new DataClassRowMapper<>(Account.class)
//        );
//
//        // 获取总数
//        Long total = namedParameterJdbcTemplate.queryForObject(
//                builder.buildCountSql().getSql(),
//                builder.buildCountSql().getNamedParams(),
//                Long.class
//        );
    }
}
