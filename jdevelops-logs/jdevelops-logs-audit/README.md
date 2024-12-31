# 模块配置说明
1. 数据入库配置，参考 AuditContext 的字段构建表结构-注意新增一个 id 字段，和 操作时间 字段
2. 构建入库方法， 参考DefAuditSave 重新实现 AuditSave e.g `@Service public class DbAuditSave implements AuditSave{} `



# 审计日志使用说明
## 1. 在需要审计的方法上加入注解
> 注解里的参数根据实际情况设置， 特别注意：auditType
```java
    @AuditLog(auditType = OperationalAuditType.role_dispose
            , operationType = OperationalType.DELETE
            , description = AuditLogDescription.DELETE_REAL
            
    )
```
## 2. 在需要审计的方法里设置审计参数
> 具体参数根据实际情况设置 ， 特别注意：UniqueIndexType ，UniqueIndex
```java
AuditContextHolder.getContext().setUniqueCode(originalRole.getCode())
                    .setDataTitle(originalRole.getName())
                    .setUniqueIndexType(UniqueIndexType.MYSQL)
                    .setUniqueIndex(OperationalAuditIndex.TABLE_TB_ROLE)
                    .originalJson(originalRole)
                    .appendOptUser(userInfo)
```
## batch = true
> 批量
```java
BatchAuditContext batchContext = AuditContextHolder.getBatchContext();
batchContext.addContext(new AuditContext());
```



