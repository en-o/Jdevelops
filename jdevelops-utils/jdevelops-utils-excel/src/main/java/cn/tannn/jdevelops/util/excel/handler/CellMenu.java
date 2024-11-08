
package cn.tannn.jdevelops.util.excel.handler;

import cn.tannn.jdevelops.util.excel.model.HeaderMenuData;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Collections;
import java.util.List;

/**
 * 单元格下拉
 * 处理下拉太多无法显示的问题
 * @author <a href="https://developer.aliyun.com/article/939661">web</a>
 */
public class CellMenu implements SheetWriteHandler {


    /**
     * 下拉框内容map Integer数据所在列数，string[]下拉数据列表
     */
    private List<HeaderMenuData> selectMap;

    private char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public CellMenu(List<HeaderMenuData> selectMap) {
        this.selectMap = selectMap;
    }

    public CellMenu(HeaderMenuData selectMap) {
        this.selectMap = Collections.singletonList(selectMap);
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (selectMap == null) {
            return;
        }
        // 需要设置下拉框的sheet页
        Sheet curSheet = writeSheetHolder.getSheet();

        selectMap.forEach(sm -> {

            DataValidationHelper helper = curSheet.getDataValidationHelper();
            //定义sheet的名称
            String dictSheetName = "dist"+sm.getIndex();
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            // 数据字典的sheet页
            Sheet dictSheet = workbook.createSheet(dictSheetName);

            // 设置下拉单元格的首行、末行、首列、末列
            CellRangeAddressList rangeAddressList = new CellRangeAddressList(1, 65533,
                    sm.getIndex(), sm.getIndex());
            int rowLen = sm.getPullDownData().size();
            // 设置字典sheet页的值 每一列一个字典项
            for (int i = 0; i < rowLen; i++) {
                Row row = dictSheet.getRow(i);
                if (row == null) {
                    row = dictSheet.createRow(i);
                }
                row.createCell( sm.getIndex()).setCellValue(sm.getPullDownData().get(i));
            }
            String excelColumn = getExcelColumn(sm.getIndex());
            // 下拉框数据来源 eg:字典sheet!$B1:$B2
            String refers = dictSheetName + "!$" + excelColumn + "$1:$" + excelColumn + "$" + rowLen;
            // 创建可被其他单元格引用的名称
            Name name = workbook.createName();
            // 设置名称的名字
            name.setNameName("dict" + sm.getIndex());
            // 设置公式
            name.setRefersToFormula(refers);
            // 设置引用约束
            DataValidationConstraint constraint = helper.createFormulaListConstraint("dict" + sm.getIndex());
            // 设置约束
            DataValidation validation = helper.createValidation(constraint, rangeAddressList);
            if (validation instanceof HSSFDataValidation) {
                validation.setSuppressDropDownArrow(false);
            } else {
                validation.setSuppressDropDownArrow(true);
                validation.setShowErrorBox(true);
            }
            // 阻止输入非下拉框的值
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox("提示", "此值与单元格定义格式不一致！");
            // 添加下拉框约束
            writeSheetHolder.getSheet().addValidationData(validation);
            //设置列为隐藏
            int hiddenIndex = workbook.getSheetIndex(dictSheet);
            if (!workbook.isSheetHidden(hiddenIndex)) {
                workbook.setSheetHidden(hiddenIndex, true);
            }
        });

    }

    /**
     * 将数字列转化成为字母列
     */
    private String getExcelColumn(int num) {
        String column = "";
        int len = alphabet.length - 1;
        int first = num / len;
        int second = num % len;
        if (num <= len) {
            column = alphabet[num] + "";
        } else {
            column = alphabet[first - 1] + "";
            if (second == 0) {
                column = column + alphabet[len] + "";
            } else {
                column = column + alphabet[second - 1] + "";
            }
        }
        return column;
    }
}
