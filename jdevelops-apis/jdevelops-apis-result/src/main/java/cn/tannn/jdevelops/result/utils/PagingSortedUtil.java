package cn.tannn.jdevelops.result.utils;

import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.request.PagingSorted;
import cn.tannn.jdevelops.result.request.Sorted;

/**
 * 分页相关
 *
 * @author tn
 * @version 1
 * @date 2021/1/26 23:55
 */
public final class PagingSortedUtil {

    /**
     * 判断 PageVO 是否为空，空就new一个默认的
     *
     * @param page page
     * @return PageDTO
     */
    public static Paging pageDef(Paging page) {
        if (page == null) {
            return new Paging();
        }
        return page;
    }

    /**
     * 判断 SortDTO 是否为空，空就new一个默认的
     *
     * @param sort sort
     * @return PageDTO
     */
    public static Sorted sortDef(Sorted sort) {
        if (sort == null) {
            return new Sorted();
        }
        return sort;
    }


    /**
     * 判断 SortPageDTO 是否为空，空就new一个默认的
     *
     * @param sortPage SortPageDTO
     * @return SortPageDTO
     */
    public static PagingSorted sortPageDef(PagingSorted sortPage) {
        if (sortPage == null) {
            return new PagingSorted();
        }
        return sortPage;
    }

}
