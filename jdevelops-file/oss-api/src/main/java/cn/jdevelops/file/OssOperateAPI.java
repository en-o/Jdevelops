package cn.jdevelops.file;


import cn.jdevelops.file.bean.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
     */
    FilePathResult uploadFile(@Valid UploadDTO uploaded);

    /**
     * 文件下载
     * @param response response
     * @param download params
     */
    void downloadFile(HttpServletResponse response, @Valid DownloadDTO download);

    /**
     * 获取存在有效期的文件地址
     *
     * @param expireDate params
     * @return fileUrl
     */
    String expireDateUrl(@Valid ExpireDateDTO expireDate);


    /**
     * 删除文件s
     *
     * @param remove params
     */
    void removeFiles(@Valid RemoveFileDTO remove);

}
