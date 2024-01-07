package cn.jdevelops.util.funasr;

import cn.jdevelops.sboot.websocket.funasr.domain.FunasrConstant;
import cn.jdevelops.sboot.websocket.funasr.domain.FunasrMode;
import cn.jdevelops.webs.websocket.core.WebSocketServer;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * wav文件处理
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-01 14:20
 */
public class FunasrMessage {

    private static final Logger logger = LoggerFactory.getLogger(FunasrMessage.class);

    private boolean iseof = false;

    public final WebSocketServer webSocketServer;

    public FunasrMessage(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }


    /**
     * 发送 asr 音频流 返回实时语言
     * @param username 接收用户
     * @param fileName 文件名
     * @param wavPath  文件路径
     */
    public void sendSpeech(String username, String fileName, String wavPath, FunasrMode mode) {
        String suffix = fileName.split("\\.")[fileName.split("\\.").length - 1];
        // 首次通信,建立连接
        sendHandshake(username,
                mode,
                FunasrConstant.strChunkSize,
                FunasrConstant.chunkInterval,
                "wavName",
                true,
                "",
                suffix);
        File file = new File(wavPath);

        int chunkSize = FunasrConstant.sendChunkSize;
        byte[] bytes = new byte[chunkSize];
        int readSize = 0;
        // 音频流
        try (FileInputStream fis = new FileInputStream(file)) {

            if (wavPath.endsWith(".wav")) {
                fis.read(bytes, 0, 44); //skip first 44 wav header
            }
            readSize = fis.read(bytes, 0, chunkSize);
            while (readSize > 0) {
                // send when it is chunk size
                if (readSize == chunkSize) {
                    webSocketServer.sendByte(username, bytes);
                } else {
                    // send when at last or not is chunk size
                    byte[] tmpBytes = new byte[readSize];
                    for (int i = 0; i < readSize; i++) {
                        tmpBytes[i] = bytes[i];
                    }
                    webSocketServer.sendByte(username, bytes);
                }
                // if not in offline mode, we simulate online stream by sleep
                if (!mode.equals("offline")) {
                    Thread.sleep(Integer.valueOf(chunkSize / 32));
                }

                readSize = fis.read(bytes, 0, chunkSize);
            }

            if (!mode.equals(FunasrMode.ONLINE)) {
                // if not offline, we send eof and wait for 3 seconds to close
                Thread.sleep(2000);
                sendEof(username);
                Thread.sleep(3000);
                //关闭这个用户的所有session
                webSocketServer.close(username);
            } else {
                //  发送结束标志
               sendEof(username);
            }

        } catch (Exception e) {
            logger.error("实时语音识别数据发送失败", e);
        }finally {
            iseof = true;
        }
    }





    /**
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
    public void sendHandshake(String userName,
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


    /**
     * 发送结束标志
     * @param username 用户
     */
    public void sendEof(String username) {
        try {
            org.springframework.boot.configurationprocessor.json.JSONObject obj = new org.springframework.boot.configurationprocessor.json.JSONObject();
            obj.put("is_speaking", false);
            if (logger.isDebugEnabled()) {
                logger.debug("sendEof: " + obj);
            }
            webSocketServer.sendInfo(username, obj.toString());
        } catch (Exception e) {
            logger.error("结束标志发送失败", e);
        }
    }

}
