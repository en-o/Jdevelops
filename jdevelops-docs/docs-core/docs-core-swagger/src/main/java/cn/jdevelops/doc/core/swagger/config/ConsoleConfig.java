package cn.jdevelops.doc.core.swagger.config;

import cn.jdevelops.doc.core.swagger.bean.SwaggerBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static cn.jdevelops.doc.core.swagger.constant.PublicConstant.COLON;
import static cn.jdevelops.doc.core.swagger.constant.PublicConstant.SPIRIT;

/**
 * 控制台输出
 *
 * @author tn
 * @version 1
 * @date 2020/6/18 16:24
 */
@Slf4j
public class ConsoleConfig implements ApplicationRunner {


	@Value("${server.port:8080}")
	private int serverPort;

	@Value("${server.servlet.context-path:/}")
	private String serverName;


	@Resource
	private SwaggerBean swaggerBean;


	@Override
	public void run(ApplicationArguments args) {
		try {
			if (SPIRIT.equals(serverName)) {
				serverName = "";
			}
			String groupStr = "";
			String groupName = swaggerBean.getGroupName();
			if (StringUtils.isNotBlank(groupName)) {
				groupStr = "?group=" + swaggerBean.getGroupName();
			}
			log.info("\n----------------------------------------------------------\n\t" +
					" swagger 启动. Access URLs:\n\t" +
					"swagger 启动成功！接口文档地址(cloud没有页面)-HTML: (http://" + getRealIp() + COLON + serverPort + serverName + "/doc.html" + ")\n\t" +
					"swagger 启动成功！接口文档地址-JSON: (http://" + getRealIp() + COLON + serverPort + serverName + "/v2/api-docs" + groupStr + ")\n\t" +
					"swagger 启动成功！接口文档地址-OpenApi-JSON: (http://" + getRealIp() + COLON + serverPort + serverName + "/v3/api-docs" + groupStr + ")\n\t" +
					"----------------------------------------------------------");


		} catch (Exception ignored) {
		}
	}

	/**
	 * 获取本地真正的IP地址，即获得有线或者无线WiFi地址。(过滤虚拟机、蓝牙等地址)
	 *
	 * @return java.lang.String
	 * @author tn
	 * @date 2020/4/21 23:44
	 */

	private static String getRealIp() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces
						.nextElement();

				// 去除回环接口，子接口，未运行和接口
				if (netInterface.isLoopback() || netInterface.isVirtual()
						|| !netInterface.isUp()) {
					continue;
				}

				if (!netInterface.getDisplayName().contains("Intel")
						&& !netInterface.getDisplayName().contains("Realtek")) {
					continue;
				}
				Enumeration<InetAddress> addresses = netInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = addresses.nextElement();
					// ipv4
					if (ip instanceof Inet4Address) {
						return ip.getHostAddress();
					}
				}
				break;
			}
		} catch (SocketException e) {
			log.error("获取主机ip地址时出错"
					+ e.getMessage());
		}
		return "127.0.0.1";
	}


}

