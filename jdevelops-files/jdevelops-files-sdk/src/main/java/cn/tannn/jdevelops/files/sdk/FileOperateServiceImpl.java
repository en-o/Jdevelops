package cn.tannn.jdevelops.files.sdk;

import cn.tannn.cat.file.sdk.OssOperateAPI;
import cn.tannn.cat.file.sdk.api.UploadFile;
import cn.tannn.cat.file.sdk.api.UploadFiles;
import cn.tannn.cat.file.sdk.bean.FileIndex;
import cn.tannn.cat.file.sdk.bean.FileStorage;
import cn.tannn.cat.file.sdk.core.ftp.FtpOperate;
import cn.tannn.cat.file.sdk.core.local.LocalOperate;
import cn.tannn.cat.file.sdk.core.minio.MinioOperate;
import cn.tannn.cat.file.sdk.core.qiniu.QiNiuOperate;
import cn.tannn.cat.file.sdk.exception.FileException;
import cn.tannn.cat.file.sdk.exception.FileExceptionCode;
import cn.tannn.jdevelops.result.exception.ExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 文件操作实现
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/15 14:04
 */
@Service
public class FileOperateServiceImpl implements FileOperateService {

    private static final Logger LOG = LoggerFactory.getLogger(FileOperateServiceImpl.class);


    /**
     * 本地操作
     */
    private final OssOperateAPI localOperate;
    private final OssOperateAPI minioOperate;
    private final OssOperateAPI qiNiuOperate;
    private final FtpOperate ftpOperate;


    public FileOperateServiceImpl(
                                  LocalOperate localOperate,
                                  MinioOperate minioOperate,
                                  QiNiuOperate qiNiuOperate, FtpOperate ftpOperate
    ) {
        this.localOperate = localOperate;
        this.minioOperate = minioOperate;
        this.qiNiuOperate = qiNiuOperate;
        this.ftpOperate = ftpOperate;
    }

    @Override
    public FileIndex upload(UploadFile upload) throws IOException {
        FileStorageConfig master = fileStorageConfigDao.findMaster()
                .orElseThrow(() -> new FileException(FileExceptionCode.FILE_CONFIG_MASTER_NONENTITY));
        return upload(upload, master);
    }

    @Override
    public FileIndex upload(Long storageId, UploadFile upload) throws IOException {
        FileStorageConfig storageConfig = fileStorageConfigDao.findById(storageId)
                .orElseThrow(() -> new FileException(FILE_CONFIG_NONSUPPORT));
        return upload(upload, storageConfig);
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
                LOG.error("批量上传错误忽略", e);
            }
        });

    }


    @Override
    public void download(Long fileIndexId, HttpServletResponse response) {
        fileIndexMetaDao.findById(fileIndexId)
                .ifPresent(file -> {
                            FileIndex fileIndex = file.toFileIndex();
                            if (fileIndex.localStorage()) {
                                localOperate.downloadFile(response, fileIndex);
                            } else if (fileIndex.minioStorage()) {
                                minioOperate.downloadFile(response, fileIndex);
                            } else if (fileIndex.qiNiuStorage()) {
                                qiNiuOperate.downloadFile(response, fileIndex);
                            } else if (fileIndex.ftpStorage()) {
                                ftpOperate.downloadFile(response, fileIndex);
                            }
                        }
                );
    }

    @Override
    public void remove(Long fileIndexId) {

    }


    @Override
    public boolean remove(FileIndex fileIndex) {
        // 删除源文件
        boolean delete;
        if (fileIndex.localStorage()) {
            // local
            delete = localOperate.removeFile(fileIndex);
        } else if (fileIndex.minioStorage()) {
            // minio
            delete = minioOperate.removeFile(fileIndex);
        } else if (fileIndex.qiNiuStorage()) {
            // 七牛
            delete = qiNiuOperate.removeFile(fileIndex);
        } else if (fileIndex.ftpStorage()) {
            // ftp
            delete = ftpOperate.removeFile(fileIndex);
        } else {
            delete = false;
        }
        return delete;

    }

    /**
     * 上传
     * @param upload 文件信息
     * @param storageConfig 存储器信息
     * @return 文件索引
     * @throws IOException IOException
     */
    private FileIndex upload(UploadFile upload, FileStorageConfig storageConfig) throws IOException {
        FileStorage storage = storageConfig.toFileStorage();
        FileIndex fileIndex;
        if (storage.localConfig()) {
            fileIndex = localOperate.uploadFile(upload, storage);
        } else if (storage.minioConfig()) {
            fileIndex = minioOperate.uploadFile(upload, storage);
        } else if (storage.qiNiuConfig()) {
            fileIndex = qiNiuOperate.uploadFile(upload, storage);
        } else if (storage.ftpConfig()) {
            fileIndex = ftpOperate.uploadFile(upload, storage);
        } else {
            throw new FileException(new ExceptionCode(11002, "暂不支持[" + storageConfig.getName() + "]存储"));
        }
        //  判断重复，配置ID 和 path 重复的情况会清楚不在重复save
        FileIndex fileIndexMeta = FileIndex.toFileIndexMeta(fileIndex);
        if (!fileIndexMetaDao.existsByStorageIdAndUrlSuffix(storageConfig.getId(), fileIndex.getUrlSuffix())) {
            fileIndexMeta = fileIndexMetaDao.save(fileIndexMeta);
        }
        return fileIndexMeta;
    }
}
