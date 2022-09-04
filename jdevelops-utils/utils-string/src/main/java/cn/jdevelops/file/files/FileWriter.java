package cn.jdevelops.file.files;


import cn.jdevelops.file.FileUtil;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 文件写入器
 * hutool的file包太多东西，我只要几个功能用来实现 hostsUtil
 * @author Looly
 */
public class FileWriter extends FileWrapper {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建 FileWriter
	 *
	 * @param file    文件
	 * @param charset 编码
	 * @return FileWriter
	 */
	public static FileWriter create(File file, Charset charset) throws IOException {
		return new FileWriter(file, charset);
	}
	/**
	 * 构造
	 *
	 * @param file    文件
	 * @param charset 编码，
	 */
	public FileWriter(File file, Charset charset) throws IOException {
		super(file, charset);
		checkFile();
	}

	/**
	 * 检查文件
	 *
	 * @throws IOException IO异常
	 */
	private void checkFile() throws IOException {
		Assert.notNull(file, "File to write content is null !");
		if (this.file.exists() && !file.isFile()) {
			throw new IOException("File [{}] is not a file !"+this.file.getAbsoluteFile());
		}
	}

	/**
	 * 将列表写入文件
	 *
	 * @param <T>      集合元素类型
	 * @param list     列表
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IOException IO异常
	 */
	public <T> File writeLines(Iterable<T> list, boolean isAppend) throws IOException {
		return writeLines(list, null, isAppend);
	}



	/**
	 * 将列表写入文件
	 *
	 * @param <T>           集合元素类型
	 * @param list          列表
	 * @param lineSeparator 换行符枚举（Windows、Mac或Linux换行符）
	 * @param isAppend      是否追加
	 * @return 目标文件
	 * @throws IOException IO异常
	 * @since 3.1.0
	 */
	public <T> File writeLines(Iterable<T> list, LineSeparator lineSeparator, boolean isAppend) throws IOException {
		try (PrintWriter writer = getPrintWriter(isAppend)) {
			boolean isFirst = true;
			for (T t : list) {
				if (null != t) {
					if(isFirst){
						isFirst = false;
						if(isAppend && FileUtil.isNotEmpty(this.file)){
							// 追加模式下且文件非空，补充换行符
							printNewLine(writer, lineSeparator);
						}
					} else{
						printNewLine(writer, lineSeparator);
					}
					writer.print(t);

					writer.flush();
				}
			}
		}
		return this.file;
	}


	/**
	 * 获得一个打印写入对象，可以有print
	 *
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IOException IO异常
	 */
	public PrintWriter getPrintWriter(boolean isAppend) throws IOException {
		return new PrintWriter(getWriter(isAppend));
	}
	/**
	 * 获得一个带缓存的写入对象
	 *
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IOException IO异常
	 */
	public BufferedWriter getWriter(boolean isAppend) throws IOException {
		try {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileUtil.touch(file), isAppend), charset));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}


	/**
	 * 打印新行
	 *
	 * @param writer        Writer
	 * @param lineSeparator 换行符枚举
	 * @since 4.0.5
	 */
	private void printNewLine(PrintWriter writer, LineSeparator lineSeparator) {
		if (null == lineSeparator) {
			//默认换行符
			writer.println();
		} else {
			//自定义换行符
			writer.print(lineSeparator.getValue());
		}
	}
}
