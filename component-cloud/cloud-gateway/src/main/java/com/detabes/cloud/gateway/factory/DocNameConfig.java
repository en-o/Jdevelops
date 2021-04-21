package com.detabes.cloud.gateway.factory;


import lombok.Data;

/**
 * 每个文档的中文名
 * <pre>
 * 写-路由里（DocName）
 * spring:
 *   cloud:
 *     gateway:
 *       routes:
 *         - id: server-user
 *           uri: lb://server-user
 *           predicates:
 *             - Path=/api/user/**
 *             - DocName=用户
 *         - id: server-basic
 *           uri: lb://server-basic
 *           predicates:
 *             - Path=/api/basic/**
 *             - DocName=基础
 *
 * 用在： http://192.168.0.111:200/swagger-resources
 * 接口文档的搜索框中
 * </pre>
 * @author tnnn
 */
@Data
public class DocNameConfig {
    private String name;
}