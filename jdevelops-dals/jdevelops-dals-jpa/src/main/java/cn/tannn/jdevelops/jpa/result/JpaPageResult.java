package cn.tannn.jdevelops.jpa.result;

import cn.tannn.jdevelops.result.bean.SerializableBean;
import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.response.PageResult;
import cn.tannn.jdevelops.result.utils.ListTo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

/**
 * JPA 分页查询返回的指定对象
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 17:24
 */
@Schema(description = "JPA分页数据实体")
public class JpaPageResult<B> extends PageResult<B> {

    /**
     * 空对象
     */
    public  JpaPageResult() {
        super(1,
                20,
                0,
                0L,
                Collections.emptyList());
    }

    /**
     * 空对象
     */
    public JpaPageResult(Paging page) {
        super(page.getPageIndex(),
                page.getPageSize(),
                0,
                0L,
                Collections.emptyList());
    }

    public JpaPageResult(Integer currentPage, Integer pageSize, Integer totalPages, Long total, List<B> rows) {
        super(currentPage
                , pageSize
                , totalPages
                , total
                , rows);
    }

    public JpaPageResult(Page<B> rows) {
        super(rows.getNumber() + 1
                , rows.getSize()
                , rows.getTotalPages()
                , rows.getTotalElements()
                , rows.getContent());
    }



    /**
     * 对象转换
     * @param rows 数据
     */
    public static <R> JpaPageResult<R> toPage(Page<R> rows) {
        return new JpaPageResult<>(rows.getNumber()+1,
                rows.getSize(),
                rows.getTotalPages(),
                rows.getTotalElements(),
                rows.getContent());
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
