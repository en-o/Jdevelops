package cn.tannn.jdevelops.util.excel.style;

import cn.idev.excel.metadata.data.DataFormatData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.handler.context.CellWriteHandlerContext;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import cn.idev.excel.write.metadata.style.WriteFont;
import cn.idev.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;

/**
 * 单元格格式设置 （ 设置表头和填充内容的样式）
 * <p> 设置表头和填充内容的样式
 * <code>
 * <p> ExcelWriterBuilder excelWriterBuilder = EasyExcelFactory.write()
 * <p> //设置行高的策略
 * <p>.registerWriteHandler(new CellWidthStyleStrategy());
 * </code>
 * @author web
 */
public class CellStyleStrategy extends HorizontalCellStyleStrategy {

    private final WriteCellStyle headWriteCellStyle;
    private final WriteCellStyle contentWriteCellStyle;

    /**
     * 操作列
     */
    private final List<Integer> columnIndexes;

    /**
     * 单元格格式设置
     * @param columnIndexes 操作列
     * @param headWriteCellStyle   设置头样式
     * @param contentWriteCellStyle  设置填充数据样式
     */
    public CellStyleStrategy(List<Integer> columnIndexes,WriteCellStyle headWriteCellStyle, WriteCellStyle contentWriteCellStyle) {
        this.columnIndexes = columnIndexes;
        this.headWriteCellStyle = headWriteCellStyle;
        this.contentWriteCellStyle = contentWriteCellStyle;
    }

    /**
     * 设置头样式
     * @param context CellWriteHandlerContext
     */
    @Override
    protected void setHeadCellStyle( CellWriteHandlerContext context) {
        // 获取字体实例
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        // 根据行索引为不同级别的表头应用不同样式
        //表头不同处理
        if (columnIndexes.get(0).equals(context.getRowIndex())) {
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            headWriteFont.setFontHeightInPoints((short) 12);
            headWriteFont.setBold(false);
            headWriteFont.setFontName("宋体");
        }else{
            headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            headWriteFont.setFontHeightInPoints((short) 11);
            headWriteFont.setBold(false);
            headWriteFont.setFontName("微软雅黑");
        }
        headWriteCellStyle.setWriteFont(headWriteFont);
        DataFormatData dataFormatData = new DataFormatData();
        dataFormatData.setIndex((short)49);
        headWriteCellStyle.setDataFormatData(dataFormatData);
        if (stopProcessing(context)) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(headWriteCellStyle, cellData.getOrCreateStyle());
    }

    /**
     * 设置填充数据样式
     * @param context CellWriteHandlerContext
     */
    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 11);
        //设置数据填充后的实线边框
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        DataFormatData dataFormatData = new DataFormatData();
        dataFormatData.setIndex((short)49);
        contentWriteCellStyle.setDataFormatData(dataFormatData);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(contentWriteCellStyle, cellData.getOrCreateStyle());
    }
}

