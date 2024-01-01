package cn.jdevelops.sboot.websocket.funasr;


import cn.jdevelops.webs.websocket.core.WebSocketServer;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Funasr 消息工具
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-01 14:24
 */
public class FunasrMessage {
    private static final Logger logger = LoggerFactory.getLogger(FunasrMessage.class);



    public final WebSocketServer webSocketServer;

    public FunasrMessage(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }


    /**
     * send json at first time
     * 首次通讯[json message]
     * @see <a href="https://github.com/alibaba-damo-academy/FunASR/blob/main/runtime/docs/websocket_protocol_zh.md">从客户端往服务端发送数据</a>
     * @param userName 指定接收用户
     * @param mode 示推理模式为一句话识别；`online`，表示推理模式为实时语音识别；`2pass`：表示为实时语音识别，并且说话句尾采用离线模型进行纠错
     * @param strChunkSize 表示流式模型latency配置，`[5,10,5]`，表示当前音频为600ms，并且回看300ms，又看300ms
     * @param chunkInterval
     * @param wavName 表示需要推理音频文件名
     * @param isSpeaking 表示断句尾点，例如，vad切割点，或者一条wav结束
     * @param hotWords 如果使用热词，需要向服务端发送热词数据（字符串），格式为 "{"阿里巴巴":20,"通义实验室":30}"
     * @param suffix 示音视频文件后缀名，可选pcm、mp3、mp4等（备注：只支持pcm音频流）
     */
    public void sendFistJson(String userName,
                             FunasrMode mode,
                             String strChunkSize,
                             int chunkInterval,
                             String wavName,
                             boolean isSpeaking,
                             String hotWords,
                             String suffix) {
        try {
            JSONObject obj = new JSONObject();
            // 指定推理模式
            obj.put("mode", mode.getMode());
            JSONArray array = new JSONArray();
            String[] chunkList = strChunkSize.split(",");
            for (int i = 0; i < chunkList.length; i++) {
                array.add(Integer.valueOf(chunkList[i].trim()));
            }
            // 音频数据
            obj.put("chunk_size", array);
            obj.put("chunk_interval", chunkInterval);
            obj.put("wav_name", wavName);


            // 热词
            if (hotWords.trim().length() > 0) {
                String regex = "\\d+";
                JSONObject jsonitems = new JSONObject();
                String[] items = hotWords.trim().split(" ");
                Pattern pattern = Pattern.compile(regex);
                String tmpWords = "";
                for (int i = 0; i < items.length; i++) {
                    Matcher matcher = pattern.matcher(items[i]);
                    if (matcher.matches()) {
                        jsonitems.put(tmpWords.trim(), items[i].trim());
                        tmpWords = "";
                        continue;
                    }
                    tmpWords = tmpWords + items[i] + " ";
                }
                obj.put("hotwords", jsonitems.toString());
            }
            if (suffix.equals("wav")) {
                suffix = "pcm";
            }
            obj.put("wav_format", suffix);

            // 结束标志: 音频数据发送结束后，需要发送结束标志（需要用json序列化）：
            if (isSpeaking) {
                obj.put("is_speaking", new Boolean(true));
            } else {
                obj.put("is_speaking", new Boolean(false));
            }
            logger.info("sendJson: " + obj);
            webSocketServer.sendInfo(userName,obj.toString());
        } catch (Exception e) {
            logger.error("首次通讯失败[json message]", e);
        }
    }
}
