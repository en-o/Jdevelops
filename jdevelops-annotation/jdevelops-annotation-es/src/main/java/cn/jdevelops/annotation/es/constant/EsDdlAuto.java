package cn.jdevelops.annotation.es.constant;

/**
 * 索引创建的模式
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/25 10:37
 */
public enum EsDdlAuto {

    /**
     * 不创建
     */
    NONE,
    /**
     * 项目每次重启都会删除旧的创建新的
     */
    CREATE,
    /**
     * 比对mapping 一样就不创建，不一样就重新创建（性能不好）[key一样，value没有验证,所以只会判断字段是否发生了变化而不会判断类型的变化]
     */
    UPDATE,
    /**
     * 只验证索引名重复，重复提醒，不重复才创建（推荐）
     */
    VALIDATE
}
