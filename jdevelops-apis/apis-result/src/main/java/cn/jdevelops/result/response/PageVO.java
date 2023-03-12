package cn.jdevelops.result.response;


import io.swagger.v3.oas.annotations.media.Schema;


import java.util.Objects;

/**
 * 分页排序
 *
 * @author lxw
 * @date 2018年5月11日
 */
@Schema(description = "分页参数实体")
public class PageVO {
    @Schema(description = "页码", defaultValue = "1", example = "1")
    private Integer pageIndex;
    @Schema(description = "数量", defaultValue = "20", example = "20")
    private Integer pageSize;

    public PageVO() {
    }

    /**
     * 默认 pageIndex = 1
     * @param pageSize 一页显示几条
     */
    public PageVO(Integer pageSize) {
        this.pageIndex = 1;
        this.pageSize = pageSize;
    }

    /**
     * 分页
     * @param pageIndex 页码
     * @param pageSize 一页显示几条
     */
    public PageVO(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageVO{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }

    public Integer getPageIndex() {
        if(Objects.isNull(pageIndex)){
            return 1;
        }
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        if(Objects.isNull(pageSize)){
            return 20;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
