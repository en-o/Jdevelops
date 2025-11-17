package cn.tannn.jdevelops.mq.redis;

/**
 * mq 队列名 常量
 * <p>  统一前缀  {@link ResMqProperties#getPrefix()}
 * @author tan
 */
public class MqConstants {

    /**
     * 文件上传 topic
     * 全称 TOPIC_PREFIX+UPLOAD_TOPIC 【papermate:com:sunway:resmq:resource-upload-topic】
     */
    public final static String UPLOAD_TOPIC = "resource-upload-topic";

    /**
     * 解析中
     */
    public final static String PARSE_RUN_TOPIC = "resource-parse-runing-topic";

    /**
     * 解析成功
     */
    public final static String PARSE_OK_TOPIC = "resource-parse-success-topic";

    /**
     * 结构优化中
     */
    public final static String ADJUST_TOPIC = "resource-adjust-runing-topic";

    /**
     * 结构优化完成
     */
    public final static String ADJUST_SUCCESS_TOPIC = "resource-adjust-success-topic";

    /**
     * 翻译中
     */
    public final static String TRANS_RUN_TOPIC = "resource-trans-runing-topic";

    /**
     * 等待翻译
     */
    public static final String WAIT_TRANS_TOPIC = "resource-wait-trans-topic";

    /**
     * 翻译完成
     */
    public final static String TRANS_OK_TOPIC = "resource-trans-success-topic";


    /**
     * 处理失败队列
     */
    public final static String ERROR_TOPIC = "resource-error-topic";

}
