package cn.tannn.jdevelops.utils.core.file.files;



import cn.tannn.jdevelops.utils.core.file.FileUtil;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 文件读取器
 * hutool的file包太多东西，我只要几个功能用来实现 hostsUtil
 * @author Looly
 *
 */
public class FileReader extends FileWrapper {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建 FileReader
	 * @param file 文件
	 * @param charset 编码，
	 * @return FileReader
	 */
	public static FileReader create(File file, Charset charset) throws IOException {
		return new FileReader(file, charset);
	}

	/**
	 * 构造
	 * @param file 文件
	 * @param charset 编码，
	 */
	public FileReader(File file, Charset charset) throws IOException {
		super(file, charset);
		checkFile();
	}

	/**
	 * 检查文件
	 *
	 * @throws IOException IO异常
	 */
	private void checkFile() throws IOException {
		if (!file.exists()) {
			throw new IOException("File not exist: " + file);
		}
		if (!file.isFile()) {
			throw new IOException("Not a file:" + file);
		}
	}



	/**
	 * 从文件中读取每一行数据
	 *
	 * @param <T> 集合类型
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IOException IO异常
	 */
	public <T extends Collection<String>> T readLines(T collection) throws IOException {
		BufferedReader reader = null;
		try {
			reader = FileUtil.getReader(file, charset);
			String line;
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				collection.add(line);
			}
			return collection;
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			close(reader);
		}
	}


	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 *
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}


}
