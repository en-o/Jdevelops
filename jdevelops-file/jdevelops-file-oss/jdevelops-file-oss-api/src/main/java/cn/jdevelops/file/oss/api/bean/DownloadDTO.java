package cn.jdevelops.file.oss.api.bean;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 文件下载入参
 *
 * @author tn
 */

@SuppressWarnings("all")
public class DownloadDTO {

	/**
	 * 桶名称(最少1个字符，最大63个）
	 * e.g. tn
	 */
	@NotBlank
	@Size(min = 1,max = 63)
	String bucket;


	/**
	 * 路径/文件名（相对路径+服务器中的新文件名） - 不包括桶级别
	 * e.g file/1.png
	 */
	@NotBlank
	String childFolder_FreshName;

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getChildFolder_FreshName() {
		return childFolder_FreshName;
	}

	public void setChildFolder_FreshName(String childFolder_FreshName) {
		this.childFolder_FreshName = childFolder_FreshName;
	}

	@Override
	public String toString() {
		return "DownloadDTO{" +
				"bucket='" + bucket + '\'' +
				", childFolder_FreshName='" + childFolder_FreshName + '\'' +
				'}';
	}
}
