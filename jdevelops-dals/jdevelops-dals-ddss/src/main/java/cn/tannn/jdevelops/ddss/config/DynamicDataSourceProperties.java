package cn.tannn.jdevelops.ddss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 多数据源属性（从applition.yml读取数据源配置）
 *
 * @author tan
 */
@ConfigurationProperties(prefix = "jdevelops.dynamic")
public class DynamicDataSourceProperties {
    /**
     *  tableName表中的数据库密码加密盐(16位) <br/>
     *  如果不满足16或者则会使用默认 (加密方式ObjectUtils.encryptAES，参考：DynamicDatasourceService.add)
     */
    String salt;

    /**
     * 数据源管理的表名[默认dy_datasource,可自定义][ps表结构是固定的（init_table_dy_datasource_mysql.sql）]
     */
    String tableName;

    public String getSalt() {
        if(salt == null || 16 != salt.length() ){
            return "salt1231212qadqw";
        }
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getTableName() {
        if(tableName == null || tableName.isEmpty()){
            return "dy_datasource";
        }
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

