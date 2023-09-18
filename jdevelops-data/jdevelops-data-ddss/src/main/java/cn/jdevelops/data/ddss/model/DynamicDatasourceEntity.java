package cn.jdevelops.data.ddss.model;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据源元信息
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 12:00
 */

public class DynamicDatasourceEntity implements RowMapper<DynamicDatasourceEntity> {

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


    @Override
    public DynamicDatasourceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        DynamicDatasourceEntity datasourceEntity = new DynamicDatasourceEntity();
        datasourceEntity.setDatasourceName(rs.getString("datasource_name"));
        datasourceEntity.setDatasourceUrl(rs.getString("datasource_url"));
        datasourceEntity.setDatasourceUsername(rs.getString("datasource_username"));
        datasourceEntity.setDatasourcePassword(rs.getString("datasource_password"));
        datasourceEntity.setDriverClassName(rs.getString("driver_class_name"));
        datasourceEntity.setEnable(rs.getInt("enable"));
        datasourceEntity.setRemark(rs.getString("remark"));
        return datasourceEntity;
    }


    public boolean isEnable() {
        return enable==1;
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
                "datasourceName='" + datasourceName + '\'' +
                ", datasourceUrl='" + datasourceUrl + '\'' +
                ", datasourceUsername='" + datasourceUsername + '\'' +
                ", datasourcePassword='" + datasourcePassword + '\'' +
                ", remark='" + remark + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", enable=" + enable +
                '}';
    }
}
