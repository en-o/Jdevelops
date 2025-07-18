package cn.tannn.jdevelops.util.excel.style;

import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.CellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel表格列宽自动调整策略
 * 用于处理复杂表头的列宽设置，可自动根据内容调整列宽
 *
 * <p>使用示例：
 * <pre>
 * ExcelWriterBuilder excelWriterBuilder = EasyExcelFactory.write()
 *     .registerWriteHandler(new CellWidthStyleStrategy())
 * </pre>
 *
 * @author web
 */
public class CellWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

    /** 缓存每个sheet中每列的最大宽度 */
    private Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

    /**
     * 设置列宽
     *
     * @param writeSheetHolder Excel工作表持有者
     * @param cellDataList 单元格数据列表
     * @param cell 当前单元格
     * @param head 表头信息
     * @param relativeRowIndex 相对行索引(从0开始)
     * @param isHead 是否是表头
     */
    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Map<Integer, Integer> maxColumnWidthMap = CACHE.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>());
        if (Boolean.TRUE.equals(isHead)) {
            if(relativeRowIndex == 1){
                int length = cell.getStringCellValue().getBytes().length;
                Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
                if (maxColumnWidth == null || length > maxColumnWidth) {
                    maxColumnWidthMap.put(cell.getColumnIndex(), length);
                    writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), length * 300);
                }
            }
        }else{
            Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
            if (columnWidth >= 0) {
                if (columnWidth > 255) {
                    columnWidth = 255;
                }
                Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
                if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                    maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
                    writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
                }
            }
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (Boolean.TRUE.equals(isHead)) {
            return cell.getStringCellValue().getBytes().length;
        } else {
            CellData<?> cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                return switch (type) {
                    case STRING -> cellData.getStringValue().getBytes().length;
                    case BOOLEAN -> cellData.getBooleanValue().toString().getBytes().length;
                    case NUMBER -> cellData.getNumberValue().toString().getBytes().length;
                    default -> -1;
                };
            }
        }
    }
}
