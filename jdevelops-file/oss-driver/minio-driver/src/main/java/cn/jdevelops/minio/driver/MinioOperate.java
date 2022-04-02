package cn.jdevelops.minio.driver;

import cn.jdevelops.file.*;
import cn.jdevelops.file.bean.*;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) {
        return operateFileUtil.uploadFile(uploaded.getFile(), uploaded.getBucket(), uploaded.getChildFolder());
    }

    @SneakyThrows
    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download) {
        operateFileUtil.download(response,download.getBucket(),download.getChildFolder_FreshName());
    }

    @SneakyThrows
    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) {
        return operateFileUtil.getExpiryObjectUrl(expireDate.getBucket(), expireDate.getChildFolder_FreshName(), expireDate.getExpires());
    }

    @SneakyThrows
    @Override
    public void removeFiles(RemoveFileDTO remove) {
        if(remove.getChildFolder_FreshName().size()==1){
            operateFileUtil.removeObject(remove.getBucket(), remove.getChildFolder_FreshName().get(0));
        }
        operateFileUtil.removeObjects(remove.getBucket(), remove.getChildFolder_FreshName());
    }
}
