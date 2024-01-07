package cn.jdevelops.util.funasr.message;


import cn.jdevelops.util.funasr.config.FunasrProperties;
import cn.jdevelops.util.funasr.domain.FunasrConstant;
import cn.jdevelops.util.funasr.domain.FunasrMode;
import cn.jdevelops.util.funasr.websocket.FunasrWebSocketClientEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.TargetDataLine;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

    public final FunasrWebSocketClientEndpoint clientEndpoint;
    public final FunasrProperties funasrProperties;

    public FunasrMessage(FunasrWebSocketClientEndpoint clientEndpoint, FunasrProperties funasrProperties) {
        this.clientEndpoint = clientEndpoint;
        this.funasrProperties = funasrProperties;
    }


    /**
     * 发送 asr 音频流 返回实时语言
     *
     * @param username 接收用户
     * @param session  websocket session
     * @param audio   音频流
     * @param line  TargetDataLine
     */
    public void sendSpeech(String username, Session session, byte[] audio, TargetDataLine line) {
        // 首次通信,建立连接
        sendHandshake(
                session,
                FunasrConstant.strChunkSize,
                FunasrConstant.chunkInterval,
                "wavName",
                true,
                "");
        int chunkSize = FunasrConstant.sendChunkSize;
        byte[] bytes = new byte[chunkSize];
        int readSize = line.read(audio, 0, audio.length);
        while (readSize > 0) {
            try {
                // send when it is chunk size
                if (readSize == chunkSize) {
                    sendByte(bytes, session);
                } else {
                    // send when at last or not is chunk size
                    byte[] tmpBytes = new byte[readSize];
                    for (int i = 0; i < readSize; i++) {
                        tmpBytes[i] = bytes[i];
                    }
                    sendByte(bytes, session);
                }
                // if not in offline mode, we simulate online stream by sleep
                if (!funasrProperties.getFunasrMode().equals(FunasrMode.OFFLINE)) {
                    Thread.sleep(Integer.valueOf(chunkSize / 32));
                }
                readSize = line.read(bytes, 0, chunkSize);
            } catch (Exception e) {
                logger.error("实时语音识别数据发送失败", e);
            }

        }
        if (!funasrProperties.getFunasrMode().equals(FunasrMode.ONLINE)) {
            // if not offline, we send eof and wait for 3 seconds to close
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
               logger.error("sleep失败");
            }
            sendEof();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.error("sleep失败");
            }
            //关闭这session
            clientEndpoint.onClose(session);
        } else {
            //  发送结束标志
            sendEof();
        }

    }


    /**
     * 首次通讯[json message]
     *
     * @param strChunkSize  表示流式模型latency配置，`[5,10,5]`，表示当前音频为600ms，并且回看300ms，又看300ms
     * @param chunkInterval
     * @param wavName       表示需要推理音频文件名
     * @param isSpeaking    表示断句尾点，例如，vad切割点，或者一条wav结束
     * @param hotWords      如果使用热词，需要向服务端发送热词数据（字符串），格式为 "{"阿里巴巴":20,"通义实验室":30}"
     * @see <a href="https://github.com/alibaba-damo-academy/FunASR/blob/main/runtime/docs/websocket_protocol_zh.md">从客户端往服务端发送数据</a>
     */
    public void sendHandshake(Session session,
                              String strChunkSize,
                              int chunkInterval,
                              String wavName,
                              boolean isSpeaking,
                              String hotWords) {
        // suffix        示音视频文件后缀名，可选pcm、mp3、mp4等（备注：只支持pcm音频流）
        try {
            JSONObject obj = new JSONObject();
            // 指定推理模式
            obj.put("mode", funasrProperties.getFunasrMode());
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
            // 结束标志: 音频数据发送结束后，需要发送结束标志（需要用json序列化）：
            if (isSpeaking) {
                obj.put("is_speaking", true);
            } else {
                obj.put("is_speaking", false);
            }
            sendStr(obj.toString(), session);
        } catch (Exception e) {
            logger.error("首次通讯失败[json message]", e);
        }
    }


    /**
     * 发送结束标志
     */
    public void sendEof() {
        try {
            org.springframework.boot.configurationprocessor.json.JSONObject obj = new org.springframework.boot.configurationprocessor.json.JSONObject();
            obj.put("is_speaking", false);
            if (logger.isDebugEnabled()) {
                logger.debug("sendEof: " + obj);
            }
            sendStr(obj.toString(), null);
        } catch (Exception e) {
            logger.error("结束标志发送失败", e);
        }
    }


    /**
     * 发送消息
     *
     * @param message 消息
     * @param session Session
     */
    public void sendStr(String message, Session session) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            logger.error("消息发送失败", e);
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息
     * @param session Session
     */
    public void sendByte(byte[] message, Session session) {
        try {
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(message));
        } catch (Exception e) {
            logger.error("byte流消息发送失败", e);
        }
    }

}
