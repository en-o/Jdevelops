package cn.tannn.jdevelops.utils.core.jar;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tnnn
 * <a href="https://blog.csdn.net/weixin_42514654/article/details/114046759">参考</a>
 * 在代码中调用另外一个jar
 *  - 直接调用线程会阻塞
 * 使用线程读取
 * <pr>
ExecutorService jar = Executors.newFixedThreadPool(1);
jar.execute(() -> {
JarExecutor jarExecutor = new JarExecutor();
jarExecutor.executeJar("E:\\西信\\demo1\\target\\demo1-0.0.1-SNAPSHOT.jar",null);
});
jar.shutdown();
 * </pr>
 */
public class JarExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(JarExecutor.class);

    private BufferedReader error;
    private BufferedReader op;
    private int exitVal;


    /**
     * 执行JAR
     * @param jarFilePath JAR 路径
     * @param args jar参数
     */
    public void executeJar(String jarFilePath, List args) {

        final List actualArgs = new ArrayList();

        actualArgs.add(0, "java");

        actualArgs.add(1, "-jar");

        actualArgs.add(2, jarFilePath);

        if (args != null && args.size() > 0) {
            actualArgs.addAll(args);
        }

        try {
            final Runtime re = Runtime.getRuntime();

            final Process command = re.exec((String[]) actualArgs.toArray(new String[0]));

            this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));

            this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));

// Wait for the application to Finish

            command.waitFor();

            this.exitVal = command.exitValue();

            if (this.exitVal != 0) {
                throw new IOException("Failed to execure jar, " + this.getExecutionLog());

            }

        } catch (final InterruptedException | IOException e) {
            LOG.error("执行JAR失败", e);
        }
    }


    /**
     * 执行日志
      * @return 日志
     */
    public String getExecutionLog() {
        StringBuilder error = new StringBuilder();

        String line;

        try {
            while ((line = this.error.readLine()) != null) {
                error.append("\n").append(line);

            }

        } catch (final IOException e) {
            LOG.error("读取错误日志失败", e);
        }

        StringBuilder output = new StringBuilder();

        try {
            while ((line = this.op.readLine()) != null) {
                output.append("\n").append(line);

            }

        } catch (final IOException e) {
            LOG.error("输出错误日志失败", e);
        }

        try {
            this.error.close();

            this.op.close();

        } catch (final IOException e) {
            LOG.error("关闭文件流失败", e);
        }

        return "exitVal: " + this.exitVal + ", error: " + error + ", output: " + output;
    }
}
