package cn.tannn.jdevelops.result.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * 分页排序
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/8 上午11:01
 */
@Schema(description = "分页排序参数")
public class PagingSorted extends Paging{

    /**
     * 范围：[1-5]
     * 排序
     */
    @Valid
    @Size(min = 1, max = 5)
    List<Sorted> sorts;

    public PagingSorted() {
    }

    /**
     * 默认
     */
    public static PagingSorted def(){
        return new PagingSorted();
    }

    /**
     * 分页排序
     *
     * @param pageSize 数量
     */
    public PagingSorted(Integer pageSize) {
        super(pageSize);
    }


    /**
     * 分页排序
     *
     * @param pageSize 数量
     * @param sorts    排序
     */
    public PagingSorted(Integer pageSize, List<Sorted> sorts) {
        super(pageSize);
        this.sorts = sorts;
    }


    /**
     * 分页排序
     *
     * @param pageSize 数量
     * @param sorts    排序
     */
    public PagingSorted(Integer pageSize, Sorted sorts) {
        super(pageSize);
        ArrayList<Sorted> sortArray = new ArrayList<>();
        sortArray.add(sorts);
        this.sorts = sortArray;
    }


    /**
     * 分页排序
     *
     * @param sorts 排序
     */
    public PagingSorted(List<Sorted> sorts) {
        this.sorts = sorts;
    }


    /**
     * 分页排序
     *
     * @param pageIndex 页码
     * @param pageSize  数量
     */
    public PagingSorted(Integer pageIndex, Integer pageSize) {
        super(pageIndex, pageSize);
    }


    /**
     * 分页排序
     *
     * @param pageIndex 页码
     * @param pageSize  数量
     * @param sorts     排序
     */
    public PagingSorted(Integer pageIndex, Integer pageSize, List<Sorted> sorts) {
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
    public PagingSorted(Integer pageIndex, Integer pageSize, Integer orderDesc, String... orderBy) {
        super(pageIndex, pageSize);
        ArrayList<Sorted> sortArray = new ArrayList<>();
        sortArray.add(new Sorted(orderDesc, orderBy));
        this.sorts = sortArray;
    }


    /**
     * 分页排序
     *
     * @param pageIndex 页码
     * @param pageSize  数量
     * @param sorts     排序
     */
    public PagingSorted(Integer pageIndex, Integer pageSize, Sorted sorts) {
        super(pageIndex, pageSize);
        ArrayList<Sorted> sortArray = new ArrayList<>();
        sortArray.add(sorts);
        this.sorts = sortArray;
    }


    public List<Sorted> getSorts() {
        if (null == sorts || sorts.isEmpty()) {
            ArrayList<Sorted> sortArray = new ArrayList<>();
            sortArray.add(new Sorted());
            return sortArray;
        } else {
            return sorts;
        }
    }

    public void setSorts(List<Sorted> sorts) {
        this.sorts = sorts;
    }


    /**
     *当排序为空是修改默认的排序字段,若不为空在原有的排序基础上追加默认自定义排序
     *
     * @param orderBy 排序字段 （默认排序方式为倒叙）
     */
    public PagingSorted optionsDefOrderBy(String... orderBy) {
        if (this.sorts == null) {
            ArrayList<Sorted> sortArray = new ArrayList<>();
            sortArray.add(new Sorted(orderBy));
            this.sorts = sortArray;
        } else {
            this.sorts.add(new Sorted(orderBy));
        }
        return this;
    }


    /**
     * 当排序为空是修改默认的排序字段,若不为空则不进行操作
     *
     * @param orderBy 排序字段 （默认排序方式为倒叙）
     */
    public PagingSorted optionsNullDefOrderBy(String... orderBy) {
        if (this.sorts == null) {
            ArrayList<Sorted> sortArray = new ArrayList<>();
            sortArray.add(new Sorted(orderBy));
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
    public PagingSorted optionsDefOrderBy(Integer orderDesc, String... orderBy) {
        if (this.sorts == null) {
            ArrayList<Sorted> sortArray = new ArrayList<>();
            sortArray.add(new Sorted(orderDesc, orderBy));
            this.sorts = sortArray;
        } else {
            this.sorts.add(new Sorted(orderDesc, orderBy));
        }

        return this;
    }


    /**
     * 当排序为空是修改默认的排序字段,若不为空则不进行操作
     *
     * @param orderDesc 排序方式 正序0--Direction.ASC，反序1--Direction.DESC [0-1]
     * @param orderBy   排序字段
     */
    public PagingSorted optionsNullDefOrderBy(Integer orderDesc, String... orderBy) {
        if (this.sorts == null) {
            ArrayList<Sorted> sortArray = new ArrayList<>();
            sortArray.add(new Sorted(orderDesc, orderBy));
            this.sorts = sortArray;
        }
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PagingSorted.class.getSimpleName() + "[", "]")
                .add("sorts=" + sorts)
                .toString();
    }
}
