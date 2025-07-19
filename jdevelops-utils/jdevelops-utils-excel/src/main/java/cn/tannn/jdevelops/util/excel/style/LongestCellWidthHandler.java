package cn.tannn.jdevelops.util.excel.style;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

/**
 * 根据表头设置列宽
 * <p> 固定宽度 columnWidth * 5, 20 </p>
 */
public class LongestCellWidthHandler extends AbstractColumnWidthStyleStrategy {
    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (Boolean.TRUE.equals(isHead)) {
            int columnWidth = cell.getStringCellValue().length();
            columnWidth = Math.max(columnWidth * 5, 20);
            if (columnWidth > 255) {
                columnWidth = 255;
            }
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        }
    }
}
