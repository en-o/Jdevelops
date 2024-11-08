package cn.tannn.jdevelops.util.excel.listener;

import cn.tannn.jdevelops.util.excel.model.CommonExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 自定义监听器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/10/21 下午3:28
 */
public class ExcelDataListener<T extends CommonExcel> implements ReadListener<T> {

	private static final Logger LOG = LoggerFactory.getLogger(ExcelDataListener.class);

	/**
	 * 缓存的数据
	 */
	private final List<T> cachedDataList = ListUtils.newArrayList();

	@Override
	public void invoke(T data, AnalysisContext analysisContext) {
		// 获取行号
		ReadRowHolder readRowHolder = analysisContext.readRowHolder();
		Integer rowIndex = readRowHolder.getRowIndex();

		data.setRowIndex(rowIndex + 1);
		cachedDataList.add(data);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {

	}
}
