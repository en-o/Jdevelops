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
	@Schema(description = "相对路径(访问前缀+桶+文件路径+文件名)")
	private String relativePath;

	/**
	 * 绝对路径(http链接)
	 */
	@Schema(description = "绝对路径(http链接)")
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


	/**
	 * 桶名称
	 */
	@Schema(description = "桶名称")
	private String bucket;

	/**
	 * 文件后缀
	 */
	@Schema(description = "文件后缀")
	private String suffix;

	/**
	 * 文件类型
	 */
	@Schema(description = "文件类型")
	private String contentType;


	/**
	 * 映射路径(local才有)
	 */
	@Schema(description = "映射路径(local才有)")
	private String contextPath;


	public FilePathResult() {
	}

	/**
	 *
	 * @param relativePath 相对路径(访问前缀+桶+文件路径+文件名)
	 * @param absolutePath 绝对路径(http链接)
	 * @param originalName 原本对象的名称
	 * @param freshName 文件服务器中的新文件名
	 * @param downPath 文件下载和删除用到的路径
	 * @param bucket 桶名称
	 * @param suffix 文件后缀
	 * @param contentType 文件类型
	 * @param contextPath 映射路径(local才有)
	 */
	public FilePathResult(String relativePath,
						  String absolutePath,
						  String originalName,
						  String freshName,
						  String downPath,
						  String bucket,
						  String suffix,
						  String contentType,
						  String contextPath) {
		this.relativePath = relativePath;
		this.absolutePath = absolutePath;
		this.originalName = originalName;
		this.freshName = freshName;
		this.downPath = downPath;
		this.bucket = bucket;
		this.suffix = suffix;
		this.contentType = contentType;
		this.contextPath = contextPath;
	}


	/**
	 *
	 * @param relativePath 相对路径(访问前缀+桶+文件路径+文件名)
	 * @param absolutePath 绝对路径(http链接)
	 * @param originalName 原本对象的名称
	 * @param freshName 文件服务器中的新文件名
	 * @param downPath 文件下载和删除用到的路径
	 * @param bucket 桶名称
	 * @param suffix 文件后缀
	 * @param contentType 文件类型
	 */
	public FilePathResult(String relativePath,
						  String absolutePath,
						  String originalName,
						  String freshName,
						  String downPath,
						  String bucket,
						  String suffix,
						  String contentType) {
		this.relativePath = relativePath;
		this.absolutePath = absolutePath;
		this.originalName = originalName;
		this.freshName = freshName;
		this.downPath = downPath;
		this.bucket = bucket;
		this.suffix = suffix;
		this.contentType = contentType;
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

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	@Override
	public String toString() {
		return "FilePathResult{" +
				"relativePath='" + relativePath + '\'' +
				", absolutePath='" + absolutePath + '\'' +
				", originalName='" + originalName + '\'' +
				", freshName='" + freshName + '\'' +
				", downPath='" + downPath + '\'' +
				", bucket='" + bucket + '\'' +
				", suffix='" + suffix + '\'' +
				", fileType='" + contentType + '\'' +
				", contextPath='" + contextPath + '\'' +
				'}';
	}
}
