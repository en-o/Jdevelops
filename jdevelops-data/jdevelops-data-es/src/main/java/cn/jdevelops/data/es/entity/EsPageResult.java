package cn.jdevelops.data.es.entity;

import cn.jdevelops.api.result.request.PageDTO;
import cn.jdevelops.api.result.response.PageResult;

import java.util.Collections;
import java.util.List;

/**
 * ES分页数据实体
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 17:24
 */
public class EsPageResult<R> extends PageResult<R> {

    public EsPageResult(Integer currentPage, Integer pageSize, Integer totalPages, Long total, List<R> rows) {
        super(currentPage, pageSize, totalPages, total, rows);
    }


    /**
     * 返回空对象
     */
    public EsPageResult() {
        super(1,
                20,
                0,
                0L,
                Collections.emptyList());
    }

    /**
     * 返回空对象
     */
    public EsPageResult(PageDTO page) {
        super(page.getPageIndex(),
                page.getPageSize(),
                0,
                0L,
                Collections.emptyList());
    }





}
