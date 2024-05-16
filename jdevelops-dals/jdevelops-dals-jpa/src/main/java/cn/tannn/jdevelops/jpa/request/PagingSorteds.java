package cn.tannn.jdevelops.jpa.request;

import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.request.PagingSorted;
import cn.tannn.jdevelops.result.request.Sorted;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 分页JPA扩展
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/12 下午12:16
 */
@Schema(description = "Jpa分页排序参数")
public class PagingSorteds extends PagingSorted {

    public PagingSorteds() {
    }

    public PagingSorteds(Integer pageSize) {
        super(pageSize);
    }

    public PagingSorteds(Integer pageSize, List<Sorted> sorts) {
        super(pageSize, sorts);
    }

    public PagingSorteds(Integer pageSize, Sorted sorts) {
        super(pageSize, sorts);
    }

    public PagingSorteds(List<Sorted> sorts) {
        super(sorts);
    }

    public PagingSorteds(Integer pageIndex, Integer pageSize) {
        super(pageIndex, pageSize);
    }

    public PagingSorteds(Integer pageIndex, Integer pageSize, List<Sorted> sorts) {
        super(pageIndex, pageSize, sorts);
    }

    public PagingSorteds(Integer pageIndex, Integer pageSize, Integer orderDesc, String... orderBy) {
        super(pageIndex, pageSize, orderDesc, orderBy);
    }

    public PagingSorteds(Integer pageIndex, Integer pageSize, Sorted sorts) {
        super(pageIndex, pageSize, sorts);
    }


    @Override
    public PagingSorteds append(String... orderBy) {
        return pagingSorted(super.append(orderBy));
    }

    @Override
    public PagingSorteds append(Integer orderDesc, String... orderBy) {
        return pagingSorted(super.append(orderDesc, orderBy));
    }

    @Override
    public PagingSorteds fixSort(String... orderBy) {
        return pagingSorted(super.fixSort(orderBy));
    }

    @Override
    public PagingSorteds fixSort(Integer orderDesc, String... orderBy) {
        return pagingSorted(super.fixSort(orderDesc, orderBy));
    }

    /**
     * 获取分页 Pageable
     * @return Pageable
     */
    public  Pageable pageable() {
        return PageRequest.of(getPageIndex(),
                getPageSize(),
                Sorteds.sorteds2Sort2(getSorts()));
    }



    /**
     *  fix append 内用
     * @param pagingSorted PagingSorted
     * @return Sorteds
     */
    public PagingSorteds pagingSorted(PagingSorted pagingSorted) {
        setPageIndex(pagingSorted.getPageIndex());
        setSorts(pagingSorted.getSorts());
        setPageSize(pagingSorted.getPageSize());
        return this;
    }

    /**
     * 默认
     */
    public static PagingSorteds defs(){
        return new PagingSorteds();
    }

    /**
     * 获取分页 Pageable
     *
     * @param page 分页
     * @param sort 排序
     * @return Pageable
     */
    public static Pageable pageable(Pagings page, Sorteds sort) {
        return Pagings.pageable(page,sort);
    }

    /**
     * 获取分页 Pageable
     *
     * @param paging 分页 {@link PagingSorteds}
     * @return Pageable
     */
    public static Pageable pageable(PagingSorteds paging) {
        if(paging == null){
            paging = PagingSorteds.defs();
        }
        return paging.pageable();
    }

}
