package cn.tannn.jdevelops.ddss.service;

import cn.tannn.jdevelops.ddss.config.DynamicDataSourceProperties;
import cn.tannn.jdevelops.ddss.core.DynamicDataSource;
import cn.tannn.jdevelops.ddss.exception.DynamicDataSourceException;
import cn.tannn.jdevelops.ddss.model.AddDynamicDatasource;
import cn.tannn.jdevelops.ddss.model.DynamicDatasourceEntity;
import cn.tannn.jdevelops.ddss.model.FixDynamicDatasource;
import cn.tannn.jdevelops.ddss.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return jdbcTemplate.query("select * from " + dynamicDataSourceProperties.getTableName() + " where enable = 1 ", new DynamicDatasourceEntity());
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
            String sql = "select * from " + dynamicDataSourceProperties.getTableName() + " where enable = 1 and datasource_name = ?";
            DynamicDatasourceEntity datasourceEntity = jdbcTemplate.queryForObject(sql, new DynamicDatasourceEntity(), new Object[]{dbName});
            return datasourceEntity;
        } catch (Exception e) {
            LOG.error("查询可用的数据源失败", e);
        }
        return null;
    }


    /**
     * 更新数据源启用状态
     *
     * @param datasourceName 数据源名称
     * @param enable         是否启用 (1:启用, 0:禁用)
     * @return 更新影响的行数
     */
    public int updateDataSourceStatus(String datasourceName, int enable) {
        String sql = "UPDATE " + dynamicDataSourceProperties.getTableName() + " SET enable = ? WHERE datasource_name = ?";
        int update = jdbcTemplate.update(sql, enable, datasourceName);
        // 刷新项目中的数据源连接
        if(enable == 1){
            DynamicDataSource.setDataSource(datasourceName);
        }else {
            DynamicDataSource.deleteDataSource(datasourceName);
        }
        return update;
    }


    /**
     * 查询数据源
     *
     * @param dbName 数据源名 {@link DynamicDatasourceEntity#getDatasourceName()}
     * @return DyDatasourceEntity
     */
    public DynamicDatasourceEntity findDyDatasourceEntity(String dbName) {
        try {
            String sql = "select * from " + dynamicDataSourceProperties.getTableName() + " where datasource_name = ?";
            DynamicDatasourceEntity datasourceEntity = jdbcTemplate.queryForObject(sql, new DynamicDatasourceEntity(), new Object[]{dbName});
            return datasourceEntity;
        } catch (EmptyResultDataAccessException e) {
            LOG.warn("查询不到数据:" + dbName);
        }
        return null;
    }


    /**
     * 判断数据源是否存在
     *
     * @param dbName 数据源名 {@link DynamicDatasourceEntity#getDatasourceName()}
     * @return true 存在
     */
    public boolean verifyExist(String dbName) {
        try {
            String sql = "select * from " + dynamicDataSourceProperties.getTableName() + " where datasource_name = ?";
            DynamicDatasourceEntity datasourceEntity = jdbcTemplate.queryForObject(sql, new DynamicDatasourceEntity(), new Object[]{dbName});
            return ObjectUtils.isNotBlank(datasourceEntity.getDatasourceName());
        } catch (EmptyResultDataAccessException e) {
            LOG.warn("不存在数据源:" + dbName);
        }
        return false;
    }

    /**
     * 查询所有数据源
     */
    public List<DynamicDatasourceEntity> findAll() {
        return jdbcTemplate.query("select * from " + dynamicDataSourceProperties.getTableName(), new DynamicDatasourceEntity());
    }


    /**
     * 删除数据源
     */
    public void delete(String datasourceName) {
        // 根据数据源名称删除数据源
        jdbcTemplate.update("delete from " + dynamicDataSourceProperties.getTableName() + " where datasource_name = ? ", new Object[]{datasourceName});
        // 刷新项目中的数据源连接
        DynamicDataSource.deleteDataSource(datasourceName);
    }


    /**
     * 新增数据源
     *
     * @param datasourceEntity DyDatasourceEntity
     */
    public void add(AddDynamicDatasource datasourceEntity) throws InvalidKeyException {
        // 1. 检验重复
        if (verifyExist(datasourceEntity.getDatasourceName())) {
            throw DynamicDataSourceException.specialMessage(503, "数据源《" + datasourceEntity.getDatasourceName() + "》已存在");
        }

        String password = ObjectUtils.encryptAES(datasourceEntity.getDatasourcePassword(), dynamicDataSourceProperties.getSalt());
        String username = ObjectUtils.encryptAES(datasourceEntity.getDatasourceUsername(), dynamicDataSourceProperties.getSalt());
        // 2. 插入数据源数据
        jdbcTemplate.update("insert into " + dynamicDataSourceProperties.getTableName() +
                "(datasource_name," +
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
        // 3. 使得新增的数据源生效 [添加源的时候就将他加入不用在 aop拦截的再去查库了，其实影响好像也不大]
        DynamicDataSource.setDataSource(datasourceEntity.getDatasourceName());
    }


    /**
     * 更新数据源配置
     * 使用合并策略：如果传入的属性为null，则保留原有值
     *
     * @param dataSourceEntity 待更新的数据源实体
     * @return 更新影响的行数
     */
    public int updateDataSource(FixDynamicDatasource dataSourceEntity) throws InvalidKeyException {
        // 先查询是否存在
        DynamicDatasourceEntity existingDataSource = findDyDatasourceEntity(dataSourceEntity.getDatasourceName());
        if (existingDataSource == null) {
            // 如果不存在，可以选择抛出异常或返回0
            throw new DynamicDataSourceException("数据源不存在：" + dataSourceEntity.getDatasourceName());
        }
        // 合并更新值
        DynamicDatasourceEntity mergedEntity = mergeDataSource(existingDataSource, dataSourceEntity);

        // 执行更新
        String sql = "UPDATE " + dynamicDataSourceProperties.getTableName() + " SET " +
                "datasource_url = ?, " +
                "datasource_username = ?, " +
                "datasource_password = ?, " +
                "driver_class_name = ?, " +
                "remark = ? " +
                "WHERE datasource_name = ?";

        int update = jdbcTemplate.update(
                sql,
                mergedEntity.getDatasourceUrl(),
                mergedEntity.getDatasourceUsername(),
                mergedEntity.getDatasourcePassword(),
                mergedEntity.getDriverClassName(),
                mergedEntity.getRemark(),
                mergedEntity.getDatasourceName()
        );
        // 刷新项目中的数据源连接
        DynamicDataSource.renewalDataSource(mergedEntity.getDatasourceName());
        return update;
    }

    /**
     * 合并数据源信息
     *
     * @param existing 已存在的数据源
     * @param update   待更新的数据源
     * @return 合并后的数据源实体
     */
    private DynamicDatasourceEntity mergeDataSource(DynamicDatasourceEntity existing, FixDynamicDatasource update) throws InvalidKeyException {
        DynamicDatasourceEntity data = new DynamicDatasourceEntity();
        // 主键不允许更新
        data.setDatasourceName(existing.getDatasourceName());
        data.setDatasourceUrl(ObjectUtils.isNotBlank(update.getDatasourceUrl()) ? update.getDatasourceUrl() : existing.getDatasourceUrl());

        if(ObjectUtils.isNotBlank(update.getDatasourceUsername())){
            String username = ObjectUtils.encryptAES(update.getDatasourceUsername(), dynamicDataSourceProperties.getSalt());
            data.setDatasourceUsername(username);
        }else {
            data.setDatasourceUsername(existing.getDatasourceUsername());
        }

        if(ObjectUtils.isNotBlank(update.getDatasourcePassword())){
            String password = ObjectUtils.encryptAES(update.getDatasourcePassword(), dynamicDataSourceProperties.getSalt());
            data.setDatasourcePassword(password);
        }else {
            data.setDatasourcePassword(existing.getDatasourcePassword());
        }

        data.setRemark(ObjectUtils.isNotBlank(update.getDriverClassName()) ? update.getDriverClassName() : existing.getDriverClassName());
        data.setDriverClassName(ObjectUtils.isNotBlank(update.getRemark()) ? update.getRemark() : existing.getRemark());
        data.setEnable(existing.getEnable());
        return data;
    }
}

