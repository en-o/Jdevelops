package cn.tannn.jdevelops.util.excel.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.read.metadata.holder.ReadRowHolder;
import cn.idev.excel.util.ListUtils;
import cn.tannn.jdevelops.util.excel.model.CommonExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 自定义 Excel 数据监听器
 * 支持批量处理和行号自动填充
 *
 * @param <T> 继承自 CommonExcel 的数据模型
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/10/21 下午3:28
 */
public class ExcelDataFunListener<T extends CommonExcel> implements ReadListener<T> {

	private static final Logger LOG = LoggerFactory.getLogger(ExcelDataFunListener.class);

	/**
	 * 默认批处理大小
	 */
	private static final int DEFAULT_BATCH_SIZE = 1000;

	/**
	 * 缓存的数据列表
	 */
	private final List<T> cachedDataList = ListUtils.newArrayList();

	/**
	 * 数据处理函数
	 */
	private final Consumer<List<T>> dataHandler;

	/**
	 * 批处理大小
	 */
	private final int batchSize;

	/**
	 * 已处理的总行数
	 */
	private long processedCount = 0;

	/**
	 * 构造函数 - 使用默认批处理大小
	 *
	 * @param dataHandler 数据处理函数，不能为空
	 */
	public ExcelDataFunListener(Consumer<List<T>> dataHandler) {
		this(dataHandler, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 构造函数 - 自定义批处理大小
	 *
	 * @param dataHandler 数据处理函数，不能为空
	 * @param batchSize   批处理大小，必须大于0
	 */
	public ExcelDataFunListener(Consumer<List<T>> dataHandler, int batchSize) {
		this.dataHandler = Objects.requireNonNull(dataHandler, "数据处理函数不能为空");
		if (batchSize <= 0) {
			throw new IllegalArgumentException("批处理大小必须大于0");
		}
		this.batchSize = batchSize;
		LOG.info("Excel监听器初始化完成，批处理大小: {}", batchSize);
	}

	/**
	 * 处理每一行数据
	 */
	@Override
	public void invoke(T data, AnalysisContext context) {
		if (data == null) {
			LOG.warn("读取到空数据，跳过处理");
			return;
		}

		try {

			// 获取行号
			ReadRowHolder readRowHolder = context.readRowHolder();
			if (readRowHolder != null) {
				Integer rowIndex = readRowHolder.getRowIndex();
				if (rowIndex != null) {
					// 设置行号（从1开始）
					data.setRowIndex(rowIndex + 1);
				}
			}

			// 添加到缓存
			cachedDataList.add(data);
			processedCount++;

			// 达到批处理大小时执行处理
			if (cachedDataList.size() >= batchSize) {
				processBatch();
			}

		} catch (Exception e) {
			LOG.error("处理Excel数据时发生错误，行号: {}",
					data.getRowIndex() != null ? data.getRowIndex() : "未知", e);
			throw new RuntimeException("Excel数据处理失败", e);
		}
	}

	/**
	 * 所有数据解析完成后的处理
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		try {
			// 处理剩余数据
			if (!cachedDataList.isEmpty()) {
				processBatch();
			}

			LOG.info("Excel数据解析完成！总计处理行数: {}", processedCount);

		} catch (Exception e) {
			LOG.error("完成Excel解析时发生错误", e);
			throw new RuntimeException("Excel解析完成处理失败", e);
		}
	}

	/**
	 * 批量处理数据
	 */
	private void processBatch() {
		if (cachedDataList.isEmpty()) {
			return;
		}

		try {
			// 创建数据副本，避免并发修改
			List<T> dataToProcess = ListUtils.newArrayList(cachedDataList);

			// 清空缓存
			cachedDataList.clear();

			// 执行数据处理
			dataHandler.accept(dataToProcess);

			LOG.debug("批量处理完成，处理数据量: {}", dataToProcess.size());

		} catch (Exception e) {
			LOG.error("批量处理数据时发生错误", e);
			throw new RuntimeException("批量数据处理失败", e);
		}
	}

	/**
	 * 获取已处理的行数
	 *
	 * @return 已处理的行数
	 */
	public long getProcessedCount() {
		return processedCount;
	}

	/**
	 * 获取当前缓存的数据量
	 *
	 * @return 缓存数据量
	 */
	public int getCachedSize() {
		return cachedDataList.size();
	}

	/**
	 * 获取批处理大小
	 *
	 * @return 批处理大小
	 */
	public int getBatchSize() {
		return batchSize;
	}
}
