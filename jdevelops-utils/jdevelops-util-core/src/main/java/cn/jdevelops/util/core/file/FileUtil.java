package cn.jdevelops.util.core.file;


import cn.jdevelops.util.core.file.files.FileReader;
import cn.jdevelops.util.core.file.files.FileWriter;
import cn.jdevelops.util.core.list.CollectionUtil;
import cn.jdevelops.util.core.thread.ThreadUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 文件工具类
 *
 * @author looly
 */
public class FileUtil {


	/**
	 * 从文件中读取每一行数据
	 *
	 * @param file    文件
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IOException IO异常
	 */
	public static List<String> readLines(File file, Charset charset) throws IOException {
		return readLines(file, charset, new ArrayList<>());
	}

	/**
	 * 将列表写入文件，覆盖模式
	 *
	 * @param <T>     集合元素类型
	 * @param list    列表
	 * @param file    文件
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IOException IO异常
	 * @since 4.2.0
	 */
	public static <T> File writeLines(Collection<T> list, File file, Charset charset) throws IOException {
		return writeLines(list, file, charset, false);
	}



	/**
	 * 将列表写入文件，追加模式，策略为：
	 * <ul>
	 *     <li>当文件为空，从开头追加，尾部不加空行</li>
	 *     <li>当有内容，换行追加，尾部不加空行</li>
	 *     <li>当有内容，并末尾有空行，依旧换行追加</li>
	 * </ul>
	 *
	 * @param <T>     集合元素类型
	 * @param list    列表
	 * @param file    文件
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IOException IO异常
	 * @since 3.1.2
	 */
	public static <T> File appendLines(Collection<T> list, File file, Charset charset) throws IOException {
		return writeLines(list, file, charset, true);
	}




	/**
	 * 从文件中读取每一行数据
	 *
	 * @param <T>        集合类型
	 * @param file       文件路径
	 * @param charset    字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IOException IO异常
	 */
	public static <T extends Collection<String>> T readLines(File file, Charset charset, T collection) throws IOException {
		return FileReader.create(file, charset).readLines(collection);
	}

	/**
	 * 将列表写入文件
	 *
	 * @param <T>      集合元素类型
	 * @param list     列表
	 * @param file     文件
	 * @param charset  字符集
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IOException IO异常
	 */
	public static <T> File writeLines(Collection<T> list, File file, Charset charset, boolean isAppend) throws IOException {
		return FileWriter.create(file, charset).writeLines(list, isAppend);
	}



	/**
	 * 目录是否为空
	 *
	 * @param file 目录
	 * @return 是否为空，当提供非目录时，返回false
	 */
	public static boolean isNotEmpty(File file) {
		return !isEmpty(file);
	}


	/**
	 * 文件是否为空<br>
	 * 目录：里面没有文件时为空 文件：文件大小为0时为空
	 *
	 * @param file 文件
	 * @return 是否为空，当提供非目录时，返回false
	 */
	public static boolean isEmpty(File file) {
		if (null == file || !file.exists()) {
			return true;
		}

		if (file.isDirectory()) {
			String[] subFiles = file.list();
			return CollectionUtil.isEmpty(subFiles);
		} else if (file.isFile()) {
			return file.length() <= 0;
		}

		return false;
	}


	/**
	 * 将文件转换成Byte数组
	 *
	 * @param pathStr 文档
	 */
	public static byte[] fileConvertToByteArray(String pathStr) {
		File file = new File(pathStr);
		return fileConvertToByteArray(file);
	}


	/**
	 * 将文件转换成Byte数组
	 *
	 * @param file 文件
	 */
	public static byte[] fileConvertToByteArray(File file) {
		try(FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {

			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}





	/**
	 * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 *
	 * @param file 文件对象
	 * @return 文件，若路径为null，返回null
	 * @throws IOException IO异常
	 */
	public static File touch(File file) throws IOException {
		if (null == file) {
			return null;
		}
		if (!file.exists()) {
			mkParentDirs(file);
			try {
				//noinspection ResultOfMethodCallIgnored
				file.createNewFile();
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		return file;
	}

	/**
	 * 创建所给文件或目录的父目录
	 *
	 * @param file 文件或目录
	 * @return 父目录
	 */
	public static File mkParentDirs(File file) throws IOException {
		if (null == file) {
			return null;
		}
		return mkdir(getParent(file, 1));
	}


	/**
	 * 获取指定层级的父路径
	 *
	 * <pre>
	 * getParent(file("d:/aaa/bbb/cc/ddd", 0)) -》 "d:/aaa/bbb/cc/ddd"
	 * getParent(file("d:/aaa/bbb/cc/ddd", 2)) -》 "d:/aaa/bbb"
	 * getParent(file("d:/aaa/bbb/cc/ddd", 4)) -》 "d:/"
	 * getParent(file("d:/aaa/bbb/cc/ddd", 5)) -》 null
	 * </pre>
	 *
	 * @param file  目录或文件
	 * @param level 层级
	 * @return 路径File，如果不存在返回null
	 * @since 4.1.2
	 */
	public static File getParent(File file, int level) throws IOException {
		if (level < 1 || null == file) {
			return file;
		}

		File parentFile;
		try {
			parentFile = file.getCanonicalFile().getParentFile();
		} catch (IOException e) {
			throw new IOException(e);
		}
		if (1 == level) {
			return parentFile;
		}
		return getParent(parentFile, level - 1);
	}



	/**
	 * 创建文件夹，会递归自动创建其不存在的父文件夹，如果存在直接返回此文件夹<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型<br>
	 *
	 * @param dir 目录
	 * @return 创建的目录
	 */
	public static File mkdir(File dir) {
		if (dir == null) {
			return null;
		}
		if (!dir.exists()) {
			mkdirsSafely(dir, 5, 1);
		}
		return dir;
	}


	/**
	 * 安全地级联创建目录 (确保并发环境下能创建成功)
	 *
	 * <pre>
	 *     并发环境下，假设 test 目录不存在，如果线程A mkdirs "test/A" 目录，线程B mkdirs "test/B"目录，
	 *     其中一个线程可能会失败，进而导致以下代码抛出 FileNotFoundException 异常
	 *
	 *     file.getParentFile().mkdirs(); // 父目录正在被另一个线程创建中，返回 false
	 *     file.createNewFile(); // 抛出 IO 异常，因为该线程无法感知到父目录已被创建
	 * </pre>
	 *
	 * @param dir         待创建的目录
	 * @param tryCount    最大尝试次数
	 * @param sleepMillis 线程等待的毫秒数
	 * @return true表示创建成功，false表示创建失败
	 * @author z8g
	 * @since 5.7.21
	 */
	public static boolean mkdirsSafely(File dir, int tryCount, long sleepMillis) {
		if (dir == null) {
			return false;
		}
		if (dir.isDirectory()) {
			return true;
		}
		for (int i = 1; i <= tryCount; i++) { // 高并发场景下，可以看到 i 处于 1 ~ 3 之间
			// 如果文件已存在，也会返回 false，所以该值不能作为是否能创建的依据，因此不对其进行处理
			//noinspection ResultOfMethodCallIgnored
			dir.mkdirs();
			if (dir.exists()) {
				return true;
			}
			ThreadUtils.sleep(sleepMillis);
		}
		return dir.exists();
	}





	/**
	 * 获得一个文件读取器
	 *
	 * @param file    文件
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IOException IO异常
	 */
	public static BufferedReader getReader(File file, Charset charset) throws IOException {
		return getReader(getInputStream(file), charset);
	}

	/**
	 * 获得输入流
	 *
	 * @param file 文件
	 * @return 输入流
	 * @throws IOException 文件未找到
	 */
	public static BufferedInputStream getInputStream(File file) throws IOException {
		return toBuffered(toStream(file));
	}

	/**
	 * 转换为{BufferedInputStream}
	 *
	 * @param in { InputStream}
	 * @return { BufferedInputStream}
	 * @since 4.0.10
	 */
	public static BufferedInputStream toBuffered(InputStream in) {
		if (Objects.isNull(in)) {
			throw new IllegalArgumentException("InputStream must be not null!");
		}
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

    /**
     * 文件转为{ FileInputStream}
     *
     * @param file 文件
     * @return {FileInputStream}
     */
    public static FileInputStream toStream(File file) throws IOException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        }
    }

	/**
	 * 获得一个Reader
	 *
	 * @param in      输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		if (null == in) {
			return null;
		}

		InputStreamReader reader;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}

		return new BufferedReader(reader);
	}
}
