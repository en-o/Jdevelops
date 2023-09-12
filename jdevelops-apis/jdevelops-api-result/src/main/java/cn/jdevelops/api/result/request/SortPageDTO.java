package cn.jdevelops.api.result.request;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页排序组合
 *
 * @author tn
 * @date 2021-01-25 13:59
 */
@Schema(description = "公共分页参数")
public class SortPageDTO extends PageDTO {

    /**
     * 范围：[1-5]
     * 排序
     */
    @Valid
    @Size(min = 1, max = 5)
    List<SortDTO> sorts;

    public SortPageDTO() {
    }


    /**
     * 分页排序
     *
     * @param pageSize 数量
     */
    public SortPageDTO(Integer pageSize) {
        super(pageSize);
    }


    /**
     * 分页排序
     *
     * @param pageSize 数量
     * @param sorts    排序
     */
    public SortPageDTO(Integer pageSize, List<SortDTO> sorts) {
        super(pageSize);
        this.sorts = sorts;
    }


    /**
     * 分页排序
     *
     * @param pageSize 数量
     * @param sorts    排序
     */
    public SortPageDTO(Integer pageSize, SortDTO sorts) {
        super(pageSize);
        ArrayList<SortDTO> sortArray = new ArrayList<>();
        sortArray.add(sorts);
        this.sorts = sortArray;
    }


    /**
     * 分页排序
     *
     * @param sorts 排序
     */
    public SortPageDTO(List<SortDTO> sorts) {
        this.sorts = sorts;
    }


    /**
     * 分页排序
     *
     * @param pageIndex 页码
     * @param pageSize  数量
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize) {
        super(pageIndex, pageSize);
    }


    /**
     * 分页排序
     *
     * @param pageIndex 页码
     * @param pageSize  数量
     * @param sorts     排序
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, List<SortDTO> sorts) {
        super(pageIndex, pageSize);
        this.sorts = sorts;
    }


    /**
     * 分页排序
     *
     * @param pageIndex 页码
     * @param pageSize  数量
     * @param orderDesc 排序方式
     * @param orderBy   排序字段
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, Integer orderDesc, String... orderBy) {
        super(pageIndex, pageSize);
        ArrayList<SortDTO> sortArray = new ArrayList<>();
        sortArray.add(new SortDTO(orderDesc, orderBy));
        this.sorts = sortArray;
    }


    /**
     * 分页排序
     *
     * @param pageIndex 页码
     * @param pageSize  数量
     * @param sorts     排序
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, SortDTO sorts) {
        super(pageIndex, pageSize);
        ArrayList<SortDTO> sortArray = new ArrayList<>();
        sortArray.add(sorts);
        this.sorts = sortArray;
    }


    public List<SortDTO> getSorts() {
        if (null == sorts || sorts.isEmpty()) {
            ArrayList<SortDTO> sortArray = new ArrayList<>();
            sortArray.add(new SortDTO());
            return sortArray;
        } else {
            return sorts;
        }
    }

    public void setSorts(List<SortDTO> sorts) {
        this.sorts = sorts;
    }


    /**
     *当排序为空是修改默认的排序字段,若不为空在原有的排序基础上追加默认自定义排序
     *
     * @param orderBy 排序字段 （默认排序方式为倒叙）
     */
    public SortPageDTO optionsDefOrderBy(String... orderBy) {
        if (this.sorts == null) {
            ArrayList<SortDTO> sortArray = new ArrayList<>();
            sortArray.add(new SortDTO(orderBy));
            this.sorts = sortArray;
        } else {
            this.sorts.add(new SortDTO(orderBy));
        }
        return this;
    }


    /**
     * 当排序为空是修改默认的排序字段,若不为空则不进行操作
     *
     * @param orderBy 排序字段 （默认排序方式为倒叙）
     */
    public SortPageDTO optionsNullDefOrderBy(String... orderBy) {
        if (this.sorts == null) {
            ArrayList<SortDTO> sortArray = new ArrayList<>();
            sortArray.add(new SortDTO(orderBy));
            this.sorts = sortArray;
        }
        return this;
    }

    /**
     * 当排序为空是修改默认的排序字段,若不为空在原有的排序基础上追加默认自定义排序
     *
     * @param orderDesc 排序方式 正序0--Direction.ASC，反序1--Direction.DESC [0-1]
     * @param orderBy   排序字段
     */
    public SortPageDTO optionsDefOrderBy(Integer orderDesc, String... orderBy) {
        if (this.sorts == null) {
            ArrayList<SortDTO> sortArray = new ArrayList<>();
            sortArray.add(new SortDTO(orderDesc, orderBy));
            this.sorts = sortArray;
        } else {
            this.sorts.add(new SortDTO(orderDesc, orderBy));
        }

        return this;
    }


    /**
     * 当排序为空是修改默认的排序字段,若不为空则不进行操作
     *
     * @param orderDesc 排序方式 正序0--Direction.ASC，反序1--Direction.DESC [0-1]
     * @param orderBy   排序字段
     */
    public SortPageDTO optionsNullDefOrderBy(Integer orderDesc, String... orderBy) {
        if (this.sorts == null) {
            ArrayList<SortDTO> sortArray = new ArrayList<>();
            sortArray.add(new SortDTO(orderDesc, orderBy));
            this.sorts = sortArray;
        }
        return this;
    }

    @Override
    public String toString() {
        return "SortPageDTO{" +
                "sorts=" + sorts +
                '}';
    }
}
