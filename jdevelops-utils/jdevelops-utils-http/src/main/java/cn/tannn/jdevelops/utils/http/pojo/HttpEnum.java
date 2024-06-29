package cn.tannn.jdevelops.utils.http.pojo;


/**
 * http相关
 * @author tn
 * @date 2020-12-17 14:32
 */

public enum HttpEnum {

    /** POST */
    POST("POST","POST"),
    /** GET */
    GET("GET","GET"),
    /** PUT */
    PUT("PUT","PUT"),
    /** PATCH */
    PATCH("PATCH","PATCH"),
    /** DELETE */
    DELETE("DELETE","DELETE"),
    /** HEAD */
    HEAD("HEAD","HEAD"),
    /** OPTIONS */
    OPTIONS("OPTIONS","OPTIONS"),
    /**  multipart/ */
    MULTIPART_PATHSEPARATOR("MULTIPART/","消息头中的东西 multipart/"),
    ;
    private final String code;
    private final String remark;

    HttpEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}
