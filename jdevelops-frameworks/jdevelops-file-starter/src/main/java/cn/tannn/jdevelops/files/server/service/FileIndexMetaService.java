package cn.tannn.jdevelops.files.server.service;

import cn.tannn.cat.file.sdk.enums.StorageDict;
import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import cn.tannn.jdevelops.jpa.service.J2Service;

import java.util.List;
import java.util.Optional;

/**
 * 文件索引数据
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/7 14:12
 */
public interface FileIndexMetaService extends J2Service<FileIndexMeta> {

    /**
     * 是否使用中
     * @param storageId 存储器配置ID
     * @return true 使用中
     */
    boolean using(Long storageId);

    /**
     * 重复检验
     * @param storageId storageId
     * @param urlSuffix urlSuffix
     * @return true 存在
     */
    boolean repetition(Long storageId, String urlSuffix);


    /**
     * 查询存储
     * @param ids ids
     * @return FileStorage
     */
    List<FileIndexMeta> findByIds(List<Long> ids);


    /**
     * 查询 storage 相关的文件信息
     * @param path path
     * @param storage {@link StorageDict}
     * @return
     */
    Optional<FileIndexMeta> findPathAndStorage(String path, StorageDict storage);
}
