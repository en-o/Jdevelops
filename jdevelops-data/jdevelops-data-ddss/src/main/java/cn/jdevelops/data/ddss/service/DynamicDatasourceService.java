package cn.jdevelops.data.ddss.service;

import cn.jdevelops.data.ddss.core.DynamicDataSource;
import cn.jdevelops.data.ddss.model.AddDynamicDatasource;
import cn.jdevelops.data.ddss.model.DynamicDatasourceEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * 数据源管理
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 13:28
 */
@Service
public class DynamicDatasourceService {

    /**
     * 查询所有可用的数据源
     */
    public List<DynamicDatasourceEntity> findEnable() {
        // 查询 enable = 1 的数据
        return Collections.emptyList();
    }


    /**
     * 查询可用的数据源
     *
     * @param dbName 数据源名 {@link DynamicDatasourceEntity#getDatasourceName()}
     * @return DyDatasourceEntity
     */
    public DynamicDatasourceEntity findEnable(String dbName) {
        // 查询 enable = 1 and  datasource_name = dbName 的数据
        return new DynamicDatasourceEntity();
    }


    /**
     * 查询数据源
     *
     * @param dbName 数据源名 {@link DynamicDatasourceEntity#getDatasourceName()}
     * @return DyDatasourceEntity
     */
    public DynamicDatasourceEntity findDyDatasourceEntity(String dbName) {
        // 1. 根据数据源名查询数据源元信息
        return new DynamicDatasourceEntity();
    }

    /**
     * 查询所有数据源
     */
    public List<DynamicDatasourceEntity> findAll() {
        // 查询所有数据源
        return Collections.emptyList();
    }


    /**
     * 删除数据源
     */
    public void delete(String datasourceName) {
        // 根据数据源名称删除数据源
        // 刷新项目中的数据源连接
        DynamicDataSource.refreshDataSource(datasourceName);
    }


    /**
     * 新增数据源
     *
     * @param datasourceEntity DyDatasourceEntity
     */
    public void add(AddDynamicDatasource datasourceEntity) {
        // 1. 检验重复
        // 2. 插入数据源数据
        // 3. 使得新增的数据源生效
        DynamicDataSource.setDataSource(datasourceEntity.getDatasourceName());
    }
}
