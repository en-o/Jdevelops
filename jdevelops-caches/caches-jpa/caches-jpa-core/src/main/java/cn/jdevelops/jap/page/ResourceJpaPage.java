package cn.jdevelops.jap.page;


import cn.jdevelops.entity.basics.vo.SerializableVO;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 分页返回统一工具类
 *
 * @author lxw
 * @date 2019年1月17日
 */
@ApiModel(value = "JPA分页返回统一工具类", description = "JPA分页返回统一工具类")
public class ResourceJpaPage<T> implements Serializable {

	@ApiModelProperty("当前页")
	private Integer currentPage;
	@ApiModelProperty("每页显示条数")
	private Integer pageSize;
	@ApiModelProperty("总页数")
	private Integer totalPages;
	@ApiModelProperty("总记录数")
	private Long total;
	@ApiModelProperty("数据")
	private List<T> rows;

	public ResourceJpaPage() {
	}


	public ResourceJpaPage(Page<T> rows) {
		this.currentPage = rows.getNumber()+1;
		this.pageSize = rows.getSize();
		this.totalPages = rows.getTotalPages();
		this.total = rows.getTotalElements();
		this.rows = rows.getContent();
	}

	public <S extends SerializableVO> ResourceJpaPage(Page<S> rows, Class<T> clazz) {
		List<S> content = rows.getContent();
		List<T> result = new ArrayList(content.size());
		Iterator var3 = content.iterator();
		while (var3.hasNext()) {
			SerializableVO abs = (SerializableVO) var3.next();
			result.add((T) abs.to(clazz));
		}
		this.currentPage = rows.getNumber()+1;
		this.pageSize = rows.getSize();
		this.totalPages = rows.getTotalPages();
		this.total = rows.getTotalElements();
		this.rows = result;
	}

	/**
	 * 获取当前页
	 *
	 * @return  Integer
	 * @author lxw
	 * @date 2019年1月17日
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * 设定当前页
	 *
	 * @param currentPage currentPage
	 * @author lxw
	 * @date 2019年1月17日
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 获取每页显示条数
	 *
	 * @return Integer
	 * @author lxw
	 * @date 2019年1月17日
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * 设定每页显示条数
	 *
	 * @param pageSize pageSize
	 * @author lxw
	 * @date 2019年1月17日
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取总记录数
	 *
	 * @return Integer
	 * @author lxw
	 * @date 2019年1月17日
	 */
	public Integer getTotalPages() {
		return totalPages;
	}

	/**
	 * 设定总页数
	 *
	 * @param totalPages totalPages
	 * @author lxw
	 * @date 2019年1月17日
	 */
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * 总记录数
	 *
	 * @return Long
	 * @author lxw
	 * @date 2019年3月25日
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * 总记录数
	 *
	 * @param total total
	 * @author lxw
	 * @date 2019年3月25日
	 */
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * 当前页结果集
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 当前页结果集
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "ResourceJpaPage{" +
				"currentPage=" + currentPage +
				", pageSize=" + pageSize +
				", totalPages=" + totalPages +
				", total=" + total +
				", rows=" + JSON.toJSONString(rows) +
				'}';
	}
}
