package cn.jdevelops.sboot.websocket.funasr;

import cn.jdevelops.webs.websocket.core.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.websocket.Session;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * wav文件处理
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-01 14:20
 */
public class WavMessage {

    private static final Logger logger = LoggerFactory.getLogger(WavMessage.class);

    private boolean iseof = false;

    public final WebSocketServer webSocketServer;
    public final FunasrMessage funasrMessage;

    public WavMessage(WebSocketServer webSocketServer, FunasrMessage funasrMessage) {
        this.webSocketServer = webSocketServer;
        this.funasrMessage = funasrMessage;
    }


    /**
     * function for rec wav file
     *
     * @param username 接收用户
     * @param fileName 文件名
     * @param wavPath  文件路径
     */
    public void recWav(String username, String fileName, String wavPath, FunasrMode mode) {
        String suffix = fileName.split("\\.")[fileName.split("\\.").length - 1];
        // 首次通信
        funasrMessage.sendFistJson(username,
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
        }
    }


    /**
     * 发送结束标志
     * send json at end of wav
     */
    public void sendEof(String username) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("is_speaking", false);
            if (logger.isDebugEnabled()) {
                logger.debug("sendEof: " + obj);
            }
            webSocketServer.sendInfo(username, obj.toString());
            iseof = true;
        } catch (Exception e) {
            logger.error("结束标志发送失败", e);
        }
    }

}
