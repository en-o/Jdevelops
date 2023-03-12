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
@Schema(description = "公共分页参数")
public class RoutinePageDTO implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段(实体的有效字段)", defaultValue = "id", example = "id")
    private String orderBy;

    /**
     * 排序方式 正序0--Direction.ASC，反序1--Direction.DESC
     */
    @Schema(description = "排序方式(正序0，反序1)", defaultValue = "1", example = "1")
    private Integer orderDesc;

    /**
     * 页码
     */
    @Schema(description = "页码", defaultValue = "1", example = "1")
    private Integer pageIndex;

    /**
     * 数量
     */
    @Schema(description = "数量", defaultValue = "20", example = "20")
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
