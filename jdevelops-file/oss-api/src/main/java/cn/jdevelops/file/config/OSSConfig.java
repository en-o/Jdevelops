package cn.jdevelops.file.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
public class OSSConfig {
	/**
	 * 文件上传地址
	 */
	private String uploadUrl;
	/**
	 * 文件浏览地址: https://sda.xx.com
	 */
	private String browseUrl;

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

	/**
	 * 配置自己空间所在的区域： z0, z1, z2
	 * @see <a href="https://developer.qiniu.com/kodo/1671/region-endpoint-fq">七牛云</a>
	 */
	private String regionId;
}
