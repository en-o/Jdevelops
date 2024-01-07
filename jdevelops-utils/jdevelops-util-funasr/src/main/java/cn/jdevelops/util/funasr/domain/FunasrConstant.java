package cn.jdevelops.util.funasr.domain;

/**
 * 公共藏量
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-01 14:44
 */
public class FunasrConstant {
    public static int chunkInterval = 10;

    /**
     * 表示流式模型latency配置，`[5,10,5]`，表示当前音频为600ms，并且回看300ms，又看300ms
     */
    public static String strChunkSize = "5,10,5";

    /**
     * 发送块大小
     */
    public static int sendChunkSize = 1920;



    static String hotWords="";
    static String fstHotWords="";

}
