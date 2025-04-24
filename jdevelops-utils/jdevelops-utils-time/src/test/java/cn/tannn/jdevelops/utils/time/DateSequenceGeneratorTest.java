package cn.tannn.jdevelops.utils.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateSequenceGeneratorTest {

    @Test
    void generateSequence() {

        String time = "2025-04-03";
        // 生成并打印所有序列

        System.out.println("Years sequence (DESC):");
        System.out.println(DateSequenceGenerator.generateSequence(time, 4));

        System.out.println("\nMonths sequence (DESC):");
        System.out.println(DateSequenceGenerator.generateSequence(time, 3));


        System.out.println("\nWeeks sequence (DESC):");
        System.out.println(DateSequenceGenerator.generateSequence(time, 2));


        System.out.println("\nDays sequence (DESC):");
        System.out.println(DateSequenceGenerator.generateSequence(time, 1));

    }
}
