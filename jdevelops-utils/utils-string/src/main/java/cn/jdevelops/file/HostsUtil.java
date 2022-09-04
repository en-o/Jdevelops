package cn.jdevelops.file;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 修改hosts文件
 *
 * @author tan
 */
public class HostsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HostsUtil.class);

    /**
     * 获取host文件路径
     * @return String
     */
    public static String getHostFile() {
        String fileName;
        // 判断系统
        if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
            fileName = "/etc/hosts";
        } else {
            fileName = System.getenv("windir") + "\\system32\\drivers\\etc\\hosts";
        }
        return fileName;
    }

    /**
     * 读取hosts文件
     * @return String of Set
     * @throws IOException IOException
     */
    public static Set<String> read() throws IOException {
        return FileUtil.readLines(new File(getHostFile()), StandardCharsets.UTF_8).stream()
                .filter(it -> !it.trim().matches("(^#.*)|(\\s*)"))
                .map(it -> it.replaceAll("#.*", "").trim().replaceAll("\\s+", "\t"))
                .collect(Collectors.toSet());
    }

    /**
     * 添加 hosts
     * @param ip  ip
     * @param domain domain
     * @return boolean
     * @throws IOException IOException
     */
    public synchronized static boolean append(String ip,String domain) throws IOException {
        String format = String.format("%s\t%s", ip, domain);
        if(exists(ip, domain)){
            LOG.warn(format+"已存在");
        }else {
            FileUtil.appendLines(Lists.newArrayList(format), new File(getHostFile()), StandardCharsets.UTF_8);
            flushdns();
        }

        return true;
    }

    /**
     * 判断 dns是否存在
     * @param ip ip
     * @param domain domain
     * @return boolean
     * @throws IOException IOException
     */
    public static boolean exists(String ip, String domain) throws IOException {
        return HostsUtil.read().contains(String.format("%s\t%s", ip, domain));
    }

    public static boolean flushdns() {
        if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
            try {
                Runtime.getRuntime().exec("/etc/init.d/nscd restart");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Runtime.getRuntime().exec("ipconfig /flushdns");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }



    /**
     * 根据输入IP和Domain，删除host文件中的某个host配置
     * @param ip ip
     * @param domain domain
     * @return boolean
     */
    public synchronized static boolean delete(String ip, String domain) throws IOException {
        String format = String.format("%s\t%s", ip, domain);
        if(exists(ip,domain)){
            List<String> hostFileDataLines =  FileUtil.readLines(new File(getHostFile()), StandardCharsets.UTF_8);
            List<String> collect = hostFileDataLines.stream()
                    .filter(it -> !it.trim().equalsIgnoreCase(format)).collect(Collectors.toList());
            FileUtil.writeLines(collect,new File(getHostFile()),StandardCharsets.UTF_8);
            flushdns();
        }else {
            LOG.warn(format+"不存在");
        }
        return true;
    }


}
