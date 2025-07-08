package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.result.response.ResultPageVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DynamicSqlBuilderPageDemo {

//
//    void demo() {
// SQL
//DynamicSqlBuilder builder = AccountQuery.buildAccountJdbc(page);
//    List<AccountFull2> accounts = namedParameterJdbcTemplate.query(builder.getSql(), builder.getNamedParams()
//            , new DataClassRowMapper<>(AccountFull2.class));
//    // 获取总记录数
//    Long total = namedParameterJdbcTemplate.queryForObject(builder.buildCountSql().getSql(), builder.getNamedParams()
//            , Long.class);
//    // 构建分页结果
//    PageResult<AccountFull2> pageResult = JpaPageResult.page(page.getPage(), total, accounts);
//        return ResultPageVO.success(pageResult, "查询成功");
//    }


//public static DynamicSqlBuilder buildAccountJdbc(AccountPage page) {
//    DynamicSqlBuilder builder = new DynamicSqlBuilder("""
//                    SELECT a.*,b.masked_phone as phone,
//                        (SELECT GROUP_CONCAT(c.login_platform) FROM tb_account_login_platform c WHERE c.user_id = a.id) as login_platforms
//                     FROM tb_account a left join tb_account_sensitive b on b.user_id=a.id
//                """, ParameterMode.NAMED);
////        if (page.getLoginPlatform() != null && !page.getLoginPlatform().equals(PlatformType.NONE)) {
////            builder.custom("""
////            EXISTS (
////                SELECT 1 FROM tb_account_login_platform b
////                WHERE b.user_id = a.id
////                AND b.login_platform = :loginPlatform
////            )
////            """).addNamedParam("loginPlatform", page.getLoginPlatform().name());
////        }
//    // FIND_IN_SET 是 MySQL 的一个字符串函数，用于在逗号分隔的字符串列表中查找一个指定的字符串。
//    if (page.getLoginPlatform() != null && !page.getLoginPlatform().equals(PlatformType.NONE)) {
//        builder.custom("""
//            FIND_IN_SET(:loginPlatform, (
//                SELECT GROUP_CONCAT(c.login_platform)
//                FROM tb_account_login_platform c
//                WHERE c.user_id = a.id
//            )) > 0
//        """).addNamedParam("loginPlatform", page.getLoginPlatform().name());
//    }
//    // 添加动态条件
//    builder
//            .dynamicEq("a.login_name", page.getLoginName(), NullHandleStrategy.IGNORE)
//            .dynamicEq("a.gender", page.getGender(), NullHandleStrategy.IGNORE)
//            .dynamicLike("a.name", page.getName(), NullHandleStrategy.IGNORE)
//            .dynamicEq("a.status", page.getStatus(), NullHandleStrategy.IGNORE)
//            .dynamicEq("a.type", page.getType(), NullHandleStrategy.IGNORE)
//            .dynamicEq("a.available", page.getAvailable(), NullHandleStrategy.IGNORE)
//            .dynamicEq("a.register_platform", page.getRegisterPlatform(), NullHandleStrategy.IGNORE)
//            .dynamicLike("a.org_name", page.getOrgName(), NullHandleStrategy.IGNORE);
//
//    // 处理组织编码条件
//    if (page.getOrgNo() != null) {
//        if (page.getOrgNo().trim().isEmpty()) {
//            builder.isNull("a.org_no");
//        } else {
//            builder.dynamicEq("a.org_no", page.getOrgNo(), NullHandleStrategy.IGNORE);
//        }
//    }
//    // 处理用户过期时间条件
//    if (page.getUserExpireTimeValid() != null) {
//        String nowTime = TimeUtil.thisDefNow();
//        if (page.getUserExpireTimeValid()) {
//            // 有效用户：过期时间大于当前时间或过期时间为空
//            builder.or(o -> {
//                o.isNull("user_expire_time");
//                o.op("a.user_expire_time",">=", nowTime);
//                return o;
//            });
//        } else {
//            // 无效用户：过期时间小于当前时间且过期时间不为空
//            builder.isNotNull("a.user_expire_time");
//            builder.op("a.user_expire_time","<", nowTime);
//        }
//    }
//
//
//    // 添加分页
//    builder.pageZero(page.getPage().getPageIndex(), page.getPage().getPageSize());
//    return builder;
//}


}
