package cn.jdevelops.authentication.sas.server.util;

import cn.jdevelops.api.result.request.SortDTO;
import cn.jdevelops.api.result.request.SortPageDTO;
import cn.jdevelops.api.result.response.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分页
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 01:42
 */
public class PageUtil {

    /**
     *  SortPageDTO  -> Pageable
     * @param sortPage sortPage
     * @return Pageable
     */
    public static Pageable sortPageOf(SortPageDTO sortPage){
        return  PageRequest.of(sortPage.getPageIndex(),
                sortPage.getPageSize(),
                sortOf(sortPage.getSorts()));
    }


    /**
     * SortDTO  -> Sort
     * @param sorts sorts
     * @return Sort
     */
    public static Sort sortOf(List<SortDTO> sorts){
        return sorts.stream()
                .map(s -> {
                    // （正序0，反序1）
                    if (s.getOrderDesc() == 0) {
                        return Sort.by(s.getOrderBy()).ascending();
                    } else {
                        return Sort.by(s.getOrderBy()).descending();
                    }
                })
                .reduce(Sort::and).orElse(null);
    }


    /**
     * 对象转换
     * @param rows 数据
     */
    public static <R> PageResult<R> toPage(Page<R> rows) {
        return new PageResult<>(rows.getNumber()+1,
                rows.getSize(),
                rows.getTotalPages(),
                rows.getTotalElements(),
                rows.getContent());
    }


}
