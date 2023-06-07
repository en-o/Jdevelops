package cn.jdevelops.api.result.request;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Collections;
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
    @Size(min = 1,max = 5)
    List<SortDTO> sorts;

    public SortPageDTO() {
    }



    /**
     * 分页排序
     * @param pageSize 数量
     */
    public SortPageDTO(Integer pageSize) {
        super(pageSize);
    }


    /**
     * 分页排序
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageSize, List<SortDTO> sorts) {
        super(pageSize);
        this.sorts = sorts;
    }


    /**
     * 分页排序
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageSize, SortDTO sorts) {
        super(pageSize);
        this.sorts = Collections.singletonList(sorts);
    }


    /**
     * 分页排序
     * @param sorts 排序
     */
    public SortPageDTO(List<SortDTO> sorts) {
        this.sorts = sorts;
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize) {
        super(pageIndex, pageSize);
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, List<SortDTO> sorts) {
        super(pageIndex, pageSize);
        this.sorts = sorts;
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     * @param orderDesc 排序方式
     * @param orderBy 排序字段
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, Integer orderDesc, String... orderBy) {
        super(pageIndex, pageSize);
        this.sorts = Collections.singletonList(new SortDTO(orderDesc,  orderBy));
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, SortDTO sorts) {
        super(pageIndex, pageSize);
        this.sorts = Collections.singletonList(sorts);
    }


    public List<SortDTO> getSorts() {
        if(null == sorts || sorts.isEmpty()){
            return Collections.singletonList(new SortDTO());
        }else {
            return sorts;
        }
    }

    public void setSorts(List<SortDTO> sorts) {
        this.sorts = sorts;
    }


    /**
     * 默认的 排序是ID，当表字段中没有ID时可以用这个改默认排序字段
     * @param orderBy 排序字段 （默认排序方式为倒叙）
     */
    public SortPageDTO settingDefOrderBy(String... orderBy){
        this.sorts = Collections.singletonList(new SortDTO(orderBy));
        return this;
    }

    /**
     * 默认的 排序是ID，当表字段中没有ID时可以用这个改默认排序字段
     * @param orderDesc   排序方式 正序0--Direction.ASC，反序1--Direction.DESC [0-1]
     * @param orderBy 排序字段
     */
    public SortPageDTO settingDefOrderBy(Integer orderDesc, String... orderBy){
        this.sorts = Collections.singletonList(new SortDTO(orderDesc, orderBy));
        return this;
    }


    @Override
    public String toString() {
        return "SortPageDTO{" +
                "sorts=" + sorts +
                '}';
    }
}
