package cn.jdevelops.file;


import cn.jdevelops.file.bean.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 文件接口类
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 13:44
 */
public interface OssOperateAPI {

    /**
     * 上传文件
     *
     * @param uploaded params
     * @return cn.jdevelops.file.FilePathVO
     * @throws Exception exception
     */
    FilePathResult uploadFile(@Valid UploadDTO uploaded) throws Exception;


    /**
     * 上传文件
     *
     * @param uploaded params
     * @return cn.jdevelops.file.FilePathVO
     * @throws Exception exception
     */
    List<FilePathResult> uploadFile(@Valid UploadsDTO uploaded) throws Exception;

    /**
     * 文件下载
     *
     * @param response response
     * @param download params
     * @throws Exception exception
     */
    void downloadFile(HttpServletResponse response, @Valid DownloadDTO download) throws Exception;

    /**
     * 获取存在有效期的文件地址
     *
     * @param expireDate params
     * @return fileUrl
     * @throws Exception exception
     */
    String expireDateUrl(@Valid ExpireDateDTO expireDate) throws Exception;


    /**
     * 删除文件s
     *
     * @param remove params
     * @throws Exception exception
     */
    void removeFiles(@Valid RemoveFileDTO remove) throws Exception;

}
