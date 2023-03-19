package cn.jdevelops.data.jap.page;

import cn.jdevelops.data.jap.util.ListTo;
import cn.jdevelops.result.bean.SerializableBean;
import cn.jdevelops.result.request.PageDTO;
import cn.jdevelops.result.response.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.*;

/**
 * JPA 分页查询返回的指定对象
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 17:24
 */
@Schema(description = "JPA分页数据实体")
public class JpaPageResult<R> extends PageResult<R> {


    public JpaPageResult(Integer currentPage, Integer pageSize, Integer totalPages, Long total, List<R> rows) {
        super(currentPage, pageSize, totalPages, total, rows);
    }

    /**
     * 解析  Page
     * @param rows Page
     */
    public JpaPageResult(Page<R> rows) {
        super(rows.getNumber()+1,
                rows.getSize(),
                rows.getTotalPages(),
                rows.getTotalElements(),
                rows.getContent());
    }

    /**
     * 返回空对象
     */
    public  JpaPageResult() {
        super(1,
                20,
                0,
                0L,
                Collections.emptyList());
    }

    /**
     * 返回空对象
     */
    public JpaPageResult(PageDTO page) {
        super(page.getPageIndex(),
                page.getPageSize(),
                0,
                0L,
                Collections.emptyList());
    }

    /**
     * 对象转换
     * @param rows 数据
     * @param clazz 返回对象
     */
    public static  <S extends SerializableBean<S>,R> JpaPageResult<R> toPage(Page<S> rows, Class<R> clazz) {
        List<S> content = rows.getContent();
        List<R> result = ListTo.to(clazz, content);
        return new JpaPageResult<>(rows.getNumber()+1,
                rows.getSize(),
                rows.getTotalPages(),
                rows.getTotalElements(),
                result);
    }

}
