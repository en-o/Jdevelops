package cn.tannn.jdevelops.util.excel.style;

import cn.idev.excel.write.style.row.AbstractRowHeightStyleStrategy;
import org.apache.poi.ss.usermodel.Row;

/**
 * 行高设置
 * <p> 设置表头的自动调整行高策略
 * <code>
 * <p> ExcelWriterBuilder excelWriterBuilder = EasyExcelFactory.write()
 * <p> //设置行高的策略
 * <p>.registerWriteHandler(new CellRowHeightStyleStrategy())
 * </code>
 * @author web
 */
public class CellRowHeightStyleStrategy extends AbstractRowHeightStyleStrategy {

    @Override
    protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
        //设置主标题行高为17.7
        if(relativeRowIndex == 0){
            //如果excel需要显示行高为15，那这里就要设置为15*20=300
            row.setHeight((short) 3240);
        }
    }

    @Override
    protected void setContentColumnHeight(Row row, int relativeRowIndex) {
    }
}
