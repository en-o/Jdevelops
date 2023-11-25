package cn.jdevelops.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电脑信息
 * @author tn
 * @version 1
 * @date 2021/1/11 2:06
 */
public class MacUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MacUtil.class);
    private static final String WIN="win";
    private static final String WINDOWS="Windows";
    private static final String LINUX="Linux";


    /**
     * 限制创建实例
     */
    private MacUtil() {

    }

    private static String macAddressStr = null;
    private static String computerName = System.getenv().get("COMPUTERNAME");

    private static final String[] WINDOWS_COMMAND = { "ipconfig", "/all" };
    private static final String[] LINUX_COMMAND = { "/sbin/ifconfig", "-a" };
    private static final Pattern MAC_PATTERN = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*",
            Pattern.CASE_INSENSITIVE);

    /**
     * 获取多个网卡地址
     *
     * @return List
     */
    public static List<String> getMacAddressList() throws IOException {
        final ArrayList<String> macAddressList = new ArrayList<>();
        final String os = System.getProperty("os.name");
        String[] command;

        if (os.startsWith(WINDOWS)) {
            command = WINDOWS_COMMAND;
        } else if (os.startsWith(LINUX)) {
            command = LINUX_COMMAND;
        } else {
            throw new IOException("Unknow operating system:" + os);
        }
        // 执行命令
        final Process process = Runtime.getRuntime().exec(command);

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for (String line; (line = bufReader.readLine()) != null;) {
            Matcher matcher = MAC_PATTERN.matcher(line);
            if (matcher.matches()) {
                macAddressList.add(matcher.group(1));
                // macAddressList.add(matcher.group(1).replaceAll("[-:]",
                // ""));//去掉MAC中的“-”
            }
        }

        process.destroy();
        bufReader.close();
        return macAddressList;
    }

    /**
     * 获取一个网卡地址（多个网卡时从中获取一个）
     *
     * @return  String
     */
    public static String getMacAddress() {
        if (macAddressStr == null || macAddressStr.isEmpty()) {
            // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
            StringBuilder sb = new StringBuilder();
            try {
                List<String> macList = getMacAddressList();
                for (String amac : macList) {
                    if (!"0000000000E0".equals(amac)) {
                        sb.append(amac);
                        break;
                    }
                }
            } catch (IOException e) {
                LOG.error("获取一个网卡地址失败", e);
            }

            macAddressStr = sb.toString();

        }

        return macAddressStr;
    }

    /**
     * 获取电脑名
     *
     * @return String
     */
    public static String getComputerName() {
        if (computerName == null || computerName.isEmpty()) {
            computerName = System.getenv().get("COMPUTERNAME");
        }
        return computerName;
    }


    /**
     * 获取电脑唯一标识
     *
     * @return String
     */
    public static String getComputerId() {
        String id = getMacAddress();
        if (id == null || id.isEmpty()) {
            try {
                return IpUtil.getIpAddrAndName();
            } catch (IOException e) {
                LOG.error("获取电脑唯一标识失败", e);
            }
        }
        return computerName;
    }

    /**
     *  win separator Linux 相互转换
     * @param path 地址
     * @return String
     */
    public static String win2Linux(String path) {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(WIN)){
            path = path.replace("/","\\");
        }else {
            path = path.replace("\\","/");
        }
        return path;
    }
}
