package cn.jdevelops.file.oss.api.bean;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 删除文件的入参
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:28
 */

public class RemoveFileDTO {

    /**
     * 桶名称(最少1个字符，最大63个）
     * e.g. tn
     */
    @NotBlank
    @Size(min = 1,max = 63)
    String bucket;

    /**
     * 文件下载和删除用到的路径
     */
    @NotEmpty
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
