package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * Excel转换器枚举基础接口
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public interface ExcelConvertEnum {
    /**
     * 获取Excel显示值
     * @return Excel中显示的文本
     */
    String getExcelDisplay();

    /**
     * 获取数据库存储值
     * @return 数据库中存储的值
     */
    Integer getDbValue();

    /**
     * 获取枚举名称
     * @return 枚举名称
     */
    String name();
}
