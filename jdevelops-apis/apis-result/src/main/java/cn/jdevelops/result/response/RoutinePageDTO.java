package cn.jdevelops.result.response;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Objects;

/**
 * 公共的分页DTO
 *
 * @author tn
 * @date 2021-01-25 13:59
 */
@Schema(name = "分页参数", description = "公共的分页DTO")
public class RoutinePageDTO implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;

    /**
     * 排序字段
     */
    @Schema(name = "排序字段", description = "实体的有效字段",example = "id")
    private String orderBy;

    /**
     * 排序方式
     */
    @Schema(name = "排序方式", description = "正序0--Direction.ASC，反序1--Direction.DESC", example = "1")
    private Integer orderDesc;

    /**
     * 页码
     */
    @Schema(name = "页码", description = "默认第一页", example = "1")
    private Integer pageIndex;

    /**
     * 数量
     */
    @Schema(name = "数量", description = "默认20条", example = "20")
    private Integer pageSize;

    public RoutinePageDTO(String orderBy, Integer orderDesc, Integer pageIndex, Integer pageSize) {
        this.orderBy = orderBy;
        this.orderDesc = orderDesc;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public RoutinePageDTO() {
    }

    @Override
    public String toString() {
        return "RoutinePageDTO{" +
                "orderBy='" + orderBy + '\'' +
                ", orderDesc=" + orderDesc +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }

    public String getOrderBy() {
        if(Objects.isNull(orderBy)){
            return "id";
        }
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrderDesc() {
        if(Objects.isNull(orderDesc)){
            return 1;
        }
        return orderDesc;
    }

    public void setOrderDesc(Integer orderDesc) {
        this.orderDesc = orderDesc;
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
