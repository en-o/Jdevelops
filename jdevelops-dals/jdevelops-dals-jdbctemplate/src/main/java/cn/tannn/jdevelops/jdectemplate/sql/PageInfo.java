package cn.tannn.jdevelops.jdectemplate.sql;

/**
 * 分页参数
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/9/24 12:50
 */
public class PageInfo {

    /**
     * 页码 从1开始
     */
    private Integer pageIndex;

    /**
     * 数量
     */
    private Integer pageSize;

    public Integer getPageIndex() {
        if(pageIndex == null){
            return 1;
        } else if (pageIndex<1) {
            return 0;
        } else {
            return pageIndex-1;
        }
    }

    public Integer getRealPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
