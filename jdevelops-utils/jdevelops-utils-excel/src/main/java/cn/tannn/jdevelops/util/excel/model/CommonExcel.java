package cn.tannn.jdevelops.util.excel.model;


import cn.idev.excel.annotation.ExcelIgnore;

import java.util.Objects;


/**
 * 采样数据导入映射
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/10/21 下午3:28
 */
public class CommonExcel {

	@ExcelIgnore
	private Integer rowIndex;

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
            return true;
        }
		if (o == null || getClass() != o.getClass()) {
            return false;
        }
		CommonExcel that = (CommonExcel) o;
		return Objects.equals(rowIndex, that.rowIndex);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(rowIndex);
	}
}
