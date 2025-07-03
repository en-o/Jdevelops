package cn.tannn.jdevelops.jdectemplate.sql;

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
//        AccountPage page = new AccountPage();
////        page.setLoginName("ADMIN");
//        page.setLoginPlatform(PlatformType.ADMIN);
//        page.setUserExpireTimeValid(true);
//        DynamicSqlBuilder builder = AccountQuery.buildAccountJdbc(page);
//        List<AccountFull2> query = jdbcTemplate.query(builder.getSql(), builder.getNamedParams()
//                , new DataClassRowMapper<>(AccountFull2.class));
//
//        query.forEach(System.out::println);
//        Long queried = jdbcTemplate.queryForObject(builder.buildCountSql().getSql(), builder.getNamedParams()
//                , Long.class);
//        System.out.println("总数：" + queried);
//    }


}
