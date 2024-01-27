package cn.jdevelops.authentication.sas.server.controller.dto;
import cn.jdevelops.api.result.request.SortPageDTO;


/**
 * 客户端查询
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 00:58
 */
public class ClientPage {

    /**
     * 授权范围[多个逗号隔开]
     */
    private String scopes;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端secret到期区间,逗号隔开 : 2023-01-01,2023-01-02
     */
    private String clientSecretExpiresAtSection;


    /**
     * 分页排序参数
     */
    private SortPageDTO sortPage;


    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSecretExpiresAtSection() {
        return clientSecretExpiresAtSection;
    }

    public void setClientSecretExpiresAtSection(String clientSecretExpiresAtSection) {
        this.clientSecretExpiresAtSection = clientSecretExpiresAtSection;
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
        return "ClientPage{" +
                "scopes='" + scopes + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientSecretExpiresAtSection='" + clientSecretExpiresAtSection + '\'' +
                ", sortPage=" + sortPage +
                '}';
    }
}
