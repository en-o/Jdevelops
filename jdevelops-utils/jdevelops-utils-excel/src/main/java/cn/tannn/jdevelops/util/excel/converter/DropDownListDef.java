package cn.tannn.jdevelops.util.excel.converter;

import cn.tannn.jdevelops.util.excel.converter.enums.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认下拉数据
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class DropDownListDef {
    /**
     * 请假状态
     */
    public static final List<String> LEAVE_STATE = getExcelDisplayList(LeaveStateEnum.values());

    /**
     * 用户类型
     */
    public static final List<String> SCHOOL_STATE = getExcelDisplayList(SchoolStateEnum.values());

    /**
     * 性别
     */
    public static final List<String> SEX_STATE = getExcelDisplayList(GenderEnum.values());

    /**
     * 账号状态
     */
    public static final List<String> ACCOUNT_STATE = getExcelDisplayList(AccountStateEnum.values());

    /**
     * 账号类型
     */
    public static final List<String> ACCOUNT_TYPE = getExcelDisplayList(AccountTypeEnum.values());

    /**
     * 账号可用状态
     */
    public static final List<String> ACCOUNT_AVAILABLE = getExcelDisplayList(AccountAvailableEnum.values());

    /**
     * 是否
     */
    public static final List<String> WHETHER = getExcelDisplayList(WhetherEnum.values());

    /**
     * 有无
     */
    public static final List<String> YOU_WU = getExcelDisplayList(YouWuEnum.values());

    /**
     * 状态
     */
    public static final List<String> STATUS = getExcelDisplayList(StatusEnum.values());

    /**
     * 数据类型
     */
    public static final List<String> DATA_TYPE = getExcelDisplayList(DataTypeEnum.values());

    /**
     * 组织类型
     */
    public static final List<String> ORG_TYPE = getExcelDisplayList(OrgTypeEnum.values());

    /**
     * 系统配置
     */
    public static final List<String> SYS_CONFIG = getExcelDisplayList(SysConfigEnum.values());

    /**
     * 从枚举数组中提取Excel显示值列表
     *
     * @param enumValues 枚举值数组
     * @return Excel显示值列表
     */
    private static List<String> getExcelDisplayList(ExcelConvertEnum[] enumValues) {
        return Arrays.stream(enumValues)
                .map(ExcelConvertEnum::getExcelDisplay)
                .collect(Collectors.toList());
    }
}
