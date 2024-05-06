package cn.jdevelops.config.standalone.request;


import cn.jdevelops.api.result.request.SortPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页
 *
 * @author lxw
 * @date 2018年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageRequest extends SortPageDTO {


    public PageRequest() {
    }


    public Pageable getPageable() {
        return org.springframework.data.domain.PageRequest.of(getPageIndex(),
                getPageSize());
    }

    public Pageable getPageable(Sort sort) {
        return org.springframework.data.domain.PageRequest.of(getPageIndex(),
                getPageSize(),
                sort);
    }
}
