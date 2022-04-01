package cn.jdevelops.file;


/**
 * 文件接口类
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 13:44
 */
public interface FileApi {

    /**
     * 上传文件
     * @param uploaded params
     * @return cn.jdevelops.file.FilePathVO
     */
    FilePathResult uploadFile(UploadDTO uploaded);

    /**
     * 文件下载
     * @param download params
     */
    void downloadFile(DownloadDTO download);

    /**
     * 获取存在有效期的文件地址
     * @param expireDate params
     * @return fileUrl
     */
    String expireDateUrl(ExpireDateDTO expireDate);


    /**
     * 删除文件s
     * @param remove params
     */
    void removeFiles(RemoveFileDTO remove);




}
