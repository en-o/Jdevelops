package cn.jdevelops.enums.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * http相关
 * @author tn
 * @date 2020-12-17 14:32
 */
@Getter
@AllArgsConstructor
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
    private String str;
    private String remark;
}
