package cn.tannn.jdevelops.jpa.request;

import cn.tannn.jdevelops.result.request.PagingSorted;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 分页JPA扩展
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/12 下午12:16
 */
@Schema(description = "Jpa分页排序参数")
public class PagingSorteds extends PagingSorted {


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
