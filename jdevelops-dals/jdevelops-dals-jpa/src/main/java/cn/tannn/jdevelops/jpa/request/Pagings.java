package cn.tannn.jdevelops.jpa.request;

import cn.tannn.jdevelops.result.request.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页JPA扩展
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/8 上午10:59
 */
@Schema(description = "Jpa分页参数")
public class Pagings extends Paging {

    public Pagings() {
    }

    public Pagings(Integer pageSize) {
        super(pageSize);
    }

    public Pagings(Integer pageIndex, Integer pageSize) {
        super(pageIndex, pageSize);
    }

    public Pageable pageable() {
        return PageRequest.of(getPageIndex(),
                getPageSize());
    }

    /**
     * add Sort
     *
     * @param sort {@link Sort}
     * @return Pageable
     */
    public Pageable  pageable(Sort sort) {
        if (sort == null) {
            return PageRequest.of(getPageIndex(),
                    getPageSize());
        }
        return PageRequest.of(getPageIndex(),
                getPageSize(),
                sort);
    }


    /**
     * add Sort
     *
     * @param sort {@link Sorteds}
     * @return Pageable
     */
    public Pageable pageable(Sorteds sort) {
        if(sort == null){
            return PageRequest.of(getPageIndex(),getPageSize());
        }
        return PageRequest.of(getPageIndex(),
                getPageSize(),
                Sorteds.sort(sort));
    }






    public static Pagings defs(){
        return new Pagings();
    }


    /**
     * Paging to Pageable
     *
     * @param paging {@link Pagings}
     * @return Pageable
     */
    public static Pageable pageable(Pagings paging) {
        if (paging == null) {
            paging = Pagings.defs();
        }
        return paging.pageable();
    }

    /**
     * Paging to Pageable
     *
     * @param paging {@link Pagings}
     * @param sort {@link Sorteds}
     * @return Pageable
     */
    public static Pageable pageable(Pagings paging, Sorteds sort) {
        if (paging == null) {
            paging = Pagings.defs();
        }
        return paging.pageable(sort);
    }

    /**
     * Sort to Pageable ： 默认分页+自定义排序
     *
     * @param sort {@link Sorteds}
     * @return Pageable
     */
    public static Pageable pageableSorted(Sorteds sort) {
        Pagings paging = Pagings.defs();
        return  paging.pageable(sort);
    }

    /**
     * Sort to Pageable ： 默认分页+自定义排序
     *
     * @param sort {@link Sort}
     * @return Pageable
     */
    public static Pageable pageableSorted(Sort sort) {
        Pagings paging = Pagings.defs();
        return  paging.pageable(sort);
    }
}
