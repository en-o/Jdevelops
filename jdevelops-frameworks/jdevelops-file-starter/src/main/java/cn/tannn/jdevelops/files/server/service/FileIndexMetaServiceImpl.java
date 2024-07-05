package cn.tannn.jdevelops.files.server.service;

import cn.tannn.cat.file.sdk.enums.StorageDict;
import cn.tannn.jdevelops.files.server.dao.FileIndexMetaDao;
import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import cn.tannn.jdevelops.jpa.service.J2ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 文件索引数据
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/7 14:24
 */
@Service
public class FileIndexMetaServiceImpl extends J2ServiceImpl<FileIndexMetaDao,FileIndexMeta, Long> implements FileIndexMetaService {


    public FileIndexMetaServiceImpl() {
        super(FileIndexMeta.class);
    }


    @Override
    public boolean using(Long storageId) {
        return getJpaBasicsDao().existsByStorageId(storageId);
    }

    @Override
    public boolean repetition(Long storageId, String urlSuffix) {
        return getJpaBasicsDao().existsByStorageIdAndUrlSuffix(storageId, urlSuffix);
    }

    @Override
    public List<FileIndexMeta> findByIds(List<Long> ids) {
        return getJpaBasicsDao().findByIdIn(ids);
    }

    @Override
    public Optional<FileIndexMeta> findPathAndStorage(String path, StorageDict storage) {
        return getJpaBasicsDao().findByPathAndStorage(path, storage.getValue());
    }

}
