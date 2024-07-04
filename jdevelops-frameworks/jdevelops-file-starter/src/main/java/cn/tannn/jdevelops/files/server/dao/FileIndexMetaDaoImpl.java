package cn.tannn.jdevelops.files.server.dao;

import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 文件索引
 *
 * @author <a href="https://t.tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/7/4 下午11:04
 */
public class FileIndexMetaDaoImpl  extends SimpleJpaRepository<FileIndexMeta, Long> implements FileIndexMetaDao{

    private final EntityManager entityManager;

    public FileIndexMetaDaoImpl(EntityManager entityManager) {
        super(FileIndexMeta.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public boolean existsByStorageId(Long configId) {
        return false;
    }

    @Override
    public boolean existsByStorageIdAndUrlSuffix(Long storageId, String urlSuffix) {
        return false;
    }

    @Override
    public List<FileIndexMeta> findByIdIn(List<Long> ids) {
        return Collections.emptyList();
    }

    @Override
    public Optional<FileIndexMeta> findByPathAndStorage(String path, String storage) {
        return Optional.empty();
    }

    @Override
    public Optional<FileIndexMeta> findByPath(String path) {
        return Optional.empty();
    }
}
