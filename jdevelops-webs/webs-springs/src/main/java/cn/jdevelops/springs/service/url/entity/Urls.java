package cn.jdevelops.springs.service.url.entity;

import lombok.*;

/**
 * 接口集合
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-20 11:07
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Urls {

    /**
     * 分组
     */
    private String grouping;

    /**
     * 接口中文名
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口请求类型
     */
    private String requestMethod;

}
