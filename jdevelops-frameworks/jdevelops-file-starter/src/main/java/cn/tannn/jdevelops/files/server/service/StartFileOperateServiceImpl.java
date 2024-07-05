package cn.tannn.jdevelops.files.server.service;

import cn.tannn.cat.file.sdk.api.UploadFile;
import cn.tannn.cat.file.sdk.api.UploadFiles;
import cn.tannn.cat.file.sdk.bean.FileIndex;
import cn.tannn.jdevelops.files.sdk.FileOperateService;
import cn.tannn.jdevelops.files.server.dao.FileIndexMetaDao;
import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件操作
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/4 下午2:29
 */
@Service
public class StartFileOperateServiceImpl implements StartFileOperateService {

    private static final Logger logger = LoggerFactory.getLogger(StartFileOperateServiceImpl.class);
    private final FileOperateService fileOperateService;
    private final FileIndexMetaDao fileIndexMetaDao;

    public StartFileOperateServiceImpl(FileOperateService fileOperateService, FileIndexMetaDao fileIndexMetaDao) {
        this.fileOperateService = fileOperateService;
        this.fileIndexMetaDao = fileIndexMetaDao;
    }

    @Override
    public FileIndexMeta upload(UploadFile uploaded) throws IOException {
        FileIndex fileIndex = fileOperateService.upload(uploaded);
        return saveFileIndexMeta(fileIndex);
    }

    @Override
    public FileIndexMeta upload(Long storageId, UploadFile uploaded) throws IOException {
        FileIndex fileIndex = fileOperateService.upload(storageId, uploaded);
        return saveFileIndexMeta(fileIndex);
    }

    @Override
    public void uploads(UploadFiles uploads) {
        uploads.getFiles().forEach(files -> {
            try {
                UploadFile uploadFile = new UploadFile();
                uploadFile.setBucket(uploads.getBucket());
                uploadFile.setChildFolder(uploads.getChildFolder());
                uploadFile.setFile(files.getFile());
                uploadFile.setFileName(files.getFileName());
                upload(uploadFile);
            } catch (Exception e) {
                logger.error("批量上传错误忽略", e);
            }
        });
    }

    @Override
    public void download(Long fileIndexId, HttpServletResponse response) {
        fileIndexMetaDao.findById(fileIndexId).ifPresent(fi -> {
            fileOperateService.download(fi.toFileIndex(), response);
        });
    }

    @Override
    public void remove(Long fileIndexId) {
        // 删除源文件/索引
        fileIndexMetaDao.findById(fileIndexId)
                .ifPresent(this::remove);
    }

    @Override
    public void remove(FileIndexMeta fileIndex) {
        boolean remove = fileOperateService.remove(fileIndex.toFileIndex());
        // 删除索引记录
        if (remove) {
            fileIndexMetaDao.deleteById(fileIndex.getId());
        }
    }

    @Override
    public void removeByPath(String path) {
        fileIndexMetaDao.findByPath(path).ifPresent(this::remove);
    }


    /**
     * 保存文件索引
     *
     * @param fileIndex 文件索引
     * @return 数据库信息
     * @throws IOException IOException
     */
    private FileIndexMeta saveFileIndexMeta(FileIndex fileIndex) throws IOException {
        //  判断重复，配置ID 和 path 重复的情况会清楚不在重复save
        FileIndexMeta fileIndexMeta = FileIndexMeta.toFileIndexMeta(fileIndex);
        if (!fileIndexMetaDao.existsByStorageIdAndUrlSuffix(fileIndex.getStorageId(), fileIndex.getUrlSuffix())) {
            fileIndexMeta = fileIndexMetaDao.save(fileIndexMeta);
        }
        return fileIndexMeta;
    }
}
