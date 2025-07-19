package cn.tannn.jdevelops.util.excel.converter.enums;

import java.util.List;

/**
 * 支持List索引优化的枚举接口
 * 用于那些值可以用索引表示的枚举（如：0,1,2,3...）
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public interface IndexOptimizedEnum extends ExcelConvertEnum {

    /**
     * 获取所有Excel显示值的列表
     * 索引对应数据库存储值
     *
     * @return Excel显示值列表
     */
    List<String> getAllExcelDisplays();

    /**
     * 是否支持索引优化
     * 当数据库值恰好对应枚举在values()中的索引时返回true
     *
     * @return 是否支持索引优化
     */
    default boolean isIndexOptimized() {
        return false;
    }
}
