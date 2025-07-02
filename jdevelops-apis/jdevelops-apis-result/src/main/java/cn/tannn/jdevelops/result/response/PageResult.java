package cn.tannn.jdevelops.result.response;

import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询返回的指定对象
 *
 * @author lxw
 * @date 2019年1月17日
 */
@Schema(description = "分页查询返回的指定对象")
@JsonView(Views.Public.class)
public class PageResult<B> implements Serializable {

	/**
	 * 页码
	 */
	@Schema(description = "页码")
	private Integer currentPage;
	/**
	 * 数据量
	 */
	@Schema(description = "每页显示条数")
	private Integer pageSize;
	/**
	 * 总页数
	 */
	@Schema(description = "总页数")
	private Integer totalPages;
	/**
	 * 总记录数
	 */
	@Schema(description = "总记录数")
	private Long total;
	/**
	 * 数据"
	 */
	@Schema(description = "数据对象")
	private List<B> rows;

	public PageResult() {
	}

	public PageResult(Integer currentPage, Integer pageSize, Integer totalPages, Long total, List<B> rows) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalPages = totalPages;
		this.total = total;
		this.rows = rows;
	}

	/**
	 * @param pageIndex  当前页
	 * @param pageSize   每页显示条数
	 * @param totalPages 总页数
	 * @param total      总记录数
	 * @param rows       数据
	 * @param <B>        实体
	 * @return ResourcePage
	 */
	public static <B> PageResult<B> page(Integer pageIndex,
										 Integer pageSize,
										 Integer totalPages,
										 Long total,
										 List<B> rows) {
		PageResult<B> tResourcePageUtil = new PageResult<>();
		tResourcePageUtil.setCurrentPage(pageIndex);
		tResourcePageUtil.setPageSize(pageSize);
		tResourcePageUtil.setTotalPages(totalPages);
		tResourcePageUtil.setTotal(total);
		tResourcePageUtil.setRows(rows);
		return tResourcePageUtil;
	}

	/**
	 * @param page {@link Paging}
	 * @param total     总记录数
	 * @param rows      数据
	 * @param <T>       t
	 * @return ResourcePage
	 */
	public static <T> PageResult<T> page(Paging page,
										 Long total,
										 List<T> rows) {
		Integer pageSize = page.getPageSize();
		PageResult<T> tResourcePageUtil = new PageResult<>();
		tResourcePageUtil.setCurrentPage(page.getPageIndex());
		tResourcePageUtil.setPageSize(pageSize);
		// 计算总页码
		Integer totalPage = Math.toIntExact((total + pageSize - 1) / pageSize);
		tResourcePageUtil.setTotalPages(totalPage);
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
	public static <T> PageResult<T> page(Integer pageIndex,
										 Integer pageSize,
										 Long total,
										 List<T> rows) {
		PageResult<T> tResourcePageUtil = new PageResult<>();
		tResourcePageUtil.setCurrentPage(pageIndex);
		tResourcePageUtil.setPageSize(pageSize);
		// 计算总页码
		Integer totalPage = Math.toIntExact((total + pageSize - 1) / pageSize);
		tResourcePageUtil.setTotalPages(totalPage);
		tResourcePageUtil.setTotal(total);
		tResourcePageUtil.setRows(rows);
		return tResourcePageUtil;
	}

	public static <T> PageResult<T> page(Long total, List<T> rows) {
		PageResult<T> tResourcePageUtil = new PageResult<>();
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

	public List<B> getRows() {
		return rows;
	}

	public void setRows(List<B> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "ResourcePage{" +
				"currentPage=" + currentPage +
				", pageSize=" + pageSize +
				", totalPages=" + totalPages +
				", total=" + total +
				", rows=" + rows +
				'}';
	}
}
