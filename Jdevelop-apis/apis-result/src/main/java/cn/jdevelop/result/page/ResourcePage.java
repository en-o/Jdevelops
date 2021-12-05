package cn.jdevelop.result.page;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 分页返回统一工具类
 *
 * @author lxw
 * @date 2019年1月17日
 */
@ApiModel(value = "分页返回统一工具类", description = "分页返回统一工具类")
public class ResourcePage<T> implements Serializable {

	@ApiModelProperty("当前页")
	private Integer currentPage;
	@ApiModelProperty("每页显示条数")
	private Integer pageSize;
	@ApiModelProperty("总页数")
	private Integer totalPages;
	@ApiModelProperty("总记录数")
	private Long total;
	@ApiModelProperty("数据")
	private T rows;

	public ResourcePage() {
	}


	/**
	 * @param pageIndex  当前页
	 * @param pageSize   每页显示条数
	 * @param totalPages 总页数
	 * @param total      总记录数
	 * @param rows       数据
	 * @param <T>        t
	 * @return ResourcePage
	 */
	public static <T> ResourcePage<T> page(Integer pageIndex,
										   Integer pageSize,
										   Integer totalPages,
										   Long total,
										   T rows) {
		ResourcePage<T> tResourcePageUtil = new ResourcePage<>();
		tResourcePageUtil.setCurrentPage(pageIndex);
		tResourcePageUtil.setPageSize(pageSize);
		tResourcePageUtil.setTotalPages(totalPages);
		tResourcePageUtil.setTotal(total);
		tResourcePageUtil.setRows(rows);
		return tResourcePageUtil;
	}

	/**
	 * @param pageIndex 当前页
	 * @param pageSize  每页显示条数
	 * @param total     总记录数
	 * @param rows      数据
	 * @param <T>       t
	 * @return ResourcePage
	 */
	public static <T> ResourcePage<T> page(Integer pageIndex,
										   Integer pageSize,
										   Long total,
										   T rows) {
		ResourcePage<T> tResourcePageUtil = new ResourcePage<>();
		tResourcePageUtil.setCurrentPage(pageIndex);
		tResourcePageUtil.setPageSize(pageSize);
		// 计算总页码
		Integer totalPage = Math.toIntExact((total + pageSize - 1) / pageSize);
		tResourcePageUtil.setTotalPages(totalPage);
		tResourcePageUtil.setTotal(total);
		tResourcePageUtil.setRows(rows);
		return tResourcePageUtil;
	}

	public static <T> ResourcePage<T> page(Long total, T rows) {
		ResourcePage<T> tResourcePageUtil = new ResourcePage<>();
		tResourcePageUtil.setTotal(total);
		tResourcePageUtil.setRows(rows);
		return tResourcePageUtil;
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
	 * @param rows rows
	 * @author lxw
	 * @date 2019年3月25日
	 */
	public void setRows(T rows) {
		this.rows = rows;
	}


}
