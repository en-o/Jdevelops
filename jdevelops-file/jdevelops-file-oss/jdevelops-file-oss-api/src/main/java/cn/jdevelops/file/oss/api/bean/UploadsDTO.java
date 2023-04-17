package cn.jdevelops.file.oss.api.bean;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 文件上传入参
 * @author tn
 */
public class UploadsDTO {

	/**
	 * 桶名称(最少1个字符，最大63个）
	 * ps: 七牛云此处必须写空间名
	 * e.g. tn
	 */
	@NotBlank
	@Size(min = 1,max = 63)
	String bucket;

	/**
	 * 文件子目录(相对路径),为空则直接上传至桶的根目录下
	 * 注意：最有一定要有斜杠
	 * e.g. test/tn/
	 */
	String childFolder;

	/**
	 * 多个文件批量上传
	 */
	List<UploadFileInfo> files;

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getChildFolder() {
		return childFolder;
	}

	public void setChildFolder(String childFolder) {
		this.childFolder = childFolder;
	}

	public List<UploadFileInfo> getFiles() {
		return files;
	}

	public void setFiles(List<UploadFileInfo> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "UploadsDTO{" +
				"bucket='" + bucket + '\'' +
				", childFolder='" + childFolder + '\'' +
				", files=" + files +
				'}';
	}
}
