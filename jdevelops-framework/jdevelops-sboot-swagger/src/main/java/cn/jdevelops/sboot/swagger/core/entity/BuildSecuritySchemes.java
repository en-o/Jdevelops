package cn.jdevelops.sboot.swagger.core.entity;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;
import java.util.Map;

/**
 * 构造 安全方案
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-13 15:56
 */
public class BuildSecuritySchemes {

    /**
     *  SecurityRequirement： 全局应用
     */
    List<SecurityRequirement> securityItem;

    /**
     * SecurityScheme 添加 Authorize
     */
    Map<String, SecurityScheme> securitySchemes;

    public BuildSecuritySchemes() {
    }

    public BuildSecuritySchemes(List<SecurityRequirement> securityItem, Map<String, SecurityScheme> securitySchemes) {
        this.securityItem = securityItem;
        this.securitySchemes = securitySchemes;
    }

    @Override
    public String toString() {
        return "BuildSecuritySchemes{" +
                "securityItem=" + securityItem +
                ", securitySchemes=" + securitySchemes +
                '}';
    }

    public List<SecurityRequirement> getSecurityItem() {
        return securityItem;
    }

    public void setSecurityItem(List<SecurityRequirement> securityItem) {
        this.securityItem = securityItem;
    }

    public Map<String, SecurityScheme> getSecuritySchemes() {
        return securitySchemes;
    }

    public void setSecuritySchemes(Map<String, SecurityScheme> securitySchemes) {
        this.securitySchemes = securitySchemes;
    }
}
