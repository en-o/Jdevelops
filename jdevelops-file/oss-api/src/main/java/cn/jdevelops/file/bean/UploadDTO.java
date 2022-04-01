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
	 * 桶名称(最少5个字符，最大63个）
	 */
	@NotBlank
	@Size(min = 5, max = 63)
	String bucket;

	/**
	 * 文件子目录(相对路径),为空则直接上传至桶的根目录下
	 */
	String childFolder;

	/**
	 * 文件流
	 */
	private MultipartFile file;
}
