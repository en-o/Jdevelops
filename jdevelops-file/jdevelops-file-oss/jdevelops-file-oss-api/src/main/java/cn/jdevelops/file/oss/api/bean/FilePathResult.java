package cn.jdevelops.file.oss.api.bean;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 文件地址
 *
 * @author tn
 * @date 2021-03-03 14:43
 */
@Schema(description = "文件管理 - 文件VO")
public class FilePathResult implements Serializable {

	/**
	 * 相对路径(访问前缀+桶+文件路径+文件名)
	 */
	@Schema(description = "相对路径")
	private String relativePath;

	/**
	 * 绝对路径(http链接)
	 */
	@Schema(description = "绝对路径")
	private String absolutePath;

	/**
	 * 原本对象的名称
	 */
	@Schema(description = "原本对象的名称")
	private String originalName;

	/**
	 * 文件服务器中的新文件名
	 */
	@Schema(description = "文件服务器中的新文件名")
	private String freshName;


	/**
	 * 文件下载和删除用到的路径
	 */
	@Schema(description = "文件下载和删除用到的路径")
	private String downPath;


	public FilePathResult() {
	}

	public FilePathResult(String relativePath, String absolutePath, String originalName, String freshName) {
		this.relativePath = relativePath;
		this.absolutePath = absolutePath;
		this.originalName = originalName;
		this.freshName = freshName;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getFreshName() {
		return freshName;
	}

	public void setFreshName(String freshName) {
		this.freshName = freshName;
	}

	public String getDownPath() {
		return downPath;
	}

	public void setDownPath(String downPath) {
		this.downPath = downPath;
	}

	@Override
	public String toString() {
		return "FilePathResult{" +
				"relativePath='" + relativePath + '\'' +
				", absolutePath='" + absolutePath + '\'' +
				", originalName='" + originalName + '\'' +
				", freshName='" + freshName + '\'' +
				", downPath='" + downPath + '\'' +
				'}';
	}
}
