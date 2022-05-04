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


}
