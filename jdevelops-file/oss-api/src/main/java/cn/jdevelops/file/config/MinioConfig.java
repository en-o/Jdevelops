package cn.jdevelops.file.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Configuration;

/**
 * minio
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
public class MinioConfig {

    /**
     * 文件上传地址 尽量是域名
     *  e.g <p><a href="www.file.com">www.file.com</a></p>
     */
    private String uploadUrl;


    /**
     * 是否使用https
     */
    private Boolean https = false;

    /**
     * 可访问端口 （IP时用）
     */
    private Integer port;

    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码。
     */
    private String secretKey;


}
