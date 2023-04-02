package cn.jdevelops.file.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
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
}
