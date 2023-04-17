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
     * 路径/文件名（相对路径+服务器中的新文件名） - 不包括桶级别
     */
    @NotEmpty
    List<String> childFolder_FreshName;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public List<String> getChildFolder_FreshName() {
        return childFolder_FreshName;
    }

    public void setChildFolder_FreshName(List<String> childFolder_FreshName) {
        this.childFolder_FreshName = childFolder_FreshName;
    }

    @Override
    public String toString() {
        return "RemoveFileDTO{" +
                "bucket='" + bucket + '\'' +
                ", childFolder_FreshName=" + childFolder_FreshName +
                '}';
    }
}
