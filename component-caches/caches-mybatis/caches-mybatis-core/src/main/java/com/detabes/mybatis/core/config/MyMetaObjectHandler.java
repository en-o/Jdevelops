package com.detabes.mybatis.core.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * description: 公共字段填充处理器
 *
 * @author lmz
 * @company Peter
 * @date 2020/12/14  16:02
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName("createTime", metaObject);
        Object updateTime = getFieldValByName("updateTime", metaObject);
        Object uuid = getFieldValByName("uuid", metaObject);
        // 如果该字段没有设置值
        if (createTime == null) {
            // 起始版本 3.3.0
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
        if (updateTime == null) {
            // 起始版本 3.3.0
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
        if (Objects.isNull(uuid) ||StringUtils.isBlank(uuid.toString())) {
            // 起始版本 3.3.0
            this.strictInsertFill(metaObject, "uuid", String.class, UUID.randomUUID().toString().replaceAll("-", ""));
        }


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 起始版本 3.3.0
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

    }
}
