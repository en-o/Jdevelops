package cn.jdevelops.file;

import lombok.*;

/**
 * 文件地址
 *
 * @author tn
 * @date 2021-03-03 14:43
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilePathResult {

	/**
	 * 相对路径
	 */
	private String relativePath;

	/**
	 * 绝对路径
	 */
	private String absolutePath;

	/**
	 * 原本对象的名称
	 */
	private String originalName;

	/**
	 * 文件服务器中的新文件名
	 */
	private String freshName;


}
