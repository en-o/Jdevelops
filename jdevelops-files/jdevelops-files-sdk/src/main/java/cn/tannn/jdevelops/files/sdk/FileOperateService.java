package cn.tannn.jdevelops.files.sdk;

import cn.tannn.cat.file.sdk.api.UploadFile;
import cn.tannn.cat.file.sdk.api.UploadFiles;
import cn.tannn.cat.file.sdk.bean.FileIndex;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 文件操作 RPC
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/15 11:55
 */
public interface FileOperateService {

    /**
     * master上传
     *
     * @param uploaded UploadDTO
     * @return Object
     * @throws IOException Exception
     */
    FileIndex upload(UploadFile uploaded) throws IOException;

    /**
     * 选择存储器上传
     *
     * @param uploaded  UploadDTO
     * @param storageId 存储器ID [ftp:1, local:2 , minio:3 , qiniu:4 ]
     * @return Object
     * @throws IOException Exception
     */
    FileIndex upload(Long storageId, UploadFile uploaded) throws IOException;


    /**
     * master 批量上传文件
     *
     * @param uploads UploadFiles
     */
    void uploads(UploadFiles uploads);


    /**
     * 下载文件
     *
     * @param fileIndex 文件info
     * @param response    HttpServletResponse
     */
    void download(FileIndex fileIndex, HttpServletResponse response);


    /**
     * 删除文件 - oss中的文件/index中的记录
     *
     * @param fileIndex FileIndexMeta
     */
    boolean remove(FileIndex fileIndex);
}
