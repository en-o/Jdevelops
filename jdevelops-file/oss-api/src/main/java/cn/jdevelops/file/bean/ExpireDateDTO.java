package cn.jdevelops.file.bean;

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
     * 桶名称(最少5个字符，最大63个）
     */
    @NotBlank
    @Size(min = 5, max = 63)
    String bucket;

    /**
     * 文件名（服务器中的新文件名）
     */
    @NotBlank
    String freshName;

    /**
     * 过期时间 失效时间（以秒为单位，最少1秒，最大604800即7天）
     */
    @Min(1)
    @Max(604800)
    @NotNull
    Integer expires;
}
