package cn.tannn.jdevelops.util.excel.model;


/**
 * 需要下拉数据的表头 - 备用
 * @author tnnn
 */
public class HeaderMenu {
    /**
     * 需要下拉数据的表头下标
     */
    private Integer index;
    /**
     * 表头名
     */
    private String name;

    /**
     * 表头名代码
     */
    private String code;

    public HeaderMenu() {
    }

    public HeaderMenu(Integer index, String name, String code) {
        this.index = index;
        this.name = name;
        this.code = code;
    }
}
