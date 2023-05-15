package cn.jdevelops.file.oss.api.bean;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 文件下载入参
 *
 * @author tn
 */

@Schema(description = "文件管理 - 文件下载")
public class DownloadDTO {

	/**
	 * 桶名称(最少1个字符，最大63个）
	 * e.g. tn
	 */
	@NotBlank(message = "桶不能为空")
	@Size(min = 1, max = 63)
	@Schema(description = "桶名称(最少1个字符，最大63个）", requiredMode = Schema.RequiredMode.REQUIRED, example = "tan")
	String bucket;


	/**
	 * 路径/文件名（相对路径+服务器中的新文件名） - 去除桶后的全路径
	 * e.g file/1.png
	 */
	@Schema(description = "文件下载(FilePathResult.downPath)", requiredMode = Schema.RequiredMode.REQUIRED, example = "file/1.png")
	@NotBlank(message = "文件不能为空")
	String downPath;

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getDownPath() {
		return downPath;
	}

	public void setDownPath(String downPath) {
		this.downPath = downPath;
	}

	@Override
	public String toString() {
		return "DownloadDTO{" +
				"bucket='" + bucket + '\'' +
				", downPath='" + downPath + '\'' +
				'}';
	}
}
