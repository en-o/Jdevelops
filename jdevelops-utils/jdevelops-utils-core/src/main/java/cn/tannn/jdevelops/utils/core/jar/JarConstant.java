package cn.tannn.jdevelops.utils.core.jar;

/**
 * jarUtil常量
 *
 * @author tn
 * @version 1
 * @date 2022-01-08 17:42
 */
public interface JarConstant {
    /** jar授权失败 */
    String JAR_ERROPR_MESSAGE = "jar授权失败!";
    /** jar授权成功 */
    String JAR_SUCCESS_MESSAGE = "jar授权成功!";
    /** 当前jar未经授权 */
    String NO_PERMISSION_MESSAGE = "当前jar未经授权!";
    /** 当前平台不允许使用该Jar */
    String SYSTEM_NO_USE_MESSAGE = "当前平台不允许使用该Jar!";
    /** test.txt */
    String SYSTEM_MAC_FILE = "test.txt";
    /** /test.txt */
    String SYSTEM_MAC_FILE_PATH = "/test.txt";
    /** tempJar */
    String TEMP_JAR = "tempJar";
    /** jar  */
    String JAR = "jar";
    /** file */
    String FILE = "file";
    /** win */
    String WIN = "win";
    /** 感叹号 */
    String GAN_TAN = "!";
    /** 冒号 */
    String MAO_HAO = ":";
    /** 斜杠 */
    String XIE_GANG = "/";
}
