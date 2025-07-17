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
        // SELECT a.login_name, a.name, a.gender, a.status, a.create_time, a.create_time, b.masked_phone
        // FROM tb_account a LEFT JOIN tb_account_sensitive b ON b.user_id = a.id WHERE login_name = :login_name1 AND name LIKE :nameLike2
        // AND status IN (:statusList3) AND b.masked_phone LIKE :bmasked_phoneLike4 ORDER BY a.id DESC LIMIT :offset, :limit
        System.out.println("sql: "+builder.getSql());
        System.out.println("native: "+builder.getNativeSql());


        builder.orderBy("a.name desc");
        // SELECT a.login_name, a.name, a.gender, a.status, a.create_time, a.create_time, b.masked_phone
        // FROM tb_account a LEFT JOIN tb_account_sensitive b ON b.user_id = a.id WHERE login_name = :login_name1 AND name
        // LIKE :nameLike2 AND status IN (:statusList3)
        // AND b.masked_phone LIKE :bmasked_phoneLike4 ORDER BY a.id DESC, a.name desc LIMIT :offset, :limit
        System.out.println("追加sql: "+builder.getSql());
        System.out.println("追加native: "+builder.getNativeSql());

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
