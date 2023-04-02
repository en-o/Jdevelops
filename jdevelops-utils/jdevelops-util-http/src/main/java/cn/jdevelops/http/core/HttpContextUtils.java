package cn.jdevelops.http.core;

import cn.jdevelops.enums.http.HttpEnum;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpContextUtils
 *
 * @author itdragons
 */
public class HttpContextUtils {

	/**
	 * 获取query参数
	 *
	 * @param request request
	 * @return Map
	 */
	public static Map<String, String> getParameterMapAll(HttpServletRequest request) {
		Enumeration<String> parameters = request.getParameterNames();

		Map<String, String> params = new HashMap<>(16);
		while (parameters.hasMoreElements()) {
			String parameter = parameters.nextElement();
			String value = request.getParameter(parameter);
			params.put(parameter, value);
		}

		return params;
	}

	/**
	 * 获取请求Body
	 *
	 * @param request request
	 * @return String
	 */
	public static String getBodyString(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			String line ;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}


	/**
	 * 判断是否是multipart/form-data请求
	 *
	 * @param request request
	 * @return true 是form-data
	 */
	public static boolean isMultipartContent(HttpServletRequest request) {
		if (!HttpEnum.POST.getCode().equalsIgnoreCase(request.getMethod())) {
			return false;
		}

		//获取Content-Type
		String contentType = request.getContentType();
		return (contentType != null) && (contentType.toLowerCase().startsWith(HttpEnum.MULTIPART_PATHSEPARATOR.getCode().toLowerCase()));

	}
}
