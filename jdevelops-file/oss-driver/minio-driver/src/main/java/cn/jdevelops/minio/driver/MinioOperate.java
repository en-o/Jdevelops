package cn.jdevelops.minio.driver;

import cn.jdevelops.file.*;
import cn.jdevelops.file.bean.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;

/**
 * minio
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:35
 */
public class MinioOperate implements OssOperateAPI {

    @Autowired
    private OperateFileUtil operateFileUtil;

    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) throws Exception {
        return operateFileUtil.uploadFile(uploaded.getFile(), uploaded.getFileName(), uploaded.getBucket(), uploaded.getChildFolder());
    }


    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download) throws Exception {
        operateFileUtil.download(response,download.getBucket(),download.getChildFolder_FreshName());
    }


    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) throws Exception {
        return operateFileUtil.getExpiryObjectUrl(expireDate.getBucket(), expireDate.getChildFolder_FreshName(), expireDate.getExpires());
    }


    @Override
    public void removeFiles(RemoveFileDTO remove) throws Exception {
        operateFileUtil.removeObjects(remove.getBucket(), remove.getChildFolder_FreshName());
    }
}
