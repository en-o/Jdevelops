package cn.jdevelops.authentication.sas.server.controller.dto;

import cn.jdevelops.api.result.request.SortPageDTO;

/**
 * 授权同意信息
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 02:09
 */
public class AuthorizationConsentPage {

    /**
     * 客户端ID
     */
    private String clientId;


    /**
     * 用户登录名
     */
    private String loginName;

    /**
     * 分页排序参数
     */
    private SortPageDTO sortPage;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public SortPageDTO getSortPage() {
        if(sortPage == null){
            return new SortPageDTO();
        }
        return sortPage;
    }

    public void setSortPage(SortPageDTO sortPage) {
        this.sortPage = sortPage;
    }

    @Override
    public String toString() {
        return "AuthorizationConsentPage{" +
                "clientId='" + clientId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", sortPage=" + sortPage +
                '}';
    }
}
