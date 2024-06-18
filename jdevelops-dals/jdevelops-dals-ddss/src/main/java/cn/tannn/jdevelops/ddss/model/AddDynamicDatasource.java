package cn.tannn.jdevelops.ddss.model;

/**
 * 数据源
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 12:00
 */

public class AddDynamicDatasource {


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

    @Override
    public String toString() {
        return "AddDyDatasource{" +
                "datasourceName='" + datasourceName + '\'' +
                ", datasourceUrl='" + datasourceUrl + '\'' +
                ", datasourceUsername='" + datasourceUsername + '\'' +
                ", datasourcePassword='" + datasourcePassword + '\'' +
                ", remark='" + remark + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                '}';
    }
}
