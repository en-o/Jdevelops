package cn.jdevelops.authentication.sas.server.controller.dto;

import javax.validation.constraints.NotBlank;

/**
 * 修改授权范围
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 02:25
 */
public class ClientEditScopes {

    /**
     * 新的授权范围[多个空格隔开]
     */
    @NotBlank
    private String scopes;

    /**
     * 客户端id
     */
    @NotBlank
    String clientId;


    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "ClientEditScopes{" +
                "scopes='" + scopes + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
