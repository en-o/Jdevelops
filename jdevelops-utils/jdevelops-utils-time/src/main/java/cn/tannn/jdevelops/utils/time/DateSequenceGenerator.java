package cn.tannn.jdevelops.utils.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 统计用的时间序列生成
 */
public class DateSequenceGenerator {

    /**
     * 生成日期序列
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @param timeType    timeType 1:日，2:周，3:月，4:年
     * @return 日期序列
     */
    public static List<String> generateSequence(String baseDateStr, Integer timeType) {
        return switch (timeType) {
            case 1 -> generateDaySequence(baseDateStr);
            case 2 -> generateWeekSequence(baseDateStr);
            case 3 -> generateMonthSequence(baseDateStr);
            case 4 -> generateYearSequence(baseDateStr);
            default -> throw new IllegalArgumentException("非法参数");
        };
    }

    /**
     * 生成日期序列
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @param timeType    timeType 1:日，2:周，3:月，4:年
     * @return 日期序列
     */
    public static List<String> generateSequence(String baseDateStr, Integer timeType, int size) {
        return switch (timeType) {
            case 1 -> generateDaySequence(baseDateStr,size);
            case 2 -> generateWeekSequence(baseDateStr,size);
            case 3 -> generateMonthSequence(baseDateStr,size);
            case 4 -> generateYearSequence(baseDateStr,size);
            default -> throw new IllegalArgumentException("非法参数");
        };
    }

    /**
     * 生成日期序列 - 年
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateYearSequence(String baseDateStr) {
        return generateYearSequence(baseDateStr, 8);
    }


    /**
     * 生成日期序列 - 年
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateYearSequence(String baseDateStr, int size) {
        List<String> result = new ArrayList<>();
        DateTimeFormatter inputFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormat.forPattern("yyyy");

        DateTime baseDate = inputFormat.parseDateTime(baseDateStr);

        for (int i = 0; i <= size; i++) {
            DateTime date = baseDate.minusYears(i);
            result.add(outputFormat.print(date));
        }
        return result;
    }


    /**
     * 生成日期序列 - 月
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateMonthSequence(String baseDateStr) {
        return generateMonthSequence(baseDateStr, 8);
    }

    /**
     * 生成日期序列 - 月
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateMonthSequence(String baseDateStr, int size) {
        List<String> result = new ArrayList<>();
        DateTimeFormatter inputFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormat.forPattern("yyyy-MM");

        DateTime baseDate = inputFormat.parseDateTime(baseDateStr);

        for (int i = 0; i <= size; i++) {
            DateTime date = baseDate.minusMonths(i);
            result.add(outputFormat.print(date));
        }
        return result;
    }



    /**
     * 生成日期序列 - 周
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateWeekSequence(String baseDateStr) {
        return generateWeekSequence(baseDateStr,8);
    }

    /**
     * 生成日期序列 - 周
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateWeekSequence(String baseDateStr, int size) {
        List<String> result = new ArrayList<>();
        DateTimeFormatter inputFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormat.forPattern("yyyy-ww");

        DateTime baseDate = inputFormat.parseDateTime(baseDateStr);

        for (int i = 0; i <= size; i++) {
            DateTime date = baseDate.minusWeeks(i);
            result.add(outputFormat.print(date));
        }
        return result;
    }


    /**
     * 生成日期序列 - 天
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateDaySequence(String baseDateStr) {
        return generateDaySequence(baseDateStr,8);
    }

    /**
     * 生成日期序列 - 天
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateDaySequence(String baseDateStr, int size) {
        List<String> result = new ArrayList<>();
        DateTimeFormatter inputFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTime baseDate = inputFormat.parseDateTime(baseDateStr);

        for (int i = 0; i <= 8; i++) {
            DateTime date = baseDate.minusDays(i);
            result.add(outputFormat.print(date));
        }
        return result;
    }


    /**
     * 生成日期序列 - 年
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateYearSequence(LocalDateTime baseDateStr) {
        return generateYearSequence(baseDateStr,8);
    }


    /**
     * 生成日期序列 - 年
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<String> generateYearSequence(LocalDateTime baseDateStr, int size) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            LocalDateTime localDateTime = baseDateStr.minusYears(i);
            result.add(localDateTime.getYear()+"");
        }
        return result;
    }

    /**
     * 生成日期序列 - 年
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<Integer> generateYearSequence2(LocalDateTime baseDateStr) {
        return generateYearSequence2(baseDateStr,8);
    }

    /**
     * 生成日期序列 - 年
     *
     * @param baseDateStr 基准日期字符串，格式为 "yyyy-MM-dd"
     * @return 日期序列
     */
    public static List<Integer> generateYearSequence2(LocalDateTime baseDateStr, int size) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            LocalDateTime localDateTime = baseDateStr.minusYears(i);
            result.add(localDateTime.getYear());
        }
        return result;
    }
}
