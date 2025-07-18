package cn.tannn.jdevelops.util.excel.converter;

import cn.tannn.jdevelops.util.excel.converter.enums.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Excel转换器工厂
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class ExcelConverterFactory {

    private static final Map<Class<? extends ExcelConvertEnum>, GenericExcelConverter> converterCache = new ConcurrentHashMap<>();

    /**
     * 获取转换器
     *
     * @param enumClass 枚举类
     * @return 转换器实例
     */
    public static GenericExcelConverter getConverter(Class<? extends ExcelConvertEnum> enumClass) {
        return converterCache.computeIfAbsent(enumClass, GenericExcelConverter::new);
    }

    /**
     * 获取性别转换器
     */
    public static GenericExcelConverter getGenderConverter() {
        return getConverter(GenderEnum.class);
    }

    /**
     * 获取请假状态转换器
     */
    public static GenericExcelConverter getLeaveStateConverter() {
        return getConverter(LeaveStateEnum.class);
    }

    /**
     * 获取学校状态转换器
     */
    public static GenericExcelConverter getSchoolStateConverter() {
        return getConverter(SchoolStateEnum.class);
    }

    /**
     * 获取账号状态转换器
     */
    public static GenericExcelConverter getAccountStateConverter() {
        return getConverter(AccountStateEnum.class);
    }

    /**
     * 获取账号类型转换器
     */
    public static GenericExcelConverter getAccountTypeConverter() {
        return getConverter(AccountTypeEnum.class);
    }

    /**
     * 获取账号可用状态转换器
     */
    public static GenericExcelConverter getAccountAvailableConverter() {
        return getConverter(AccountAvailableEnum.class);
    }

    /**
     * 获取是否转换器
     */
    public static GenericExcelConverter getWhetherConverter() {
        return getConverter(WhetherEnum.class);
    }

    /**
     * 获取有无转换器
     */
    public static GenericExcelConverter getYouWuConverter() {
        return getConverter(YouWuEnum.class);
    }

    /**
     * 获取状态转换器
     */
    public static GenericExcelConverter getStatusConverter() {
        return getConverter(StatusEnum.class);
    }

    /**
     * 获取数据类型转换器
     */
    public static GenericExcelConverter getDataTypeConverter() {
        return getConverter(DataTypeEnum.class);
    }

    /**
     * 获取组织类型转换器
     */
    public static GenericExcelConverter getOrgTypeConverter() {
        return getConverter(OrgTypeEnum.class);
    }

    /**
     * 获取系统配置转换器
     */
    public static GenericExcelConverter getSysConfigConverter() {
        return getConverter(SysConfigEnum.class);
    }
}
