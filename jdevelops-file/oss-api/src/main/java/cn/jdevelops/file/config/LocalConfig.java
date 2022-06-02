package cn.jdevelops.file.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Configuration;

/**
 * local
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-05-04 19:05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Configuration
public class LocalConfig {

    /**
     * 使用localDriver时，文件上传的存放路径
     *  e.g E:/test/file
     */
    private String uploadDir;


}