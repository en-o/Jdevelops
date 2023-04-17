package cn.jdevelops.util.core.file.files;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * 文件包装器，扩展文件对象
 * hutool的file包太多东西，我只要几个功能用来实现 hostsUtil
 * @author Looly
 *
 */
public class FileWrapper implements Serializable{
	private static final long serialVersionUID = 1L;

	protected File file;
	protected Charset charset;


	// ------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param file 文件
	 * @param charset 编码，
	 */
	public FileWrapper(File file, Charset charset) {
		this.file = file;
		this.charset = charset;
	}
	// ------------------------------------------------------- Constructor end

	// ------------------------------------------------------- Setters and Getters start start
	/**
	 * 获得文件
	 * @return 文件
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 设置文件
	 * @param file 文件
	 * @return 自身
	 */
	public FileWrapper setFile(File file) {
		this.file = file;
		return this;
	}

	/**
	 * 获得字符集编码
	 * @return 编码
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置字符集编码
	 * @param charset 编码
	 * @return 自身
	 */
	public FileWrapper setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}
}
