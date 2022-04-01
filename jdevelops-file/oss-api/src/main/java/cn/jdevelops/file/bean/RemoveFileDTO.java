package cn.jdevelops.file.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
     * 桶名称
     */
    @NotBlank
    String bucket;

    /**
     * 待删除的文件名集合（服务器中的新文件名）
     */
    @NotEmpty
    List<String> freshName;
}
