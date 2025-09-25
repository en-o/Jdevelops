package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.jdectemplate.sql.pojo.UserQueryDto;
import cn.tannn.jdevelops.jdectemplate.sql.pojo.UserResultVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/9/25 11:46
 */
@DisplayName("SqlBuilderChain Tests")
public class SqlBuilderChainTest {

    @Test
    @DisplayName("链式构建 - 基本用法")
    void example2_ChainBasic() {
        UserQueryDto query = new UserQueryDto();
        query.setUserName("李四");
        query.setStatus(1);

        DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
                .select(UserResultVO.class)
                .from()
                .where()
                .groupBy((String) null)
                .having((String) null)
                .orderBy("u.name ASC")
                .limit(0, 20)
                .build();

        System.out.println("链式构建SQL: " + builder.getSql());
        System.out.println("链式构建参数: " + java.util.Arrays.toString(builder.getPositionalParams()));
        System.out.println();
    }



    @Test
    @DisplayName("自定义SELECT字段")
    void example3_CustomSelect() {
        UserQueryDto query = new UserQueryDto();
        query.setStatus(1);

        DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
                .select("u.id, u.name, COUNT(*) OVER() as total_count")
                .from()
                .where()
                .build();
        System.out.println("自定义SELECT SQL: " + builder.getSql());
        System.out.println();
    }



    @Test
    @DisplayName("扩展WHERE条件")
    void example4_ExtendWhere() {

        UserQueryDto query = new UserQueryDto();
        query.setUserName("王五");

        DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
                .select(UserResultVO.class)
                .from()
                .where(sqlBuilder -> {
                    // 添加额外的条件
                    sqlBuilder.op("u.create_time", "<=","2023-01-01");
                    sqlBuilder.isNotNull("u.email");
                    sqlBuilder.or(orBuilder -> orBuilder
                            .eq("d.type", "TECH")
                            .eq("r.level", "SENIOR")
                    );
                })
                .build();

        System.out.println("扩展WHERE SQL: " + builder.getSql());
        System.out.println("扩展WHERE参数: " + java.util.Arrays.toString(builder.getPositionalParams()));
        System.out.println();
    }



    @Test
    @DisplayName("自定义FROM子句")
   void example5_CustomFrom() {
        UserQueryDto query = new UserQueryDto();
        query.setStatus(1);

        DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
                .select("u.id, u.name, p.avatar, COUNT(ur.role_id) as role_count")
                .from("FROM users u " +
                        "LEFT JOIN user_profiles p ON u.id = p.user_id " +
                        "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                        "LEFT JOIN roles r ON ur.role_id = r.id")
                .where(sqlBuilder -> {
                    sqlBuilder.eq("u.status", 1);
                    sqlBuilder.isNotNull("p.avatar");
                })
                .groupBy("u.id, u.name, p.avatar")
                .having("COUNT(ur.role_id) > 0")
                .orderBy("role_count DESC")
                .limit()
                .build();

        System.out.println("自定义FROM SQL: " + builder.getSql());
        System.out.println();
    }

}
