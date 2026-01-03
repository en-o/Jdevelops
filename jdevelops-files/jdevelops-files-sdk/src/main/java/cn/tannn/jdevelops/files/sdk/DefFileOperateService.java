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
import cn.tannn.jdevelops.files.sdk.config.OssConfig;
import cn.tannn.jdevelops.files.sdk.util.FileFilter;
import cn.tannn.jdevelops.files.sdk.util.UUIDUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


/**
 * 文件操作实现
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/15 14:04
 */
@Service
public class DefFileOperateService implements FileOperateService {

    private static final Logger LOG = LoggerFactory.getLogger(DefFileOperateService.class);


    /**
     * 本地操作
     */
    private final OssOperateAPI localOperate;
    private final OssOperateAPI minioOperate;
    private final OssOperateAPI qiNiuOperate;
    private final FtpOperate ftpOperate;

    /**
     * 配置文件
     */
    private final OssConfig ossConfig;


    public DefFileOperateService(
            LocalOperate localOperate,
            MinioOperate minioOperate,
            QiNiuOperate qiNiuOperate,
            FtpOperate ftpOperate,
            OssConfig ossConfig
    ) {
        this.localOperate = localOperate;
        this.minioOperate = minioOperate;
        this.qiNiuOperate = qiNiuOperate;
        this.ftpOperate = ftpOperate;
        this.ossConfig = ossConfig;
    }

    @Override
    public FileIndex upload(UploadFile upload) throws IOException {
        FileStorage fileStorage = ossConfig.genMasterStorage();
        return upload(upload, fileStorage);
    }

    @Override
    public FileIndex upload(Long storageId, UploadFile upload) throws IOException {
        FileStorage fileStorage = ossConfig.genMasterStorage(storageId);
        return upload(upload, fileStorage);
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
    public void download(FileIndex fileIndex, HttpServletResponse response) {
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
     *
     * @param upload 文件信息
     * @return 文件索引
     * @throws IOException IOException
     */
    private FileIndex upload(UploadFile upload, FileStorage storage) throws IOException {
        List<String> filter = ossConfig.getFilter();
        if (!filter.isEmpty()) {
            FileFilter.isValidFileTypeThrow(upload.getFile(), filter, ossConfig.getFilterModel());
        }
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
            throw FileException.specialMessage(11002, "暂不支持[" + storage.getName() + "]存储");
        }
        fileIndex.setId(UUIDUtils.getInstance().generateShortUuidLong());
        return fileIndex;
    }

}
