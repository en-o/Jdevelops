package com.detabes.search.es.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * ES分页结果类
 *
 * @author l
 */
@Getter
@Setter
@ToString
public class EsPage<T> implements Serializable {
	@ApiModelProperty("当前页")
	private Integer currentPage;
	@ApiModelProperty("每页显示条数")
	private Integer pageSize;
	@ApiModelProperty("总页数")
	private Integer totalPages;
	@ApiModelProperty("总记录数")
	private Integer total;
	@ApiModelProperty("数据")
	private T rows;

	public EsPage() {
	}


	/**
	 * @param pageIndex 当前页
	 * @param pageSize  每页显示条数
	 * @param total     总记录数
	 * @param rows      数据
	 * @param <T>       t
	 * @return EsPage<T>
	 */
	public static <T> EsPage<T> page(Integer pageIndex,
									 Integer pageSize,
									 Integer total,
									 T rows) {
		EsPage<T> tResourcePageUtil = new EsPage<>();
		tResourcePageUtil.setCurrentPage(pageIndex);
		tResourcePageUtil.setPageSize(pageSize);
		// 计算总页码
		Integer totalPage = (total + pageSize - 1) / pageSize;
		tResourcePageUtil.setTotalPages(totalPage);
		tResourcePageUtil.setTotal(total);
		tResourcePageUtil.setRows(rows);
		return tResourcePageUtil;
	}

	public static <T> EsPage<T> page(Integer total, T rows) {
		EsPage<T> tResourcePageUtil = new EsPage<>();
		tResourcePageUtil.setTotal(total);
		tResourcePageUtil.setRows(rows);
		return tResourcePageUtil;
	}

	/**
	 * 获取当前页
	 *
	 * @return Integer
	 * @author lxw
	 * @date 2019年1月17日
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * 设定当前页
	 *
	 * @param currentPage 设定当前页
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
	 * @param pageSize 每页显示条数
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
	 * @param totalPages 总页数
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
	public Integer getTotal() {
		return total;
	}

	/**
	 * 总记录数
	 *
	 * @param total 总记录数
	 * @author lxw
	 * @date 2019年3月25日
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}

	/**
	 * 当前页结果集
	 *
	 * @return T
	 * @author lxw
	 * @date 2019年3月25日
	 */
	public T getRows() {
		return rows;
	}

	/**
	 * 当前页结果集
	 *
	 * @param rows 当前页结果集
	 * @author lxw
	 * @date 2019年3月25日
	 */
	public void setRows(T rows) {
		this.rows = rows;
	}
}
