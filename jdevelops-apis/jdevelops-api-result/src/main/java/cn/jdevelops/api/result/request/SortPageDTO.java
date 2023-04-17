package cn.jdevelops.api.result.request;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Objects;

/**
 * 分页排序组合
 *
 * @author tn
 * @date 2021-01-25 13:59
 */
@Schema(description = "公共分页参数")
public class SortPageDTO implements Serializable {

    /**
     * 排序字段
     * 默认id
     */
    @Schema(description = "排序字段（实体的有效字段）", defaultValue = "id", example = "id")
    private String orderBy;

    /**
     * 排序方式 正序0--Direction.ASC，反序1--Direction.DESC
     * 默认倒叙
     */
    @Schema(description = "排序方式（正序0，反序1）", defaultValue = "1", example = "1")
    private Integer orderDesc;
    /**
     * 页码
     * 默认1
     */
    @Schema(description = "页码", defaultValue = "1", example = "1")
    private Integer pageIndex;

    /**
     * 数量
     * 默认20
     */
    @Schema(description = "数量", defaultValue = "20", example = "20")
    private Integer pageSize;


    public SortPageDTO() {
    }

    /**
     * 构造
     * @param orderBy null:默认
     * @param orderDesc null:默认
     * @param pageIndex null:默认 从1开始
     * @param pageSize null:默认
     */
    public SortPageDTO(String orderBy, Integer orderDesc, Integer pageIndex, Integer pageSize) {
        this.orderBy = orderBy;
        this.orderDesc = orderDesc;
        if(pageIndex<1){
            pageIndex = 1;
        }
        // 分页查询在数据库中起始页也为0
        this.pageIndex = pageIndex-1;
        this.pageSize = pageSize;
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
