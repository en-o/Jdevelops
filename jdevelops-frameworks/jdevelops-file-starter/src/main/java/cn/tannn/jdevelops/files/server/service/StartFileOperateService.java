package cn.tannn.jdevelops.files.server.service;

import cn.tannn.cat.file.sdk.api.UploadFile;
import cn.tannn.cat.file.sdk.api.UploadFiles;
import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件操作
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/4 下午2:29
 */
public interface StartFileOperateService {

    /**
     * 上传
     *
     * @param uploaded UploadDTO
     * @return Object
     * @throws IOException Exception
     */
    FileIndexMeta upload(UploadFile uploaded) throws IOException;

    /**
     * 上传
     *
     * @param uploaded  UploadDTO
     * @param storageId 存储器ID
     * @return Object
     * @throws IOException Exception
     */
    FileIndexMeta upload(Long storageId, UploadFile uploaded) throws IOException;


    /**
     * 上传
     *
     * @param uploads UploadFiles
     */
    void uploads(UploadFiles uploads);


    /**
     * 下载
     *
     * @param fileIndexId 文件索引ID
     * @param response    HttpServletResponse
     */
    void download(Long fileIndexId, HttpServletResponse response);


    /**
     * 删除文件 - oss中的文件/index中的记录
     *
     * @param fileIndexId 文件索引ID
     */
    void remove(Long fileIndexId);

    /**
     * 删除文件 - oss中的文件/index中的记录
     *
     * @param fileIndex FileIndexMeta
     */
    void remove(FileIndexMeta fileIndex);

    /**
     * 删除文件
     * @param path 文件路径[FileIndexMeta.path]
     */
    void removeByPath(String path);
}
