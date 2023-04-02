package cn.jdevelops.file.bean;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件的文件参数
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-10 15:50
 */

public class UploadFileInfo {

    /**
     * 文件流
     */
    MultipartFile file;

    /**
     * 业务自定义存储文件名,为空则使用系统默认规则
     * ps: minio 中文可能会有点的问题
     */
    String fileName;


    @Override
    public String toString() {
        return "UploadFileInfo{" +
                "file=" + file +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
