package cn.jdevelops.file.oss.api.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

/**
 * 有效期文件入参
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:26
 */
@Getter
@Setter
@ToString
public class ExpireDateDTO {

    /**
     * 桶名称(最少1个字符，最大63个）
     * e.g. tn
     */
    @NotBlank
    @Size(min = 1,max = 63)
    String bucket;


    /**
     * 路径/文件名（相对路径+服务器中的新文件名） - 不包括桶级别
     * 七牛：直接些文件名
     * minio: 路径/文件名
     */
    @NotBlank
    String childFolder_FreshName;

    /**
     * 过期时间 失效时间（以秒为单位，最少1秒，最大604800即7天）
     */
    @Min(1)
    @Max(604800)
    @NotNull
    Integer expires;
}
