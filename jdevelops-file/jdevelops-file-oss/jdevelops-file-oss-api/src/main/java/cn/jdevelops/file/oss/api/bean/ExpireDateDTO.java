package cn.jdevelops.file.oss.api.bean;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 有效期文件入参
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:26
 */
@Schema(description = "文件管理 - 文件有效期")
public class ExpireDateDTO implements Serializable {

    /**
     * 桶名称(最少1个字符，最大63个）
     * e.g. tn
     */
    @NotBlank(message = "桶不能为空")
    @Size(min = 1, max = 63)
    @Schema(description = "桶名称(最少1个字符，最大63个）", requiredMode = Schema.RequiredMode.REQUIRED, example = "tan")
    String bucket;


    /**
     * 路径/文件名（相对路径+服务器中的新文件名） - 去除桶后的全路径
     * e.g file/1.png
     * 七牛：直接些文件名
     * minio: 路径/文件名
     */
    @Schema(description = "文件下载(FilePathResult.downPath)", requiredMode = Schema.RequiredMode.REQUIRED, example = "file/1.png")
    @NotBlank(message = "文件不能为空")
    String downPath;

    /**
     * 过期时间 失效时间（以秒为单位，最少1秒，最大604800即7天）
     */
    @Min(1)
    @Max(604800)
    @NotNull
    Integer expires;


    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDownPath() {
        return downPath;
    }

    public void setDownPath(String downPath) {
        this.downPath = downPath;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "ExpireDateDTO{" +
                "bucket='" + bucket + '\'' +
                ", downPath='" + downPath + '\'' +
                ", expires=" + expires +
                '}';
    }
}
