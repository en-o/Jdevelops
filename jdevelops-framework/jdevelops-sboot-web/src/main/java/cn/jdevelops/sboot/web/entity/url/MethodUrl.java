package cn.jdevelops.sboot.web.entity.url;

import lombok.*;

/**
 * 接口请求类型和接口地址
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-20 12:45
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MethodUrl {

    /**
     * get post put delete
     */
    private String method;

    /**
     * 接口地址
     */
    private String uri;
}
