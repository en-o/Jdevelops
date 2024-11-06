package cn.tannn.jdevelops.utils.core.office;


import cn.tannn.jdevelops.utils.core.string.StringCoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表格工具类
 * <p> 废弃了,新的在 jdevelops-utils-excel#ExcelService
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-11-09 10:03
 */
@Deprecated
public class ExcelUtil {
    private static final Logger LOG = LoggerFactory.getLogger(StringCoding.class);

    /**
     * 动态表头
     *
     * @param fields 字段集合 （注意数据跟表头要位置一致）
     * @return 表头
     */
    public static List<List<String>> getHeaderByBean(List<String> fields, String remake) {
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
