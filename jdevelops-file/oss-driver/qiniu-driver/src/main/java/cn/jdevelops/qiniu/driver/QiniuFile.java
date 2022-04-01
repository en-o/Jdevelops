package cn.jdevelops.qiniu.driver;

import cn.jdevelops.file.*;

/**
 * 七牛云上传下载文件
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:57
 */
public class QiniuFile implements FileApi {
    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) {
        System.out.println("qiniu");
        return null;
    }

    @Override
    public void downloadFile(DownloadDTO download) {
        System.out.println("qiniu");
    }

    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) {
        System.out.println("qiniu");
        return null;
    }

    @Override
    public void removeFiles(RemoveFileDTO remove) {
        System.out.println("qiniu");
    }
}
