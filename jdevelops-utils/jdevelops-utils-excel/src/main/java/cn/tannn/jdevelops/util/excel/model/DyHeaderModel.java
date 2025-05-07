package cn.tannn.jdevelops.util.excel.model;


import java.util.List;

/**
 * 动态表头
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-11-24 15:17
 */

public class DyHeaderModel {

    /**
     * 表头数据
     */
    List<List<String>> header;

    /**
     * 需要有下拉选项的表头信息
     */
    List<HeaderMenu> menu;

    public DyHeaderModel(List<List<String>> header, List<HeaderMenu> menu) {
        this.header = header;
        this.menu = menu;
    }

    public DyHeaderModel() {
    }

    public List<List<String>> getHeader() {
        return header;
    }

    public void setHeader(List<List<String>> header) {
        this.header = header;
    }

    public List<HeaderMenu> getMenu() {
        return menu;
    }

    public void setMenu(List<HeaderMenu> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "DyHeaderModel{" +
                "header=" + header +
                ", menu=" + menu +
                '}';
    }
}
