package cn.jdevelops.file.oss.driver.minio;

import cn.jdevelops.file.oss.api.OssOperateAPI;
import cn.jdevelops.file.oss.api.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * minio
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:35
 */
public class MinioOperate implements OssOperateAPI {

    private static final Logger LOG = LoggerFactory.getLogger(MinioOperate.class);
    @Autowired
    private OperateFileUtil operateFileUtil;

    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) throws Exception {
        return operateFileUtil.uploadFile(uploaded.getFile(), uploaded.getFileName(), uploaded.getBucket(), uploaded.getChildFolder());
    }

    @Override
    public List<FilePathResult> uploadFile(UploadsDTO uploaded) throws Exception {
        ArrayList<FilePathResult> results = new ArrayList<>();
        uploaded.getFiles().forEach(file -> {
            try {
                UploadDTO uploadDTO = new UploadDTO();
                uploadDTO.setFile(file.getFile());
                uploadDTO.setFileName(file.getFileName());
                uploadDTO.setBucket(uploaded.getBucket());
                uploadDTO.setChildFolder(uploaded.getChildFolder());
                results.add(uploadFile(uploadDTO));
            }catch (Exception e){
                LOG.error("批量上传有数据报错，可忽略",e);
            }
        });
        return results;
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
