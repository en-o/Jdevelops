package cn.jdevelops.result.util;

import cn.jdevelops.result.request.PageDTO;
import cn.jdevelops.result.request.SortDTO;
import cn.jdevelops.result.request.SortPageDTO;

/**
 * 分页相关
 *
 * @author tn
 * @version 1
 * @date 2021/1/26 23:55
 */
public class PageUtil {

    /**
     * 判断 PageVO 是否为空，空就new一个默认的
     *
     * @param page page
     * @return PageDTO
     */
    public static PageDTO pageDef(PageDTO page) {
        if (page == null) {
            return new PageDTO();
        }
        return page;
    }

    /**
     * 判断 SortDTO 是否为空，空就new一个默认的
     *
     * @param sort sort
     * @return PageDTO
     */
    public static SortDTO sortDef(SortDTO sort) {
        if (sort == null) {
            return new SortDTO();
        }
        return sort;
    }


    /**
     * 判断 SortPageDTO 是否为空，空就new一个默认的
     *
     * @param sortPage SortPageDTO
     * @return SortPageDTO
     */
    public static SortPageDTO sortPageDef(SortPageDTO sortPage) {
        if (sortPage == null) {
            return new SortPageDTO();
        }
        return sortPage;
    }

}
