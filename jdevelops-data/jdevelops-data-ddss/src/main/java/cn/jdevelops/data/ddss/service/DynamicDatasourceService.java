package cn.jdevelops.data.ddss.service;

import cn.jdevelops.data.ddss.config.properties.DynamicDataSourceProperties;
import cn.jdevelops.data.ddss.core.DynamicDataSource;
import cn.jdevelops.data.ddss.exception.DynamicDataSourceException;
import cn.jdevelops.data.ddss.model.AddDynamicDatasource;
import cn.jdevelops.data.ddss.model.DynamicDatasourceEntity;
import cn.jdevelops.data.ddss.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.util.List;


/**
 * 数据源管理 - jdbc
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 13:28
 */
@Service
public class DynamicDatasourceService {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicDatasourceService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;



    /**
     * 查询所有可用的数据源
     */
    public List<DynamicDatasourceEntity> findEnable() {
        // 查询 enable = 1 的数据
        return jdbcTemplate.query("select * from dy_datasource where enable = 1 ", new DynamicDatasourceEntity());
    }


    /**
     * 查询可用的数据源
     *
     * @param dbName 数据源名 {@link DynamicDatasourceEntity#getDatasourceName()}
     * @return DyDatasourceEntity
     */
    public DynamicDatasourceEntity findEnable(String dbName) {
        // 查询 enable = 1 and  datasource_name = dbName 的数据
        try {
            String sql = "select * from dy_datasource where enable = 1 and datasource_name = ?";
            DynamicDatasourceEntity datasourceEntity = jdbcTemplate.queryForObject(sql, new DynamicDatasourceEntity(), new Object[]{dbName});
            return datasourceEntity;
        } catch (Exception e) {
            LOG.error("查询可用的数据源失败", e);
        }
        return null;
    }


    /**
     * 查询数据源
     *
     * @param dbName 数据源名 {@link DynamicDatasourceEntity#getDatasourceName()}
     * @return DyDatasourceEntity
     */
    public DynamicDatasourceEntity findDyDatasourceEntity(String dbName) {
        try {
            String sql = "select * from dy_datasource where datasource_name = ?";
            DynamicDatasourceEntity datasourceEntity = jdbcTemplate.queryForObject(sql, new DynamicDatasourceEntity(), new Object[]{dbName});
            return datasourceEntity;
        }catch (EmptyResultDataAccessException e){
            LOG.warn("查询不到数据");
        }
        return null;
    }


    /**
     * 判断数据源是否存在
     *
     * @param dbName 数据源名 {@link DynamicDatasourceEntity#getDatasourceName()}
     * @return DyDatasourceEntity
     */
    public boolean verifyExist(String dbName) {
        try {
            String sql = "select * from dy_datasource where datasource_name = ?";
            DynamicDatasourceEntity datasourceEntity = jdbcTemplate.queryForObject(sql, new DynamicDatasourceEntity(), new Object[]{dbName});
            return ObjectUtils.isNotBlank(datasourceEntity.getDatasourceName());
        }catch (EmptyResultDataAccessException e){
            LOG.warn("查询不到数据");

        }
        return false;
    }

    /**
     * 查询所有数据源
     */
    public List<DynamicDatasourceEntity> findAll() {
        return jdbcTemplate.query("select * from dy_datasource ", new DynamicDatasourceEntity());
    }


    /**
     * 删除数据源
     */
    public void delete(String datasourceName) {
        // 根据数据源名称删除数据源
        jdbcTemplate.update("delete from dy_datasource where datasource_name = ? ", new Object[]{datasourceName});
        // 刷新项目中的数据源连接
        DynamicDataSource.refreshDataSource(datasourceName);
    }


    /**
     * 新增数据源
     *
     * @param datasourceEntity DyDatasourceEntity
     */
    public void add(AddDynamicDatasource datasourceEntity) throws InvalidKeyException {
        // 1. 检验重复
        DynamicDatasourceEntity dyDatasourceEntity = findDyDatasourceEntity(datasourceEntity.getDatasourceName());
        if(dyDatasourceEntity!=null&& ObjectUtils.isNotBlank(dyDatasourceEntity.getDatasourceName())){
            throw DynamicDataSourceException.specialMessage(503, "数据源《"+datasourceEntity.getDatasourceName()+"》已存在");
        }

        String password = ObjectUtils.decryptAES(datasourceEntity.getDatasourcePassword(), dynamicDataSourceProperties.getSalt());
        String username = ObjectUtils.decryptAES(datasourceEntity.getDatasourceUsername(), dynamicDataSourceProperties.getSalt());
        // 2. 插入数据源数据
        jdbcTemplate.update("insert into dy_datasource(" +
                "datasource_name," +
                "datasource_url," +
                "datasource_username," +
                "datasource_password," +
                "remark," +
                "driver_class_name," +
                "enable) values (?,?,?,?,?,?,?)", preparedStatement -> {
            preparedStatement.setString(1, datasourceEntity.getDatasourceName());
            preparedStatement.setString(2, datasourceEntity.getDatasourceUrl());
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, datasourceEntity.getRemark());
            preparedStatement.setString(6, datasourceEntity.getDriverClassName());
            preparedStatement.setInt(7, 1);
        });
        // 3. 使得新增的数据源生效
        DynamicDataSource.setDataSource(datasourceEntity.getDatasourceName());
    }
}
