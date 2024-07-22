package cn.tannn.jdevelops.files.server.dao;

import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * 文件索引
 *
 * @author <a href="https://t.tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/7/4 下午11:04
 */
public class FileIndexMetaDaoImpl extends SimpleJpaRepository<FileIndexMeta, Long> implements FileIndexMetaDao {

    private final EntityManager entityManager;

    public FileIndexMetaDaoImpl(EntityManager entityManager) {
        super(FileIndexMeta.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public boolean existsByStorageId(Long storageId) {
        String jpql = "SELECT COUNT(f) FROM FileIndexMeta f WHERE f.storageId = :storageId";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("storageId", storageId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByStorageIdAndUrlSuffix(Long storageId, String urlSuffix) {
        String jpql = "SELECT COUNT(f) FROM FileIndexMeta f WHERE f.storageId = :storageId AND f.urlSuffix = :urlSuffix";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("storageId", storageId)
                .setParameter("urlSuffix", urlSuffix)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<FileIndexMeta> findByIdIn(List<Long> ids) {
        String jpql = "SELECT f FROM FileIndexMeta f WHERE f.id IN (:ids)";
        TypedQuery<FileIndexMeta> query = entityManager.createQuery(jpql, FileIndexMeta.class)
                .setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Optional<FileIndexMeta> findByPathAndStorage(String path, String storage) {
        String jpql = "SELECT f FROM FileIndexMeta f WHERE f.path = :path AND f.storage = :storage";
        TypedQuery<FileIndexMeta> query = entityManager.createQuery(jpql, FileIndexMeta.class)
                .setParameter("path", path)
                .setParameter("storage", storage);
        List<FileIndexMeta> resultList = query.getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    @Override
    public Optional<FileIndexMeta> findByPath(String path) {
        String jpql = "SELECT f FROM FileIndexMeta f WHERE f.path = :path";
        TypedQuery<FileIndexMeta> query = entityManager.createQuery(jpql, FileIndexMeta.class)
                .setParameter("path", path);
        List<FileIndexMeta> resultList = query.getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    @Override
    public Optional<FileIndexMeta> findByUrlSuffix(String urlSuffix) {
        String jpql = "SELECT f FROM FileIndexMeta f WHERE f.urlSuffix = :urlSuffix";
        TypedQuery<FileIndexMeta> query = entityManager.createQuery(jpql, FileIndexMeta.class)
                .setParameter("urlSuffix", urlSuffix);
        List<FileIndexMeta> resultList = query.getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }
}
