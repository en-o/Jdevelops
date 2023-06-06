package cn.jdevelops.api.result.request;


import io.swagger.v3.oas.annotations.media.Schema;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

/**
 * 分页排序
 *
 * @author lxw
 * @date 2018年5月11日
 */
@Schema(description = "分页参数实体")
public class PageDTO implements Serializable {

    /**
     * 页码 [1-500]
     * 默认1
     */
    @Schema(description = "页码", defaultValue = "1", example = "1")
    @Max(value = 500,message = "页码超出了阈值")
    @Min(value = 1,message = "页码超出了阈值")
    private Integer pageIndex;

    /**
     * 数量 [1-100]
     * 默认20
     */
    @Schema(description = "数量", defaultValue = "20", example = "20")
    @Max(value = 100,message = "每页数量超出了阈值")
    @Min(value = 1,message = "每页数量超出了阈值")
    private Integer pageSize;

    public PageDTO() {
    }

    /**
     * 默认 pageIndex = 1
     * @param pageSize 一页显示几条
     */
    public PageDTO(Integer pageSize) {
        // 分页查询在数据库中起始页也为0
        this.pageIndex = 0;
        this.pageSize = pageSize;
    }

    /**
     * 分页
     * @param pageIndex 页码
     * @param pageSize 一页显示几条
     */
    public PageDTO(Integer pageIndex, Integer pageSize) {
        if(pageIndex<1){
            pageIndex = 1;
        }
        // 分页查询在数据库中起始页也为0
        this.pageIndex = pageIndex-1;
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
        if(Objects.isNull(pageIndex)||pageIndex<1){
            // 分页查询在数据库中起始页也为0
            return 0;
        }
        return pageIndex-1;
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
