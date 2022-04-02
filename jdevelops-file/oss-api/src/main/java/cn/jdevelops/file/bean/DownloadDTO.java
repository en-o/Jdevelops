package cn.jdevelops.file.bean;

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
	 * 路径/文件名（相对路径+服务器中的新文件名） - 不包括桶级别
	 */
	@NotBlank
	String childFolder_FreshName;


}
