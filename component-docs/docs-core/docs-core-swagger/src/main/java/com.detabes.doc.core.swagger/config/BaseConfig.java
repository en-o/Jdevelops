package com.detabes.doc.core.swagger.config;

import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author tn
 * @version 1
 * @ClassName BaseConfig
 * @description 基础设置
 * @date 2020/12/16 11:14
 */
public class BaseConfig {


    /**
     * 统一填写一次token
     * @return
     */
    public static List<SecurityScheme> security() {
        return newArrayList(
                new ApiKey("token", "token", "header")
        );
    }
}
