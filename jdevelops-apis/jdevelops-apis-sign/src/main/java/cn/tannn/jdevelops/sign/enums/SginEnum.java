package cn.tannn.jdevelops.sign.enums;

/**
 * 签名用
 * @author tn
 * @version 1
 * @date 2020/6/9 15:02
 */
public enum SginEnum {
    //0不需要签名，1使用MD5（小写32）数据加密 2 使用SHA数据加密 3(MD5验签，但是sign 参数存放在header中的，让所有参数都参与加密)
    ANY(0), MD5(1), SHA(2), MD5HEADER(3);
    private final int value;

    SginEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
