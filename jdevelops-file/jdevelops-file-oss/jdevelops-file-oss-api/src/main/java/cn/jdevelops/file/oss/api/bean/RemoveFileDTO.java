package cn.jdevelops.file.oss.api.bean;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 删除文件的入参
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:28
 */
@Schema(description = "文件管理 - 删除文件")
public class RemoveFileDTO implements Serializable {

    /**
     * 桶名称(最少1个字符，最大63个）
     * e.g. tn
     */
    @NotBlank(message = "桶不能为空")
    @Size(min = 1, max = 63)
    @Schema(description = "桶名称(最少1个字符，最大63个）", requiredMode = Schema.RequiredMode.REQUIRED, example = "tan")
    String bucket;

    /**
     * 文件下载和删除用到的路径
     *  路径/文件名（相对路径+服务器中的新文件名） - 去除桶后的全路径
     * e.g file/1.png
     * 七牛：直接些文件名
     * minio: 路径/文件名
     */
    @Schema(description = "文件下载(FilePathResult.downPath)",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件不能为空")
    List<String> downPath;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public List<String> getDownPath() {
        return downPath;
    }

    public void setDownPath(List<String> downPath) {
        this.downPath = downPath;
    }

    @Override
    public String toString() {
        return "RemoveFileDTO{" +
                "bucket='" + bucket + '\'' +
                ", downPath=" + downPath +
                '}';
    }
}
