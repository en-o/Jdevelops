package cn.tannn.jdevelops.util.excel.model;


import java.util.List;

/**
 * 需要下拉数据和下拉坐标
 * @author tnnn
 */

public class HeaderMenuData {
    /**
     * 需要下拉数据的表头下标
     */
    private Integer index;
    /**
     * 下拉数据
     */
    private List<String> pullDownData;


    public HeaderMenuData() {
    }

    public HeaderMenuData(Integer index, List<String> pullDownData) {
        this.index = index;
        this.pullDownData = pullDownData;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<String> getPullDownData() {
        return pullDownData;
    }

    public void setPullDownData(List<String> pullDownData) {
        this.pullDownData = pullDownData;
    }

    @Override
    public String toString() {
        return "HeaderMenuData{" +
                "index=" + index +
                ", pullDownData=" + pullDownData +
                '}';
    }
}
