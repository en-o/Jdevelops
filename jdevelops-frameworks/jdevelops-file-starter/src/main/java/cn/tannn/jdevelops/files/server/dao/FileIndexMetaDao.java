package cn.tannn.jdevelops.files.server.dao;


import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import cn.tannn.jdevelops.jpa.repository.JpaBasicsRepository;

import java.util.List;
import java.util.Optional;

/**
 * 文件索引数据
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/7 14:10
 */
public  interface FileIndexMetaDao extends JpaBasicsRepository<FileIndexMeta, Long> {

    /**
     *  存储器配置被使用
     * @param storageId 存储器配置ID
     * @return true 使用中
     */
    boolean existsByStorageId(Long storageId);

    /**
     * 判断是否存在
     * @param storageId storageId
     * @param urlSuffix urlSuffix
     * @return true 存在
     */
    boolean existsByStorageIdAndUrlSuffix(Long storageId, String urlSuffix);

    /**
     * 查询存储记录
     * @param ids id
     * @return FileStorage
     */
    List<FileIndexMeta> findByIdIn(List<Long> ids);

    /**
     * 查询文件
     * @param path path
     * @param storage 字典值[Dict.value]
     * @return FileIndexMeta
     */
    Optional<FileIndexMeta> findByPathAndStorage(String path, String storage);

    /**
     * 查询文件
     * @param path path（不出意外这个是唯一的）
     * @return FileIndexMeta
     */
    Optional<FileIndexMeta> findByPath(String path);


    /**
     * 查询文件
     * @param urlSuffix urlSuffix（不出意外这个是唯一的）
     * @return FileIndexMeta
     */
    Optional<FileIndexMeta> findByUrlSuffix(String urlSuffix);
}
