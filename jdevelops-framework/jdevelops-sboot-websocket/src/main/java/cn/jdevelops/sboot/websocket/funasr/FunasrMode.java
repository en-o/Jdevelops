package cn.jdevelops.sboot.websocket.funasr;

/**
 * 录音模式
 * @author tnnn
 * @version V1.0
 * @date 2024-01-01 14:51
 */
public enum FunasrMode {

    /**
     * 支持实时语音听写服务（默认用）
     */
   ONLINE("online"),
    /**
     * 非实时一句话转写
     */
    OFFLINE("offline"),

    /**
     * 与实时与非实时一体化协同
     */
    TWO_PASS("2pass");


    /**
     * Mode
     */
    String mode;

    FunasrMode(String name) {
        this.mode = name;
    }

    public String getMode() {
        return mode;
    }
}
