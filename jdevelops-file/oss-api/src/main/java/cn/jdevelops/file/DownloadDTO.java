package cn.jdevelops.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 文件下载入参
 *
 * @author tn
 */
@Getter
@Setter
@ToString
public class DownloadDTO {

	/**
	 * 桶名称(最少5个字符，最大63个）
	 */
	@NotBlank
	@Size(min = 5, max = 63)
	String bucket;

	/**
	 * 文件名（服务器中的新文件名）
	 */
	@NotBlank
	String freshName;


}
