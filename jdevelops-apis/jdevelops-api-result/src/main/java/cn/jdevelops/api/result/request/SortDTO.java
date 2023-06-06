package cn.jdevelops.api.result.request;

import cn.jdevelops.api.result.util.bean.ColumnSFunction;
import cn.jdevelops.api.result.util.bean.ColumnUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.*;

/**
 * 分页
 *
 * @author tn
 * @date 2020-12-17 14:26
 */
@Schema(description = "排序实体类")
public class SortDTO implements Serializable {

    /**
     * 排序字段 (可多字段)
     * 默认id
     */
    @Schema(description = "排序字段（实体的有效字段）", defaultValue = "id", example = "id")
    private List<String> orderBy;

    /**
     * 排序方式 正序0--Direction.ASC，反序1--Direction.DESC
     * 默认倒叙
     */
    @Schema(description = "排序方式（正序0，反序1）", defaultValue = "1", example = "1")
    private Integer orderDesc;


    /**
     * 默认倒序
     *
     * @param orderBy 排序字段
     */
    public SortDTO(String... orderBy) {
        this.orderBy = Arrays.asList(orderBy);
        this.orderDesc = 1;
    }


    /**
     * 默认倒序
     *
     * @param orderBy 排序字段
     */
    public <T> SortDTO(ColumnSFunction<T, ?>... orderBy) {
        List<String> list = new ArrayList<>();
        for (ColumnSFunction<T, ?> tcs : orderBy) {
            list.add(ColumnUtil.getFieldName(tcs));
        }
        this.orderBy = list;
        this.orderDesc = 1;
    }


    /**
     * 排序
     *
     * @param orderDesc 正序0--Direction.ASC，反序1--Direction.DESC
     * @param orderBy   排序字段
     */
    public SortDTO(Integer orderDesc, String... orderBy) {
        this.orderBy = Arrays.asList(orderBy);
        this.orderDesc = orderDesc;
    }


    /**
     * 排序
     *
     * @param orderDesc 正序0--Direction.ASC，反序1--Direction.DESC
     * @param orderBy   排序字段
     */
    public <T> SortDTO(Integer orderDesc, ColumnSFunction<T, ?>... orderBy) {
        List<String> list = new ArrayList<>();
        for (ColumnSFunction<T, ?> tcs : orderBy) {
            list.add(ColumnUtil.getFieldName(tcs));
        }
        this.orderBy = list;
        this.orderDesc = orderDesc;
    }

    public SortDTO() {
    }

    @Override
    public String toString() {
        return "SortVO{" +
                "orderBy='" + orderBy + '\'' +
                ", orderDesc=" + orderDesc +
                '}';
    }

    public List<String> getOrderBy() {
        if (Objects.isNull(orderBy)) {
            return Collections.singletonList("id");
        }
        return orderBy;
    }

    public Integer getOrderDesc() {
        if (Objects.isNull(orderDesc)) {
            return 1;
        }
        return orderDesc;
    }

    public void setOrderDesc(Integer orderDesc) {
        this.orderDesc = orderDesc;
    }
}
