package cn.jdevelops.authentication.sas.server.controller.dto;

import javax.validation.constraints.NotBlank;

/**
 * 修改Secret过期时间
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 02:35
 */
public class ClientEditSecretExpiresAt {
    /**
     * 客户端id
     */
    @NotBlank
    String clientId;


    /**
     * 新的客户端过期时间[为空永不过期,格式 日期:2023-01-01]
     * ps: 时分秒默认00:00:00
     */
    @NotBlank
    private String clientSecretExpiresAt;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(String clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    @Override
    public String toString() {
        return "ClientEditSecretExpiresAt{" +
                "clientId='" + clientId + '\'' +
                ", clientSecretExpiresAt='" + clientSecretExpiresAt + '\'' +
                '}';
    }
}
