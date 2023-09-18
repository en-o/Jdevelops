package cn.jdevelops.data.ddss.model;


/**
 * 数据源元信息
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 12:00
 */

public class DynamicDatasourceEntity {

    /**
     * id
     */
    Integer id;

    /**
     * 数据源名称
     */
    String datasourceName;

    /**
     * 数据源url
     */
    String datasourceUrl;

    /**
     * 数据源帐号
     */
    String datasourceUsername;

    /**
     * 数据源密码
     */
    String datasourcePassword;

    /**
     * 备注
     */
    String remark;


    /**
     * 数据库驱动
     */
    String driverClassName;

    /**
     * 是否启用[0:禁用 1:启用]
     */
    Integer enable;


    public boolean isEnable() {
        return enable==1;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public void setDatasourceUsername(String datasourceUsername) {
        this.datasourceUsername = datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public void setDatasourcePassword(String datasourcePassword) {
        this.datasourcePassword = datasourcePassword;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "DyDatasourceEntity{" +
                "id=" + id +
                ", datasourceName='" + datasourceName + '\'' +
                ", datasourceUrl='" + datasourceUrl + '\'' +
                ", datasourceUsername='" + datasourceUsername + '\'' +
                ", datasourcePassword='" + datasourcePassword + '\'' +
                ", remark='" + remark + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", enable=" + enable +
                '}';
    }
}
