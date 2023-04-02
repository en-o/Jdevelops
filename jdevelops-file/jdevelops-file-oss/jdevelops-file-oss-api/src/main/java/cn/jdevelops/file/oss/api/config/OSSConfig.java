package cn.jdevelops.file.oss.api.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * MinIo文件配置
 *
 * @author lxw
 * @version V1.0
 * @date 2020/10/13
 **/
@ConfigurationProperties(prefix = "jdevelops.oss")
@Component
@Getter
@Setter
@ToString
@Accessors(chain = true)
@SuppressWarnings("all")
public class OSSConfig {

	/**
	 * 文件浏览地址:  https://sda.xx.com
	 * 注意最后不要有斜杠
	 */
	private String browseUrl;

	/**
	 * jdevelops-file-oss-driver-local
	 */
	@NestedConfigurationProperty
	private LocalConfig local;

	/**
	 * jdevelops-file-oss-driver-minio
	 */
	@NestedConfigurationProperty
	private MinioConfig minio;

	/**
	 * jdevelops-file-oss-driver-qiniu
	 */
	@NestedConfigurationProperty
	private QiNiuConfig qiniu;

}
