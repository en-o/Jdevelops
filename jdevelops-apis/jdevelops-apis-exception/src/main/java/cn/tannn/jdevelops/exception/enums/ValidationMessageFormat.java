package cn.tannn.jdevelops.exception.enums;

/**
 * 处理validation异常提示格式
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/11/19 上午10:24
 */
public enum ValidationMessageFormat {
    /**
     * [字段+message]
     * <p>默认</p>
     */
    FIELD_MESSAGE,

    /**
     * [只有message]
     */
    MESSAGE,

    /**
     * 只返回第一个message的信息，其余的废弃
     */
    ONLY_ONE
}
