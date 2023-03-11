package cn.jdevelops.file.config;

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
	 * local-driver
	 */
	@NestedConfigurationProperty
	private LocalConfig local;

	/**
	 * minio-driver
	 */
	@NestedConfigurationProperty
	private MinioConfig minio;

	/**
	 * qiniu-driver
	 */
	@NestedConfigurationProperty
	private QiNiuConfig qiniu;

}
