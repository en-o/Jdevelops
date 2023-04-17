package cn.jdevelops.file.oss.api.config;


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


	public String getBrowseUrl() {
		return browseUrl;
	}

	public void setBrowseUrl(String browseUrl) {
		this.browseUrl = browseUrl;
	}

	public LocalConfig getLocal() {
		return local;
	}

	public void setLocal(LocalConfig local) {
		this.local = local;
	}

	public MinioConfig getMinio() {
		return minio;
	}

	public void setMinio(MinioConfig minio) {
		this.minio = minio;
	}

	public QiNiuConfig getQiniu() {
		return qiniu;
	}

	public void setQiniu(QiNiuConfig qiniu) {
		this.qiniu = qiniu;
	}

	@Override
	public String toString() {
		return "OSSConfig{" +
				"browseUrl='" + browseUrl + '\'' +
				", local=" + local +
				", minio=" + minio +
				", qiniu=" + qiniu +
				'}';
	}
}
