package cn.jdevelops.spring.core.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件的信息
 *
 * @author xzq create time 2017/04/27
 */
public class PropertiesUtil {
	/**
	 * 读取配置文件的信息
	 *
	 * @param fileName---配置文件名
	 * @param term----key值
	 * @return String
	 * @throws IOException IOException
	 * @author xzq
	 */
	public String getTerm(String fileName, String term) throws IOException {
		// 读取properties文件
		Properties prop = new Properties();
		prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
		return prop.getProperty(term);
	}

}
