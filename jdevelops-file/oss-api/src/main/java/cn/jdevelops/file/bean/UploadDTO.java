package cn.jdevelops.file.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 文件上传入参
 * @author tn
 */
@Getter
@Setter
@ToString
public class UploadDTO {

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
	 * 文件流
	 */
	MultipartFile file;

	/**
	 * 业务自定义存储文件名,为空则使用系统默认规则
	 * ps: minio 中文可能会有点的问题
	 */
	String fileName;
}
