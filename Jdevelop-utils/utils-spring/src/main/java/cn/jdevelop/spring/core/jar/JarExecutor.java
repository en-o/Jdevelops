package cn.jdevelop.spring.core.jar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @link https://blog.csdn.net/weixin_42514654/article/details/114046759
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
    private BufferedReader error;
    private BufferedReader op;
    private int exitVal;

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

//final Process command = re.exec(cmdString, args.toArray(new String[0]));

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
            e.printStackTrace();
        }
    }

    public String getExecutionLog() {
        String error = "";

        String line;

        try {
            while ((line = this.error.readLine()) != null) {
                error = error + "\n" + line;

            }

        } catch (final IOException e) {
        }

        String output = "";

        try {
            while ((line = this.op.readLine()) != null) {
                output = output + "\n" + line;

            }

        } catch (final IOException e) {
        }

        try {
            this.error.close();

            this.op.close();

        } catch (final IOException e) {
        }

        return "exitVal: " + this.exitVal + ", error: " + error + ", output: " + output;
    }
}
