package cn.jdevelops.minio.driver;

import cn.jdevelops.file.*;
import cn.jdevelops.file.bean.*;

/**
 * minio
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:35
 */
public class MinioFile implements FileApi {

    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) {
        System.out.println("minio");
        return null;
    }

    @Override
    public void downloadFile(DownloadDTO download) {
        System.out.println("minio");
    }

    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) {
        System.out.println("minio");
        return null;
    }

    @Override
    public void removeFiles(RemoveFileDTO remove) {
        System.out.println("minio");
    }
}
