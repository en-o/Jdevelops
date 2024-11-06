package cn.tannn.jdevelops.util.excel;

import cn.tannn.jdevelops.util.excel.handler.CellMenu;
import cn.tannn.jdevelops.util.excel.model.HeaderMenuData;
import com.alibaba.excel.annotation.ExcelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * excel相关功能
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/10/21 下午3:28
 */
public class ExcelService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelService.class);

    /**
     * 请假状态
     */
    public static List<String> LEAVE_STATE = Arrays.asList("请假", "正常");
    /**
     * 用户类型
     */
    public static List<String> SCHOOL_STATE = Arrays.asList("本校", "校外");


    /**
     * 下拉数据
     * <code>
     * <p> EasyExcelFactory.write(responseHeader.getOutputStream(), Bean.class)
     * <p> .registerWriteHandler(ExcelService.dropDownListLeaveState(6,LEAVE_STATE))
     * </code>
     *
     * @param index     需要下拉数据的表头下标
     * @param menuItems 列表数据
     * @return CellMenu
     */
    public static CellMenu dropDownList(Integer index, List<String> menuItems) {
        // 设置下拉
        HeaderMenuData headerMenuData = new HeaderMenuData(index, menuItems);
        return new CellMenu(headerMenuData);
    }

    /**
     * 表头新增填写须知
     * <code>
     *  <p> 这是自定义表头的用法，如果固定类的话用{@link  ExcelProperty#value()} 这个属性好像可以生成下面的效果
     *  <p> e.g  @ExcelProperty(value = {"remake","姓名"}, index = 0) @ExcelProperty(value = {"remake","性别"}, index = 1)
     *  <p> 上面的属性一样的固定值好像可以合并
     * </code>
     *
     * @param remake 表说明   [会放到第一行]
     * @param fields 表字段集合 [会放到第二行]
     * @return 表说明+表字段 = 表头
     */
    public static List<List<String>> writeHead(String remake, List<String> fields) {
        // ArrayList保证顺序
        List<List<String>> results = new ArrayList<>();
        fields.forEach(field -> {
            List<String> header = new ArrayList<>();
            header.add(field);
            results.add(concatHead(remake, header));
        });
        return results;
    }

    /**
     * 填写须知
     *
     * @param remake      第一行[表说明]
     * @param headContent 第二行[表字段]
     * @return concatHead
     */
    private static List<String> concatHead(String remake, List<String> headContent) {
        headContent.add(0, remake);
        return headContent;
    }


    /**
     * 分转元
     *
     * @param orderTotal 分
     * @return String
     */
    public static String fen2yuan(Integer orderTotal) {
        BigDecimal divide = BigDecimal.valueOf(orderTotal)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return divide.toString();
    }

    /**
     * 处理 sheet 名字问题
     * <p> 您为工作表或图表输入的名称无效。请确保:
     * <p> ·名称不多于 31 个字符。
     * <p> ·名称不包含下列任一字符::\/?*[ 或 ]。
     * <p> ·名称不为空。
     *
     * @return sheet name
     */
    public static String genSheet(String str) {
        try {
            // 第一步判空
            if (null != str && !str.trim().isEmpty()) {
                // 第二步截取
                if (str.length() > 25) {
                    str.substring(0, 25);
                }
                // 第三步替换特殊字符
                String regEx = "[：:\\\\/?*\\[\\]]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(str);
                return m.replaceAll("");
            }
        } catch (Exception e) {
            LOG.error("sheet名称异常 {}", e.getMessage());
        }
        return "sheet";
    }


}
