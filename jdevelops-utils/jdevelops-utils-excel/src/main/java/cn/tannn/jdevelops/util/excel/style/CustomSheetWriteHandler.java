package cn.tannn.jdevelops.util.excel.style;

import cn.idev.excel.write.handler.SheetWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

/**
 * EasyExcel设置单元格格式为文本的方法
 *  <p>.registerWriteHandler(new CustomSheetWriteHandler()) // 注册自定义的SheetWriteHandler</p>
 * @see <a href="https://comate.baidu.com/zh/page/YwkOKAgptJ">EasyExcel设置单元格格式为文本的方法</a>
 */
public class CustomSheetWriteHandler implements SheetWriteHandler {

    /**
     * 设置列数，可以根据需要调整
     * <p> 从0开始 </p>
     * <p> 当 appoint = true 时，column = 指定列 </p>
     */
    private final Integer column;
    /**
     * 是否指定列
     */
    private final boolean appoint;

    /**
     * 设置所有列为文本格式
     * @param column 列数量【从0开始 】
     */
    public CustomSheetWriteHandler(int column) {
        this.column = column;
        this.appoint = false;
    }

    /**
     * 自定义列为文本格式
     * @param column  列数量【当 appoint = true 时，column = 指定列】【从0开始 】
     * @param appoint 是否指定列
     */
    public CustomSheetWriteHandler(int column, boolean appoint) {
        this.column = column;
        this.appoint = appoint;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 在创建sheet之前执行的操作
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getCachedWorkbook();
        SXSSFSheet sheet = (SXSSFSheet) writeSheetHolder.getSheet();
        if(appoint){
            style(workbook, sheet, column);
        }else {
            for (int i = 0; i < column; i++) {
                style(workbook, sheet, i);
            }
        }
    }

    private static void style(Workbook workbook, SXSSFSheet sheet, int column) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat((short) 49); // 设置为文本格式
        sheet.setDefaultColumnStyle(column, cellStyle); // 将整列设置为文本格式
        //  cellStyle.setWrapText(true);
    }
}
