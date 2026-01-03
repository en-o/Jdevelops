package cn.tannn.jdevelops.jdectemplate.xmlmapper.page;

/**
 * 分页请求参数
 * <p>封装分页查询所需的参数</p>
 *
 * @author tnnn
 */
public class PageRequest {

    /**
     * 当前页码（从1开始）
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向: ASC/DESC
     */
    private String orderDir = "DESC";

    public PageRequest() {
    }

    public PageRequest(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageRequest(Integer pageNum, Integer pageSize, String orderBy, String orderDir) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        this.orderDir = orderDir;
    }

    /**
     * 计算偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }

    /**
     * 获取完整的排序SQL
     */
    public String getOrderBySql() {
        if (orderBy != null && !orderBy.trim().isEmpty()) {
            String dir = "DESC".equalsIgnoreCase(orderDir) ? "DESC" : "ASC";
            return orderBy + " " + dir;
        }
        return null;
    }

    // Getters and Setters
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", orderBy='" + orderBy + '\'' +
                ", orderDir='" + orderDir + '\'' +
                '}';
    }
}
